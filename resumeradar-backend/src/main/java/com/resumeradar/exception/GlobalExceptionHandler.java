package com.resumeradar.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.resumeradar.responsedto.ErrorResponseDto;

/**
 * Global exception handler for handling specific exceptions across the
 * application. Utilizes Spring's {@code @RestControllerAdvice} to provide
 * centralized exception handling.
 *
 * @see ExceptionHandler
 * @see RestControllerAdvice
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles instances of {@link GenericException} and returns an appropriate
	 * error response.
	 *
	 * @param genericException The instance of {@link GenericException} to be
	 *                         handled.
	 * @return A {@link ResponseEntity} containing the error response and HTTP
	 *         status code.
	 */
	@ExceptionHandler(GenericException.class)
	public ResponseEntity<ErrorResponseDto> genericExceptionHandler(GenericException genericException) {
		final String errorMessage = genericException.getMessage();
		final String fieldName = genericException.getFieldName();
		final HttpStatus errorCode = genericException.getHttpStatusCode();
		final ErrorResponseDto errorResponse = ErrorResponseDto.builder().fieldName(fieldName).message(errorMessage)
				.build();
		return new ResponseEntity<>(errorResponse, errorCode);
	}

	/**
	 * Handles instances of {@link MethodArgumentNotValidException} and returns
	 * field-level validation errors.
	 *
	 * @param message The instance of {@link MethodArgumentNotValidException} to be
	 *                handled.
	 * @return A {@link ResponseEntity} containing a map of field-level validation
	 *         errors and HTTP status code.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> methodArgsNotValidExceptionHandler(
			MethodArgumentNotValidException message) {
		Map<String, String> response = new HashMap<>();

		message.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String defaultMessage = error.getDefaultMessage();
			response.put(fieldName, defaultMessage);
		});

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
