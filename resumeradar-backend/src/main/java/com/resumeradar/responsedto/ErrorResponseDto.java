package com.resumeradar.responsedto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {
	
	private String fieldName;
	private String message;

}
