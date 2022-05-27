package co.kr.bp.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import co.kr.bp.model.Excel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExcelUtils {
		// 각 셀의 데이터타입에 맞게 값 가져오기
		public String getCellValue(XSSFCell cell) {

			String value = "";
			
			if(cell == null){
				return value;
			}

			switch (cell.getCellType()) {
				case STRING:
					value = cell.getStringCellValue();
					break;
				case NUMERIC:
					value = (int) cell.getNumericCellValue() + "";
					break;
				default:
					break;
			}
			return value;
		}

		// 엑셀파일의 데이터 목록 가져오기 (파라미터들은 위에서 설명함)
		public List<Map<String, Object>> getListData(MultipartFile file, int startRowNum, int columnLength) {

			List<Map<String, Object>> excelList = new ArrayList<Map<String,Object>>();
			
			try {
				OPCPackage opcPackage = OPCPackage.open(file.getInputStream());

				@SuppressWarnings("resource")
				XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);

				// 첫번째 시트
				XSSFSheet sheet = workbook.getSheetAt(0);
				
				int rowIndex = 0;
				int columnIndex = 0;

				// 첫번째 행(0)은 컬럼 명이기 때문에 두번째 행(1) 부터 검색
				for (rowIndex = startRowNum; rowIndex < sheet.getLastRowNum() + 1; rowIndex++) {
					XSSFRow row = sheet.getRow(rowIndex);

					// 빈 행은 Skip
					if (row.getCell(0) != null && !row.getCell(0).toString().isEmpty()) {

						Map<String, Object> map = new HashMap<String, Object>();

						int cells = columnLength;

						for (columnIndex = 0; columnIndex <= cells; columnIndex++) {
							XSSFCell cell = row.getCell(columnIndex);
							map.put(String.valueOf(columnIndex), getCellValue(cell));
							log.info(rowIndex + " 행 : " + columnIndex+ " 열 = " + getCellValue(cell));
						}
						
						excelList.add(map);
					}
				}

			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return excelList;
		}
		
		public ByteArrayInputStream writeExcel(ArrayList<HashMap<String, Object>> list) {
			Workbook workbook = new XSSFWorkbook();
			
			Sheet sheet = workbook.createSheet("Excel");
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 4000);
			
			Row header = sheet.createRow(0);
			CellStyle headerStyle = workbook.createCellStyle();
			XSSFFont font = ((XSSFWorkbook) workbook).createFont();
			
			Cell headerCell = header.createCell(0);
			headerCell.setCellValue("회사아이디");
			headerCell.setCellStyle(headerStyle);

			headerCell = header.createCell(1);
			headerCell.setCellValue("유저아이디");
			headerCell.setCellStyle(headerStyle);
			
			headerCell = header.createCell(2);
			headerCell.setCellValue("유저명");
			headerCell.setCellStyle(headerStyle);
			
			headerCell = header.createCell(3);
			headerCell.setCellValue("전화번호");
			headerCell.setCellStyle(headerStyle);
			
			// Next, let's write the content of the table with a different style:
			CellStyle style = workbook.createCellStyle();
			style.setWrapText(true);

			Cell cell = null;
			int RowNum = 1;
			System.out.println("start list size = " + list.size());
			for (int i=0; i<list.size(); i++) {
				HashMap<String, Object> oneList = list.get(i);
				System.out.println(oneList);
				
				Row row = sheet.createRow(RowNum);
				cell = row.createCell(0);
				cell.setCellValue(String.valueOf(oneList.get("companyid")));
				cell.setCellStyle(style);

				cell = row.createCell(1);
				cell.setCellValue(String.valueOf(oneList.get("userid")));
				cell.setCellStyle(style);
				
				cell = row.createCell(2);
				cell.setCellValue(String.valueOf(oneList.get("name")));
				cell.setCellStyle(style);
				
				cell = row.createCell(3);
				cell.setCellValue(String.valueOf(oneList.get("phone")));
				cell.setCellStyle(style);
				System.out.println(RowNum);
				RowNum++;
			}
			
			System.out.println("end");
			
			try {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            workbook.write(outputStream);
	            workbook.close();
	            return new ByteArrayInputStream(outputStream.toByteArray());

	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
		}
}
