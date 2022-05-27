package co.kr.bp.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import co.kr.bp.dto.BasicResponse;
import co.kr.bp.dto.ErrorResponse;

@RestControllerAdvice
public class BadRequestHandler {
	
	// File Size 초과시
	@ExceptionHandler({MaxUploadSizeExceededException.class})
	public ResponseEntity<BasicResponse> uploadException(MaxUploadSizeExceededException exc, 
		      HttpServletRequest request,
		      HttpServletResponse response) {
		ErrorResponse errorResponse = new ErrorResponse(null);
		errorResponse.setErrorCode("404");
		errorResponse.setErrorMessage("Not Found File");
		
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(errorResponse);
	}
}
