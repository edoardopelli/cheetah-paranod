package org.cheetah.paranod.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetah.paranod.neo4j.ParentPom;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PomRawData {

	private String applicationId;
	private String applicationName;
	@Builder.Default
	private Map<String, String> dependenciesMap = new HashMap<>();
	@Builder.Default
	private List<String> dependenciesId = new ArrayList<>();
	
	private ParentPom parentPom ;
	
}
