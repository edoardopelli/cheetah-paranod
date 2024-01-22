package org.cheetah.paranod.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.cheetah.paranod.data.PomRawData;
import org.cheetah.paranod.data.PomRawData.PomRawDataBuilder;
import org.cheetah.paranod.neo4j.ParentPom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PomParser {

	@Autowired
	private Environment env;

	public PomRawData doParsing(InputStream pomxml) throws IOException {
		String suffix = String.valueOf(System.currentTimeMillis());
		String tempPom = "pom.xml." + suffix;
		File tempFile = new File(tempPom);
		IOUtils.copy(pomxml, new FileOutputStream(tempFile));
		ParentPom parentPom = readParent(tempFile);

		final String MAVEN_EXEC_PATH = env.getRequiredProperty("paranod.env.mavenExecPath");
		Process process = Runtime.getRuntime()
				.exec(new String[] { MAVEN_EXEC_PATH, "-f", tempPom, "-s", "/Users/edoardo/.m2/settings-vgi.xml",
						"dependency:tree", "\"-DoutputType=tgf\"", "\"-DoutputFile=file.tgf\"" });
		PomRawDataBuilder builder = PomRawData.builder();
		try (InputStream in = process.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			String line;
			String filePath = "";
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				log.info(line);
				if (line.indexOf("Wrote dependency tree to: ") != -1) {
					filePath = line.substring("[INFO] Wrote dependency tree to: ".length());
					break;
				}
			}
			try (InputStream file = new FileInputStream(filePath);
					BufferedReader fileReader = new BufferedReader(new InputStreamReader(file))) {
				Map<String, String> map = new HashMap<>();
				boolean firstLine = true;
				String appArtifactId = "";
				String appArtifactName = "";
				List<String> appDependencies = new ArrayList<>();
				boolean dependency = false;
				while ((line = fileReader.readLine()) != null) {
					if (dependency) {
						// arrivato alla line # prendo solo gli id delle lib legate al pom.xml in esame
						if (line.indexOf(appArtifactId) != -1) {
							StringTokenizer st = new StringTokenizer(line, " ");
							st.nextToken();
							appDependencies.add(st.nextToken());
						}
					}
					if (firstLine) {
						appArtifactId = getArtifactId(line);
						appArtifactName = getArtifactName(line);
						firstLine = false;
					} else {
						if (line.indexOf("#") == -1 && !dependency) {
							map.put(getArtifactId(line), getArtifactName(line));
						} else {
							dependency = true;
						}
					}
				}
				builder.applicationId(appArtifactId).applicationName(appArtifactName).dependenciesId(appDependencies)
						.dependenciesMap(map);
				if(parentPom!=null) {
					builder.parentPom(parentPom);
				}
			}
		}
		boolean deleted = tempFile.delete();
		log.info("File {} deleted:{}", tempFile.getName(), deleted);
		return builder.build();

	}

	public ParentPom readParent(File pomxml) {
		try {
			// Specifica il percorso del file pom.xml
			String filePath = "percorso/del/tuo/progetto/pom.xml";

			// Crea un lettore per il file pom.xml
			MavenXpp3Reader reader = new MavenXpp3Reader();
			Model model;

			try (FileReader fileReader = new FileReader(pomxml)) {
				// Effettua il parsing del file pom.xml
				model = reader.read(fileReader);
			}

			// Ottieni le informazioni del parent
			Parent parent = model.getParent();

			if (parent != null) {
				ParentPom.ParentPomBuilder parentBuilder = ParentPom.builder();
				parentBuilder.artifactId(parent.getArtifactId()).groupId(parent.getGroupId())
						.version(parent.getVersion()).id(String.valueOf(
								(parent.getGroupId() + parent.getArtifactId() + parent.getVersion()).hashCode()));
				return parentBuilder.build();

			} 
		} catch (Exception e) {
			log.error("",e);
		}
		return null;
	}

	private String getArtifactName(String line) {
		String appArtifactName;
		appArtifactName = line.substring(line.indexOf(" ") + 1);
		return appArtifactName;
	}

	private String getArtifactId(String line) {
		String appArtifactId;
		appArtifactId = line.substring(0, line.indexOf(" "));
		return appArtifactId;
	}
}
