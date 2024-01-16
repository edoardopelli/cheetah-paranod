package org.cheetah.paranod.repositories;

import java.util.List;

import org.cheetah.paranod.neo4j.Artifact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface ArtifactRepository extends Neo4jRepository<Artifact, String> {

	@Query(value = "match (a:Artifact)-[r:DEPENDS_BY]->(d:Artifact) where d.groupId = $groupId and d.artifactId = $artifactId and d.version = $version return a")
	List<Artifact> findByRelationDependsBy(@Param("groupId") String groupId, @Param("artifactId") String artifactId,
			@Param("version") String version);

	@Query(value = "match (a:Artifact)-[r:DEPENDS_BY]->(d:Artifact) where a.groupId = $groupId and a.artifactId = $artifactId and a.version = $version return d", 
			countQuery = "match (a:Artifact)-[r:DEPENDS_BY]->(d:Artifact) where a.groupId = $groupId and a.artifactId = $artifactId and a.version = $version return count(d)")
	Page<Artifact> findDependencies(@Param("groupId") String groupId, @Param("artifactId") String artifactId,
			@Param("version") String version, Pageable pageable);
}
