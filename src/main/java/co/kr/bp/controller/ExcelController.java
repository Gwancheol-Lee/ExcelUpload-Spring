package co.kr.bp.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;

import co.kr.bp.dto.BasicResponse;
import co.kr.bp.service.ExcelService;
import co.kr.bp.utils.ExcelUtils;

@CrossOrigin(origins = "https://cloudrecord.ml", allowedHeaders = "*")
@RestController
public class ExcelController {
  private static final Logger log = LoggerFactory.getLogger(ExcelController.class);
  
  @Autowired
  ExcelService excelService;
  @Autowired
  ExcelUtils excelUtil;
  
  @RequestMapping(method = {RequestMethod.GET}, value = {"/test"})
  public String Test() {
	  System.out.println("test");
	  return "Test";
  }
  
  @RequestMapping(method = {RequestMethod.GET}, value = {"/xls/download/{companyid}"})
  public void ExcelDownload(HttpServletRequest request, HttpServletResponse response, @PathVariable(required = true) Long companyid) throws IOException {
	  log.info("download start");
	  JsonObject json =new JsonObject();
	  
	  ByteArrayInputStream stream = null;
	  ArrayList<HashMap<String, Object>> list = excelService.getExcelList(companyid);
	  if (list.size() > 0) {
		  try {
			  stream = excelUtil.writeExcel(list);
		  } catch (Exception e) {
			  log.info("ExcelServiceImpl workbook write error => {} " + e.toString());
		  }
		  
		  try {
			  IOUtils.copy(stream, response.getOutputStream());
			  response.setHeader("Content-Disposition", "attachment;filename=cloudrecordLineInfo.xlsx");
		  } catch (IOException e) {
			  response.sendError(500, "엑셀 파일 생성 실패");
			  e.printStackTrace();
		  }
	  }
  }
  
  @RequestMapping(method = {RequestMethod.POST}, value = {"/xls/upload"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<BasicResponse> ExcelUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) {
	  System.out.println("upload");
	  log.info("upload start");
//	  return ResponseEntity.ok().build();
	  return ResponseEntity.ok().body(excelService.insertExcel(file));
  }
  
  
  // 샘플
  /*
  @RequestMapping(method = {RequestMethod.POST}, value = {"/xls/download"})
  public ResponseEntity<ResponseMessage> ExcelDownload(Model model) {
	  String message = "";
	  
	  String[] names = {"자몽", "애플망고", "멜론", "오렌지"};
      long[] prices = {5000, 10000, 7000, 6000};
      int[] quantities = {50, 50, 40, 40};
      
      List<Fruit> list = excelService.makeFruitList(names, prices, quantities);
      
      SXSSFWorkbook workbook = excelService.excelFileDownloadProcess(list);
      
      model.addAttribute("locale", Locale.KOREA);
      model.addAttribute("workbook", workbook);
      model.addAttribute("workbookName", "과일표");
      System.out.println(list);
      System.out.println(workbook);
      // InputStreamResource file = new InputStreamResource(model);
      
      return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test")
			.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
			.body(file);
  }
  */
  
  
  
//  @RequestMapping(method = {RequestMethod.POST}, value = {"/xls/upload"})
//  public String ExcelUpload(@RequestBody(required = true) Object body) {
//    JsonObject result = new JsonObject();
//    
//    
//    result.addProperty("CODE", Integer.valueOf(200));
//    result.addProperty("MESSAGE", "OK");
//    return result.toString();
//  }
}
