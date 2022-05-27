package co.kr.bp.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import co.kr.bp.model.Excel;

@Repository
@Mapper
public interface ExcelMapper {
	public void insertExcel(List<Excel> listExcel);
	public ArrayList<HashMap<String, Object>> getExcelList(Long companyid);
}
