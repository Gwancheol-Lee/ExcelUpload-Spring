package co.kr.bp.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.multipart.MultipartFile;

import co.kr.bp.dto.BasicResponse;

public interface ExcelService {
	
	public BasicResponse insertExcel(MultipartFile file);
	public ArrayList<HashMap<String, Object>> getExcelList(Long companyid);
}
