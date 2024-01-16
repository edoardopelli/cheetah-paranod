package org.cheetah.paranod.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PomRawData {

	private String applicationId;
	private String applicationName;
	@Builder.Default
	private Map<String, String> dependenciesMap = new HashMap<>();
	@Builder.Default
	private List<String> dependenciesId = new ArrayList<>();
}
