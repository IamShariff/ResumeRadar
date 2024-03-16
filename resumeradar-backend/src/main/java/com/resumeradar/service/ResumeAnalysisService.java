package com.resumeradar.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.resumeradar.responsedto.ResumeAnalysisResult;

public interface ResumeAnalysisService {

	List<ResumeAnalysisResult> getTopResumes(List<String> keywords, List<MultipartFile> resumeFiles, int count);

}
