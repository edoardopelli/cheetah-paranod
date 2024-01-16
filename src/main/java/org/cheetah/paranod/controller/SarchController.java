package org.cheetah.paranod.controller;

import java.util.List;

import org.cheetah.paranod.neo4j.Artifact;
import org.cheetah.paranod.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SarchController {
	
	@Autowired
	private SearchService service;

	@GetMapping("/dependency/usedby/{groupId}/{artifactId}/{version}")
	public ResponseEntity<List<Artifact>> getApplicationWithDependency(@PathVariable String groupId,@PathVariable String artifactId,@PathVariable String version){
		List<Artifact> result = service.getDependencyUsedBy(groupId,artifactId,version);
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/artifact/{groupId}/{artifactId}/{version}/dependencies")
	public ResponseEntity<Page<Artifact>> getDependencies(@PathVariable String groupId,@PathVariable String artifactId,@PathVariable String version){
		Page<Artifact> result = service.getApplicationDependencis(groupId,artifactId,version);
		
		return ResponseEntity.ok(result);
	}
}
