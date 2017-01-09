package org.shenjitang.common.util;

import jxl.Cell;
import jxl.CellType;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.read.biff.SheetImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.shenjitang.common.officeutils.ExcelParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 对POI的Excel表单对象操作工具类
 * @author yym
 * @date   2011-3-18
 */
public class PoiSheetUtil {
	private static Log logger = LogFactory.getLog(PoiSheetUtil.class);
    /**
     * 获取Excel表单对象的最大列
     * @param sheet  Excel表单对象
     * @return       Excel表单对象的最大列
     */
    public static int getSheetMaxCol(HSSFSheet sheet){
        int max = 0;
        for(int i = 0;i<sheet.getLastRowNum();i++){
            HSSFRow row = sheet.getRow(i);
            if(row==null){
                continue;
            }
            max = Math.max(max, row.getLastCellNum());
        }
        return max;
    }
    /**
     * 转换错误单元格值
     * @param errorCellValue  错误的单元格的值
     * @return                转换后的字符串格式的错误值
     */
    public static String convertError(byte errorCellValue) {
        String content = null;
        switch (errorCellValue) {
            case HSSFErrorConstants.ERROR_NULL:
                content = "#NULL!";
                break;
            case HSSFErrorConstants.ERROR_DIV_0:
                content = "#DIV/0!";
                break;
            case HSSFErrorConstants.ERROR_VALUE:
                content = "#VALUE!";
                break;
            case HSSFErrorConstants.ERROR_REF:
                content = "#REF!";
                break;
            case HSSFErrorConstants.ERROR_NAME:
                content = "#NAME?";
                break;
            case HSSFErrorConstants.ERROR_NUM:
                content = "#NUM!";
                break;
            case HSSFErrorConstants.ERROR_NA:
                content = "#N/A";
                break;
            default:
                content = "";
        }
        return content;
    }
	
	/**
	 * 获取表单中隐藏的行
	 * @param sheet
	 * @return 
	 */
	public static Integer[] getHiddenRow(HSSFSheet sheet){
		if(sheet == null || sheet.getLastRowNum() < 2){//行太少不做判断
			return null;
		}
		Set<Integer> set = new HashSet<Integer>();
		HSSFRow row = null;
		for(int i = 0;i<sheet.getLastRowNum();i++){
			row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			if(row.getZeroHeight()){
				set.add(i + 1);
			}
		}
		if(set.size() <= 0){
			return null;
		}
		Integer[] hiddenRows = new Integer[set.size()];
		int index = 0;
		for(Integer i : set){
			hiddenRows[index++] = i;
		}
		Arrays.sort(hiddenRows);
		return hiddenRows;
	}
	
    /**
	 * 获取一个POI单元格中的数据
	 * @param cell
	 * @return
	 */
    public static String getCellText(HSSFCell cell){
		if(cell == null){
			return "";
		}
		HSSFComment hssfcomment = cell.getCellComment();
		if (hssfcomment != null) {
            return hssfcomment.getString().toString();
		}
		String content = "";
		int type = cell.getCellType();
        switch(type){
            case HSSFCell.CELL_TYPE_STRING:
                content = cell.getRichStringCellValue().toString();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                double d = cell.getNumericCellValue();
                if(HSSFDateUtil.isCellDateFormatted(cell)){//如果表单对象中是日期格式，对其格式化
                    Date date = HSSFDateUtil.getJavaDate(d);
                    content = new SimpleDateFormat("yyyy-MM-dd").format(date);
                }else{
                    content = formatDouble(d, null);
                    if (StringUtilEx.matches(content, "^[0-9]*$")){
                    	if(cell.getCellStyle().getDataFormatString() == null || cell.getCellStyle().getDataFormatString().contains("\\ [$-")){
                    		Date date = cell.getDateCellValue();
                            content = new SimpleDateFormat("yyyyMMdd").format(date);
                    	}
                    }
                }
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                switch(cell.getCachedFormulaResultType()) {
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        content = String.valueOf(cell.getNumericCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_STRING:
                        content = cell.getRichStringCellValue().toString();
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        content = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_ERROR:
                        content = PoiSheetUtil.convertError(cell.getErrorCellValue());
                        break;
                }
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                content = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                content = "";
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                byte errorCellValue = cell.getErrorCellValue();
                content = PoiSheetUtil.convertError(errorCellValue);
                break;
            default:
                content = "";
        }
		return content;
	}
	/**
     * 格式化一个Double型数字
     * @param d       要格式化的数字
     * @param format  格式
     * @return
     */
    public static String formatDouble(Double d,String format){
        if(d == d.intValue()){
            return String.valueOf(d.intValue());
        }
        if(StringUtils.isBlank(format)){
            format = ExcelParser.GENERAL_FORMAT;
        }
        return new DecimalFormat(format).format(d);
    }
    
    /**
     * 以POI的方式读取Excel文件中的数据到一个map
     * map的key为Excel表单的名字，value为每个表单中的数据的二维数组
     * @param file--Excel文件对象
     * @return  如果POI不能解析该Excel文件，则返回null。
     */
    public static Map<String,String[][]> readExcelFileToMapByPOI(File file){
    	Map<String,String[][]> map = new HashMap<String,String[][]>();
    	try {
			HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(file));
			int sheetNum = book.getNumberOfSheets();
			HSSFSheet sheet = null;
			HSSFRow row = null;
			HSSFCell cell = null;
			String sheetName = "";
			String[][] data = null;
			for(int i = 0;i<sheetNum;i++){
				sheet = book.getSheetAt(i);
				if(sheet.getLastRowNum() == 0){
					continue;
				}
				sheetName = book.getSheetName(i);
				data = new String[sheet.getLastRowNum() + 1][];
				for(int j = 0;j <= sheet.getLastRowNum();j++){
					row = sheet.getRow(j);
					if(row == null){
						data[j] = new String[0];
						continue;
					}
					data[j] = new String[row.getLastCellNum()];
					for(int k = 0;k<row.getLastCellNum();k++){
						cell = row.getCell(k);
						data[j][k] = PoiSheetUtil.getCellText(cell);
					}
				}
				map.put(sheetName, data);
			}
		} catch (Exception e) {
			logger.error("不能使用POI方式解析 "+file.getAbsolutePath()+"。原因：", e);
			map =jxlParseExcel(file);
		}
    	return map;
    }
    
    public static Map<String,String[][]> jxlParseExcel(File file){
        Map<String,String[][]> map = new HashMap<String,String[][]>();
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(file);
        } catch (IOException ex) {
            logger.error(ex);
        } catch (BiffException ex) {
            logger.error(ex);
        }
        try {
            //创建sheet缓存表单
            int max = workbook.getNumberOfSheets();
            String[][] data=null;
            for (int i=0;i<max;i++) {
                SheetImpl sheet=(SheetImpl) workbook.getSheet(i);
                if(sheet.getRows()==0){
                    continue;
                }
                data = new String[sheet.getRows()][];
                // 填充单元格
                for (int j = 0; j < sheet.getRows(); j++) {
                    data[j] = new String[sheet.getColumns()];
                    for (int k = 0; k < sheet.getColumns(); k++) {
                        Cell cell = sheet.getCell(k, j);
                        if (cell.getType() == CellType.EMPTY) {
                            continue;
                        }
                        if (cell == null) {
                            continue;
                        }
                        String content = cell.getContents();
                        data[j][k]=content;
                    }
                }
                map.put(sheet.getName(), data);
            }
            logger.info("Excel 导入完成。");
        } catch (Exception ex) {
            logger.error("jxl解析异常：", ex);
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
        return map;
    }
}
