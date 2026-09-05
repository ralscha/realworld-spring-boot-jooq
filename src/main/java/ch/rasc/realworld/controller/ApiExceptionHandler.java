package ch.rasc.realworld.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jooq.exception.IntegrityConstraintViolationException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<Map<String, Map<String, List<String>>>> handleValidation(
			HandlerMethodValidationException exception) {
		Map<String, List<String>> errors = new LinkedHashMap<>();
		exception.getParameterValidationResults().forEach(result -> {
			String parameter = result.getMethodParameter().getParameterName();
			for (MessageSourceResolvable error : result.getResolvableErrors()) {
				errors.computeIfAbsent(parameter, key -> new ArrayList<>()).add(error.getDefaultMessage());
			}
		});
		return ResponseEntity.unprocessableEntity().body(Map.of("errors", errors));
	}

	@ExceptionHandler(IntegrityConstraintViolationException.class)
	public ResponseEntity<Map<String, Map<String, List<String>>>> handleConflict() {
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(Map.of("errors", Map.of("body", List.of("conflicts with an existing resource"))));
	}

}
