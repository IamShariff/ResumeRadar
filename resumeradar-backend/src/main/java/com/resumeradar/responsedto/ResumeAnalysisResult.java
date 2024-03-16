package com.resumeradar.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ResumeAnalysisResult {

	private String resumeName;
    private int resumeScore;

}
