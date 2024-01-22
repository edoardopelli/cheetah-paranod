package org.cheetah.paranod.neo4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

import lombok.Builder;
import lombok.Data;

@Node("Parent")
@Data
@Builder
public class ParentPom implements Serializable {

	private static final long serialVersionUID = 4682886541250328291L;

	@Id
	private String id;
	private String groupId;
	private String artifactId;
	private String version;
	
	@Builder.Default
	@Relationship(type = "HAS_CHILD",direction = Direction.OUTGOING)
	private Collection<Artifact> artifacts = new ArrayList<>();
	
	
}
