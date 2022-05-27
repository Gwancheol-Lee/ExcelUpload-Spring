package co.kr.bp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse extends BasicResponse{
	private String successMessage;
	private String successCode;
	
	public SuccessResponse(String successMessage) {
		this.successMessage = successMessage;
		this.successCode = "200";
	}
	public SuccessResponse(String successMessage, String successCode) {
		this.successMessage = successMessage;
		this.successCode = successCode;
	}

}
