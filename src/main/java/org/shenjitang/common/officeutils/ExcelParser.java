/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shenjitang.common.officeutils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.shenjitang.common.util.PoiSheetUtil;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author xiaolie
 */
public class ExcelParser {
    private static long fileSizeLimit = 5 * 1024 * 1024L;

    /**
     * 普通的数字格式format,默认的. General.
     */
    public final static String GENERAL_FORMAT = "###0.############";

    /**
     * 解析excel
     *
     * @param excelFile
     * @param pages       sheet表单名称
     * @param classLibDir 使用者当前程序的类路径目录以及lib目录  例如web项目   需要提供.../WEB-INF/classes和.../WEB-INF/lib 两个目录
     * @return
     * @throws Exception
     */
    public static Map<String, String[][]> parseExcel(File excelFile, String[] pages, String[] classLibDir) throws Exception {
        if (excelFile.length() > fileSizeLimit) {
            Runtime runtime = Runtime.getRuntime();
            String tmpdir = System.getProperty("java.io.tmpdir");
            String outFile = tmpdir + File.separator + "excel" + System.currentTimeMillis() + ".data";

            if (classLibDir.length == 0) {
                throw new RuntimeException("call process java ExcelParser: the parameter \"classLibDir\" is Empty");
            }

            String classpath = getFullClassPath(classLibDir);
            String[] envp = new String[]{"CLASSPATH=" + classpath};
            String cmd = "java -Xmx512m org.shenjitang.common.officeutils.ExcelParser " + excelFile.getPath() + " " + outFile;
            if (pages != null) for (String page : pages) {
                cmd += " " + page;
            }
            System.out.println("cmd:" + cmd);
            Process process = runtime.exec(cmd, envp, new File(tmpdir));
            BufferedReader errLog = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = errLog.readLine();
                if (line == null) break;
                sb.append(line).append("\n");
            }
            int code = process.waitFor();
            if (sb.length() > 0) {
                throw new RuntimeException("call process java ExcelParser: " + sb.toString());
            } else {
                ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(outFile));
                try {
                    Map<String, String[][]> map = (Map<String, String[][]>) inStream.readObject();
                    return map;
                } finally {
                    inStream.close();
                    FileUtils.forceDeleteOnExit(new File(outFile));
                }
            }
        } else {
            return parseExcelInMem(excelFile, pages);
        }
    }


    /**
     * 解析excel
     *
     * @param excelFile
     * @param classLibDir 使用者当前程序的类路径目录以及lib目录  例如web项目   需要提供.../WEB-INF/classes和.../WEB-INF/lib 两个目录
     * @return
     * @throws Exception
     */
    public static Map<String, String[][]> parseExcel(File excelFile, String[] classLibDir) throws Exception {
        String[] pages = getAllSheetName(excelFile);
        return parseExcel(excelFile, pages, classLibDir);
    }

    public static Map<String, String[][]> parseExcelInMem(File excelFile, String[] pages) throws Exception {
        String extension = FilenameUtils.getExtension(excelFile.getPath());
        if ("xlsx".equalsIgnoreCase(extension)) { // 2007
            return parse2007(excelFile, pages);
        } else { // 97
            return parse97(excelFile, pages);
        }
    }

    /**
     * 获取excel的所有表单名称
     *
     * @param excelFile
     * @return
     * @throws Exception
     */
    public static String[] getAllSheetName(File excelFile) throws Exception {
        String extension = FilenameUtils.getExtension(excelFile.getPath());
        if ("xlsx".equalsIgnoreCase(extension)) { // 2007
            return getAllSheetNamefor2007(excelFile);
        } else { // 97
            return getAllSheetNamefor97(excelFile);
        }
    }

    private static String[] getAllSheetNamefor97(File excelFile) throws Exception {
        FileInputStream is = new FileInputStream(excelFile);
        List<String> list = new ArrayList<String>();
        HSSFWorkbook wbs = new HSSFWorkbook(is);
        int sheetQty = wbs.getNumberOfSheets();
        for (int i = 0; i < sheetQty; i++) {
            HSSFSheet sheet = wbs.getSheetAt(i);
            list.add(sheet.getSheetName());
        }
        is.close();
        return list.toArray(new String[list.size()]);
    }

    private static String[] getAllSheetNamefor2007(File excelFile) throws Exception {
        FileInputStream is = new FileInputStream(excelFile);
        List<String> list = new ArrayList<String>();
        XSSFWorkbook wbs = new XSSFWorkbook(is);
        int sheetQty = wbs.getNumberOfSheets();
        for (int i = 0; i < sheetQty; i++) {
            XSSFSheet sheet = wbs.getSheetAt(i);
            list.add(sheet.getSheetName());
        }
        is.close();
        return list.toArray(new String[list.size()]);
    }

    /**
     * @param excelFile
     * @param pages
     * @return
     * @throws Exception
     */
    public static Map<String, String[][]> parse97(File excelFile, String[] pages) throws Exception {
        Map<String, String[][]> map = new HashMap();
        FileInputStream is = new FileInputStream(excelFile);
        HSSFWorkbook wbs = new HSSFWorkbook(is);
        Set<String> pageSet = new HashSet();
        if (pages != null && pages.length > 0) {
            pageSet.addAll(Arrays.asList(pages));
        }
        try {
            int sheetQty = wbs.getNumberOfSheets();
            for (int i = 0; i < sheetQty; i++) {
                HSSFSheet sheet = wbs.getSheetAt(i);
                if (!pageSet.isEmpty()) {
                    if (!pageSet.contains(sheet.getSheetName())) {
                        continue;
                    }
                }

                String[][] sheetMatrix = new String[sheet.getLastRowNum() + 1][];
                for (int j = 0; j <= sheet.getLastRowNum(); j++) {
                    HSSFRow row = sheet.getRow(j);
                    if (null != row && row.getLastCellNum() > 0) {
                        String[] rowArray = new String[row.getLastCellNum()];
                        for (int k = 0; k < row.getLastCellNum(); k++) {
                            HSSFCell cell = row.getCell(k);
                            if (null != cell) {
//                                rowArray[k] = cell.getStringCellValue();
//                            		rowArray[k] = getCellText97(cell);
                                rowArray[k] = getContent(cell);
                            }
                        }
                        sheetMatrix[j] = rowArray;
                    } else {
                        sheetMatrix[j] = new String[0];
                    }
                }
                //处理合并单元格，复制内容
                int mergedRangeSize = sheet.getNumMergedRegions();
                CellRangeAddress[] mergedRanges = new CellRangeAddress[mergedRangeSize];
                for (int k = 0; k < mergedRangeSize; k++) {
                    mergedRanges[k] = sheet.getMergedRegion(k);
                    int firstRow = mergedRanges[k].getFirstRow();
                    int lastRow = mergedRanges[k].getLastRow();
                    int firstCol = mergedRanges[k].getFirstColumn();
                    int lastCol = mergedRanges[k].getLastColumn();
                    String value = sheetMatrix[firstRow][firstCol];
                    for (int r = firstRow; r <= lastRow; r++) {
                        for (int c = firstCol; c <= lastCol; c++) {
                            if (r == firstRow && c == firstCol) {
                            } else {
                                sheetMatrix[r][c] = value;
                            }
                        }
                    }
                }
                map.put(sheet.getSheetName(), sheetMatrix);
            }
        } finally {
            is.close();
        }
        return map;
    }


    public static Map<String, String[][]> parse2007(File excelFile, String[] pages) throws Exception {
        Map<String, String[][]> map = new HashMap();
        FileInputStream is = new FileInputStream(excelFile);
        XSSFWorkbook wbs = new XSSFWorkbook(is);
        Set<String> pageSet = new HashSet();
        if (pages != null && pages.length > 0) {
            pageSet.addAll(Arrays.asList(pages));
        }
        try {
            int sheetQty = wbs.getNumberOfSheets();
            for (int i = 0; i < sheetQty; i++) {
                XSSFSheet sheet = wbs.getSheetAt(i);
                if (!pageSet.isEmpty()) {
                    if (!pageSet.contains(sheet.getSheetName())) {
                        continue;
                    }
                }
                String[][] sheetMatrix = new String[sheet.getLastRowNum() + 1][];
                for (int j = 0; j <= sheet.getLastRowNum(); j++) {
                    XSSFRow row = sheet.getRow(j);
                    if (null != row && row.getLastCellNum() > 0) {
                        String[] rowArray = new String[row.getLastCellNum()];
                        for (int k = 0; k < row.getLastCellNum(); k++) {
                            XSSFCell cell = row.getCell(k);
                            if (null != cell) {
                                if (cell.toString().endsWith(".0")) {
                                    rowArray[k] = cell.toString().substring(0, cell.toString().length() - 2);
                                } else {
                                    rowArray[k] = cell.toString();
                                }
//                                rowArray[k] = cell.getStringCellValue();
//                         	   	rowArray[k] = getCellText2007(cell);
//                            		rowArray[k] = getContent(cell);
                            }
                        }
                        sheetMatrix[j] = rowArray;
                    } else {
                        sheetMatrix[j] = new String[0];
                    }
                }
                //处理合并单元格，复制内容
                int mergedRangeSize = sheet.getNumMergedRegions();
                CellRangeAddress[] mergedRanges = new CellRangeAddress[mergedRangeSize];
                for (int k = 0; k < mergedRangeSize; k++) {
                    mergedRanges[k] = sheet.getMergedRegion(k);
                    int firstRow = mergedRanges[k].getFirstRow();
                    int lastRow = mergedRanges[k].getLastRow();
                    int firstCol = mergedRanges[k].getFirstColumn();
                    int lastCol = mergedRanges[k].getLastColumn();
                    String value = sheetMatrix[firstRow][firstCol];
                    for (int r = firstRow; r <= lastRow; r++) {
                        for (int c = firstCol; c <= lastCol; c++) {
                            if (r == firstRow && c == firstCol) {
                            } else {
                                sheetMatrix[r][c] = value;
                            }
                        }
                    }
                }
                map.put(sheet.getSheetName(), sheetMatrix);
            }
        } finally {
            is.close();
        }
        return map;
    }

    /**
     * poi获取单元格数据
     *
     * @param cell
     * @return
     */
    private static String getContent(HSSFCell cell) {
        int cellType = cell.getCellType();
        String content = null;
        switch (cellType) {
            case HSSFCell.CELL_TYPE_STRING:
                content = cell.getRichStringCellValue().toString();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    double d = cell.getNumericCellValue();
                    Date date = HSSFDateUtil.getJavaDate(d);
                    content = parseDate(date);
                } else {
                    content = round(cell.getNumericCellValue(), null);
                    if (match(content, "^[0-9]*$") && cell.getCellStyle().getDataFormatString() == null) {
                        Date date = cell.getDateCellValue();
                        content = parseDate(date);
                    } else {
                        if (match(content, "^[0-9]*$") && cell.getCellStyle().getDataFormatString().contains("\\ [$-")) {
                            Date date = cell.getDateCellValue();
                            content = parseDate(date);
                        }
                    }
                }
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            double d = cell.getNumericCellValue();
                            Date date = HSSFDateUtil.getJavaDate(d);
                            content = parseDate(date);
                        } else {
                            content = round(cell.getNumericCellValue(), null);
                            if (match(content, "^[0-9]*$") && cell.getCellStyle().getDataFormatString() == null) {
                                Date date = cell.getDateCellValue();
                                content = parseDate(date);
                            } else {
                                if (match(content, "^[0-9]*$") && cell.getCellStyle().getDataFormatString().contains("\\ [$-")) {
                                    Date date = cell.getDateCellValue();
                                    content = parseDate(date);
                                }
                            }
                        }
                        break;
                    case HSSFCell.CELL_TYPE_STRING:
                        content = cell.getRichStringCellValue().toString();
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        content = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_ERROR:
                        content = convertError(cell.getErrorCellValue());
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
                content = convertError(errorCellValue);
                break;
            default:
                content = "";
        }
        return content;
    }

    protected static String parseDate(Date date) {
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }


    /**
     * poi获取单元格数据
     *
     * @param cell
     * @return
     */
    private static String getContent(XSSFCell cell) {
        int cellType = cell.getCellType();
        String content = null;
        switch (cellType) {
            case XSSFCell.CELL_TYPE_STRING:
                content = cell.getRichStringCellValue().toString();
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    double d = cell.getNumericCellValue();
                    Date date = HSSFDateUtil.getJavaDate(d);
                    content = parseDate(date);
                } else {
                    content = round(cell.getNumericCellValue(), null);
                    if (match(content, "^[0-9]*$") && cell.getCellStyle().getDataFormatString() == null) {
                        Date date = cell.getDateCellValue();
                        content = parseDate(date);
                    } else {
                        if (match(content, "^[0-9]*$") && cell.getCellStyle().getDataFormatString().contains("\\ [$-")) {
                            Date date = cell.getDateCellValue();
                            content = parseDate(date);
                        }
                    }
                }
                break;
            case XSSFCell.CELL_TYPE_FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case XSSFCell.CELL_TYPE_NUMERIC:
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            double d = cell.getNumericCellValue();
                            Date date = HSSFDateUtil.getJavaDate(d);
                            content = parseDate(date);
                        } else {
                            content = round(cell.getNumericCellValue(), null);
                            if (match(content, "^[0-9]*$") && cell.getCellStyle().getDataFormatString() == null) {
                                Date date = cell.getDateCellValue();
                                content = parseDate(date);
                            } else {
                                if (match(content, "^[0-9]*$") && cell.getCellStyle().getDataFormatString().contains("\\ [$-")) {
                                    Date date = cell.getDateCellValue();
                                    content = parseDate(date);
                                }
                            }
                        }
                        break;
                    case XSSFCell.CELL_TYPE_STRING:
                        content = cell.getRichStringCellValue().toString();
                        break;
                    case XSSFCell.CELL_TYPE_BOOLEAN:
                        content = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case XSSFCell.CELL_TYPE_ERROR:
                        content = convertError(cell.getErrorCellValue());
                        break;
                }
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                content = String.valueOf(cell.getBooleanCellValue());
                break;
            case XSSFCell.CELL_TYPE_BLANK:
                content = "";
                break;
            case XSSFCell.CELL_TYPE_ERROR:
                byte errorCellValue = cell.getErrorCellValue();
                content = convertError(errorCellValue);
                break;
            default:
                content = "";
        }
        return content;
    }


    /**
     * 单元格数据读取error
     *
     * @param errorCellValue
     * @return
     */
    private static String convertError(byte errorCellValue) {
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

    public static String getCellText97(HSSFCell cell) {
        if (cell == null) {
            return "";
        }
        //获取批注  XG注释
//		HSSFComment hssfcomment = cell.getCellComment();
//		if (hssfcomment != null) {
//			return hssfcomment.getString().toString();
//		}
        String content = "";
        int type = cell.getCellType();
        switch (type) {
            case HSSFCell.CELL_TYPE_STRING:
                content = cell.getRichStringCellValue().toString();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                double d = cell.getNumericCellValue();
                if (isDateTime(cell)) {
                    content = getDateTimeContent(d);
                } else {
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {// 如果表单对象中是日期格式，对其格式化
                        Date date = HSSFDateUtil.getJavaDate(d);
                        content = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    } else {
                        content = formatDouble(d, null);
                    }
                }
                break;
            case HSSFCell.CELL_TYPE_FORMULA:

                switch (cell.getCachedFormulaResultType()) {
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        double dd = cell.getNumericCellValue();
                        if (isDateTime(cell)) {
                            content = getDateTimeContent(dd);
                        } else {
                            content = String.valueOf(dd);
                        }
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

    public static String getCellText2007(XSSFCell cell) {
        if (cell == null) {
            return "";
        }
        //获取批注  XG注释
//		XSSFComment hssfcomment = cell.getCellComment();
//		if (hssfcomment != null) {
//			return hssfcomment.getString().toString();
//		}
        String content = "";
        int type = cell.getCellType();
        switch (type) {
            case XSSFCell.CELL_TYPE_STRING:
                content = cell.getRichStringCellValue().toString();
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                double d = cell.getNumericCellValue();
                if (isDateTime(cell)) {
                    content = getDateTimeContent(d);
                } else {
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {// 如果表单对象中是日期格式，对其格式化
                        Date date = HSSFDateUtil.getJavaDate(d);
                        content = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    } else {
                        content = formatDouble(d, null);
                    }
                }
                break;
            case XSSFCell.CELL_TYPE_FORMULA:

                switch (cell.getCachedFormulaResultType()) {
                    case XSSFCell.CELL_TYPE_NUMERIC:
                        double dd = cell.getNumericCellValue();
                        if (isDateTime(cell)) {
                            content = getDateTimeContent(dd);
                        } else {
                            content = String.valueOf(dd);
                        }
                        break;
                    case XSSFCell.CELL_TYPE_STRING:
                        content = cell.getRichStringCellValue().toString();
                        break;
                    case XSSFCell.CELL_TYPE_BOOLEAN:
                        content = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case XSSFCell.CELL_TYPE_ERROR:
                        content = PoiSheetUtil.convertError(cell.getErrorCellValue());
                        break;
                }
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                content = String.valueOf(cell.getBooleanCellValue());
                break;
            case XSSFCell.CELL_TYPE_BLANK:
                content = "";
                break;
            case XSSFCell.CELL_TYPE_ERROR:
                byte errorCellValue = cell.getErrorCellValue();
                content = PoiSheetUtil.convertError(errorCellValue);
                break;
            default:
                content = "";
        }
        return content;
    }

    public static String formatDouble(Double d, String format) {
        if (d == d.intValue()) {
            return String.valueOf(d.intValue());
        }
        if (StringUtils.isBlank(format)) {
            format = GENERAL_FORMAT;
        }
        return new DecimalFormat(format).format(d);
    }

    public static void print(Map<String, String[][]> map) {
        for (Map.Entry<String, String[][]> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println("=================================================");
            for (String[] row : entry.getValue()) {
                for (String cell : row) {
                    System.out.print(cell);
                    System.out.print("  ");
                }
                System.out.println();
            }
            System.out.println("-------------------------------------------------");
        }
    }

    public static boolean isDateTime(HSSFCell cell) {
        HSSFCellStyle cellStyle = cell.getCellStyle();
        short dataFormat = cellStyle.getDataFormat();
        String dataForamtStr = cellStyle.getDataFormatString();
        if (dataFormat > 0 && StringUtils.isNotBlank(dataForamtStr)) {
            boolean isInQuote = false;
            for (int i = 0; i < dataForamtStr.length(); i++) {
                char c = dataForamtStr.charAt(i);
                if (c == '"') {
                    isInQuote = !isInQuote;
                } else {
                    if (!isInQuote) {
                        switch (c) {
                            case 'y':
                            case 'm':
                            case 'd':
                            case 'h':
                                return true;
                        }
                    }
                }

            }
        }
        return false;
    }

    public static boolean isDateTime(XSSFCell cell) {
        XSSFCellStyle cellStyle = cell.getCellStyle();
        short dataFormat = cellStyle.getDataFormat();
        String dataForamtStr = cellStyle.getDataFormatString();
        if (dataFormat > 0 && StringUtils.isNotBlank(dataForamtStr)) {
            boolean isInQuote = false;
            for (int i = 0; i < dataForamtStr.length(); i++) {
                char c = dataForamtStr.charAt(i);
                if (c == '"') {
                    isInQuote = !isInQuote;
                } else {
                    if (!isInQuote) {
                        switch (c) {
                            case 'y':
                            case 'm':
                            case 'd':
                            case 'h':
                                return true;
                        }
                    }
                }

            }
        }
        return false;
    }

    public static String getDateTimeContent(double d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0L);
        double time = d - ((Double) d).intValue();
        cal.add(Calendar.DATE, ((int) d) - 25568 - 1);
        if (time == 0) {
            return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        } else {
            long iTime = (long) (time * 24L * 3600L * 1000L);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.setTimeInMillis(cal.getTimeInMillis() + iTime);
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
        }
    }


    /**
     * 将excel列的英文转换为数值
     * AA - 26
     * BB - 53
     *
     * @param rowStr AA
     * @return
     */
    public static int getRowNum(String rowStr) {
        char[] rowStrArray = rowStr.toUpperCase().toCharArray();
        int len = rowStrArray.length;
        int n = 0;
        for (int i = 0; i < len; i++) {
            n += (((int) rowStrArray[i]) - 65 + 1) * Math.pow(26, len - i - 1);
        }
        return n - 1;
    }

    /**
     * 数字格式化
     *
     * @param num    要进行操作的数
     * @param format 格式化模板
     * @return
     */
    public static String round(Double num, String format) {
        if (num == num.intValue())
            return String.valueOf(num.intValue());
        if (format == null)
            format = GENERAL_FORMAT;
        return new DecimalFormat(format).format(num);
    }

    public static boolean match(String express, String rex) {
        Pattern pattern = Pattern.compile(rex);
        Matcher matcher = pattern.matcher(express);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean isWindowsSystem() {
        boolean isWindows = false;
        if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
            isWindows = true;
        }
        return isWindows;
    }

    public static String getFullClassPath(String[] classLibDir) {
        String classPathSeparator = isWindowsSystem() ? ";" : ":";
        String pathSeparator = File.pathSeparator;
        StringBuilder classpath = new StringBuilder();
        for (String path : classLibDir) {
            if (!path.endsWith("\\") && !path.endsWith("/")) {
                path += pathSeparator;
            }
            if (path.contains("classes" + pathSeparator)) {
                classpath.append(path.substring(0, path.length()));
            }

            if (path.contains("lib" + pathSeparator)) {
                File libPath = new File(path);
                File[] jarFiles = libPath.listFiles(new FilenameFilter() {

                    public boolean accept(File dir, String name) {
                        String extension = FilenameUtils.getExtension(name);
                        return extension.equalsIgnoreCase("jar") || extension.equalsIgnoreCase("zip");
                    }

                });
                for (File jarFile : jarFiles) {
                    classpath.append(classPathSeparator).append(jarFile.getPath());
                }

            }
        }
        return classpath.toString();
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: com.richeninfo.officeutils.ExcelParser [fileName]");
            //args = new String[] {"d:\\temp\\abc.xls", "d:\\temp\\abc.bin"};
        }
        try {
            File file = new File(args[0]);
            if (!file.exists()) {
                System.err.println("file:" + file + " not exist!");
                System.exit(-1);
            }
            File outFile = new File(args[1]);
            String[] pages = null;
            if (args.length > 2) {
                pages = new String[args.length - 2];
                for (int i = 2; i < args.length; i++) {
                    pages[i - 2] = args[i];
                }
            }
            Map<String, String[][]> map = parseExcelInMem(file, pages);
            ObjectOutputStream objOutputStream = new ObjectOutputStream(new FileOutputStream(outFile));
            try {
                objOutputStream.writeObject(map);
                objOutputStream.flush();
            } finally {
                objOutputStream.close();
            }
            print(map);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(-2);
        }
        System.exit(0);

    }

}
