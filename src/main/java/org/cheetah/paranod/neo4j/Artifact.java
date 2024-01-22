package org.cheetah.paranod.neo4j;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Node(labels = "Artifact")
@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Artifact {

	@Id
	private String id;
	private String groupId;
	private String artifactId;
	private String version;
	private String type;
	private String scope;
	
	@Relationship(type="DEPENDS_BY", direction =  Direction.OUTGOING)
	@Builder.Default
	@JsonProperty(access = Access.WRITE_ONLY)
	Set<Artifact> dependencies = new HashSet<>();

	@Builder.Default
	@Relationship(type = "USED_BY",direction = Direction.INCOMING)
	@JsonProperty(access = Access.WRITE_ONLY)
	Set<Artifact> usedBy = new HashSet<>();
	
	@Relationship(type = "HAS_PARENT",direction = Direction.OUTGOING)
	private ParentPom parent;
}
