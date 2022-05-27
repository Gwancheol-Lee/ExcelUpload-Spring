package co.kr.bp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import co.kr.bp.dto.BasicResponse;
import co.kr.bp.dto.ErrorResponse;
import co.kr.bp.dto.SuccessResponse;
import co.kr.bp.mapper.ExcelMapper;
import co.kr.bp.model.Excel;
import co.kr.bp.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExcelServiceImpl implements ExcelService{
	
	@Autowired
	ExcelUtils excelUtil;
	@Autowired
	ExcelMapper excelMapper;
	
	
	@Override
	public BasicResponse insertExcel(MultipartFile file) {
		SuccessResponse successRes = new SuccessResponse(null);
		ErrorResponse errorRes = new ErrorResponse(null);
		
		// 파일 존재하지 않는 경우
		if (file.isEmpty()) {
			errorRes.setErrorCode("404");
			errorRes.setErrorMessage("엑셀 파일을 찾을 수 없음.");
			return errorRes;
		}
  
		// 확장자 유효성 검사 -> 엑셀파일만 가능
		// tring ext = fileUtil.getExtension(file.getOriginalFilename());
//		String contentType = file.getContentType();
//
//		// if (!ext.equals("xlsx") && !ext.equals("xls")) {
//		if(!contentType.equals("xlsx") || !contentType.equals("xls")) {
//			res.setErrorCode("400");
//			res.setErrorMessage("엑셀 확장자 xlsx, xls만 업로드 가능함.");
//			return res;
//		}

		List<Excel> listExcel = new ArrayList<Excel>();

		// 엑셀의 셀데이터를 가져와서 VO에 담기
		List<Map<String, Object>> listMap = excelUtil.getListData(file, 1, 5);
		log.info("listMap => {}" + listMap);

		for (Map<String, Object> map : listMap) {
			Excel excelInfo = new Excel();
		
			// 각 셀의 데이터를 VO에 set한다.
			excelInfo.setCompanyid(map.get("0").toString());
			excelInfo.setUserid(map.get("1").toString());
			excelInfo.setName(map.get("2").toString());
			excelInfo.setPhone(map.get("3").toString());

			listExcel.add(excelInfo);
		}
		
		log.info("listMap => {}" + listExcel);
		try {			
			excelMapper.insertExcel(listExcel);
			successRes.setSuccessCode("200");
			successRes.setSuccessMessage("엑셀 업로드 성공");
		} catch (Exception e) {
			errorRes.setErrorCode("500");
			errorRes.setErrorMessage("엑셀 DB 업로드 도중 에러 발생 \n " + e.toString());
		}
		
		return successRes != null ? successRes : errorRes;
	}
	
	public ArrayList<HashMap<String, Object>> getExcelList(Long companyid) {
		ArrayList<HashMap<String, Object>> list = excelMapper.getExcelList(companyid);
		System.out.println(list);
		
		return list;
	}
}
