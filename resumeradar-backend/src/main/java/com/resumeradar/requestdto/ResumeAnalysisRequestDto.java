package com.resumeradar.requestdto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public record ResumeAnalysisRequestDto(List<String> keywords, int count,List<MultipartFile> resumeFiles) {

}
