package org.cheetah.paranod.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.cheetah.paranod.data.PomRawData;
import org.cheetah.paranod.neo4j.Artifact;
import org.cheetah.paranod.neo4j.Artifact.ArtifactBuilder;
import org.cheetah.paranod.repositories.ArtifactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AggregateRawDataService {
	
	@Autowired
	private ArtifactRepository repository;

	public Artifact aggregate(PomRawData rawData) {
		ArtifactBuilder builder = Artifact.builder();
		builder.id(rawData.getApplicationId());
		String applicationName = rawData.getApplicationName();
		Artifact artifact = createArtifact(builder,applicationName);
		
		List<String> dependenciesId = rawData.getDependenciesId();
		for (String id : dependenciesId) {
			
			String value = rawData.getDependenciesMap().get(id);
			Artifact dependency = createArtifact(Artifact.builder().id(id), value);
			artifact.getDependencies().add(dependency);
		}
		
		artifact = repository.save(artifact);
		return artifact;

	}

	private Artifact createArtifact(ArtifactBuilder builder, String applicationName) {

		StringTokenizer tokens = new StringTokenizer(applicationName, ":");
		builder.groupId(tokens.nextToken()).artifactId(tokens.nextToken()).type(tokens.nextToken())
				.version(tokens.nextToken());
		if(tokens.hasMoreElements()) {
			builder.scope(tokens.nextToken());
		}
		Artifact artifact = builder.build();
		return artifact;
	}

}
