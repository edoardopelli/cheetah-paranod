package org.cheetah.paranod.controller;

import org.cheetah.paranod.data.PomRawData;
import org.cheetah.paranod.service.AggregateRawDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rawdata")
public class AggragateRawDataController {

	@Autowired
	private AggregateRawDataService service;
	
	
	@PostMapping(path = "/aggregate")
	public ResponseEntity<String> aggregateData(@RequestBody PomRawData rawData){
		
		service.aggregate(rawData);
		
		return ResponseEntity.ok("ok");
	}
}
