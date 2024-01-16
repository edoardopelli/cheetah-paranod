package org.cheetah.paranod.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetah.paranod.neo4j.Artifact;
import org.cheetah.paranod.repositories.ArtifactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

	@Autowired
	private ArtifactRepository repository;

	@Autowired
	private Neo4jTemplate neo4jTemplate;

	//in base alla dipendenza in input cerca da che applicativi è usat 
	public List<Artifact> getDependencyUsedBy(String groupId, String artifactId, String version) {
		return repository.findByRelationDependsBy(groupId, artifactId, version);
	}

	//trova le dipendenze usate dall'applicativo
	public Page<Artifact> getApplicationDependencis(String groupId, String artifactId, String version) {
		return repository.findDependencies(groupId, artifactId, version,PageRequest.of(0, 10));
	}
	
}
