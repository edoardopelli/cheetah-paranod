import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Main2 {

	public static void main(String[] args) throws IOException {
		final String MAVEN_EXEC_PATH = System.getenv("MAVEN_EXEC_PATH");
		Process process = Runtime.getRuntime().exec(new String[] { MAVEN_EXEC_PATH, "dependency:tree",
				"\"-DoutputType=tgf\"", "\"-DoutputFile=file.tgf\"" });

		try (InputStream in = process.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			String line;
			String filePath = "";
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				if (line.indexOf("Wrote dependency tree to: ") != -1) {
					filePath = line.substring("[INFO] Wrote dependency tree to: ".length());
					break;
				}
			}
			try (InputStream file = new FileInputStream(filePath);
					BufferedReader fileReader = new BufferedReader(new InputStreamReader(file))) {
				Map<String, String> map = new HashMap<>();
				boolean sharpFound = false;
				boolean firstLine = true;
				String appArtifactId = "";
				String appArtifactName = "";
				List<String> appDependencies = new ArrayList<>();
				boolean dependency = false;
				while ((line = fileReader.readLine()) != null) {
					if(dependency) {
						//arrivato alla line # prendo solo gli id delle lib legate al pom.xml in esame
						if(line.indexOf(appArtifactId)!=-1) {
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
						if (line.indexOf("#") == -1) {
							map.put(getArtifactId(line), getArtifactName(line));
						} else {
							dependency = true;
						}
					}
				}
				System.out.println(appDependencies);
			}
		}

	}

	private static String getArtifactName(String line) {
		String appArtifactName;
		appArtifactName = line.substring(line.indexOf(" ") + 1);
		return appArtifactName;
	}

	private static String getArtifactId(String line) {
		String appArtifactId;
		appArtifactId = line.substring(0, line.indexOf(" "));
		return appArtifactId;
	}
}
