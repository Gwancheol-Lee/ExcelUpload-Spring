package co.kr.bp.dto;

import java.io.ByteArrayOutputStream;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExcelResponse extends BasicResponse {
	private ByteArrayOutputStream stream = new ByteArrayOutputStream();
	private String code = null;
	private String message = null;
	
//	public void ExcelResponse(ByteArrayOutputStream stream) {
//		
//		return 
//	}
}
