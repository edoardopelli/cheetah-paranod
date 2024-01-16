package org.cheetah.paranod.controller;

import java.io.IOException;

import org.cheetah.paranod.data.PomRawData;
import org.cheetah.paranod.service.PomParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/pom/analyzer")
@Slf4j
public class UploadPomXmlController {
	
	@Autowired
	private PomParser pomParser;

	
	@PostMapping(path =  "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PomRawData> postMethodName(@RequestPart("file") MultipartFile file) throws IOException {
		PomRawData rawData = pomParser.doParsing(file.getInputStream());
		if(rawData == null) {
			log.info("Raw data is null!");
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(rawData);
	}
	
}
