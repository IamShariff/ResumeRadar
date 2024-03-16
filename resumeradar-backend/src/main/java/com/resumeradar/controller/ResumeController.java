package com.resumeradar.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.resumeradar.responsedto.ResumeAnalysisResult;
import com.resumeradar.service.ResumeAnalysisService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "/ResumeController")
@CrossOrigin(origins = "http://localhost:3000")
public class ResumeController {

	private final ResumeAnalysisService resumeAnalysisService;

	@PostMapping(value = "/topResumes")
	public ResponseEntity<List<ResumeAnalysisResult>> getTopResumes(@RequestParam("keywords") List<String> keywords,
			@RequestParam("count") int count, @RequestParam("resumeFiles") List<MultipartFile> resumeFiles) {
		return ResponseEntity.ok(resumeAnalysisService.getTopResumes(keywords, resumeFiles, count));
	}

}
