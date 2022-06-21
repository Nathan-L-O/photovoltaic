package com.mt.utils;

import com.alibaba.fastjson.JSON;
import com.mt.mapper.BatteryMapper;
import com.mt.pojo.Battery;
import com.mt.pojo.Inverter;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReadExcelUtils {

    //TODO
    private static String filePath = "http://";

    private static Map<PicturePosition, String> pictureMap;
    public static List readExcel(MultipartFile file) throws Exception {
        List list = new ArrayList();
        //初始化图片容器
        pictureMap = new HashMap<>();

        String originalFilename = file.getOriginalFilename();// 文件名
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = originalFilename.substring(0,originalFilename.lastIndexOf("."));

        String fileFormat = "xlsx";
        Workbook workbook;
        byte [] byteArr=file.getBytes();
        try {
            if (ExcelFormatEnum.XLS.getValue().equals(fileExtension.toLowerCase())) {
                workbook = new HSSFWorkbook(new ByteArrayInputStream(byteArr));
            } else if (ExcelFormatEnum.XLSX.getValue().equals(fileExtension.toLowerCase())) {
                workbook = new XSSFWorkbook(new ByteArrayInputStream(byteArr));
            } else {
                throw new Exception("文件类型错误");
            }
            //读取excel所有图片
            if (ExcelFormatEnum.XLS.getValue().equals(fileFormat)) {
                getPicturesXLS(workbook);
            } else {
                getPicturesXLSX(workbook);
            }
 

            Sheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getLastRowNum(); //读取行数
            //第一行标题
            Row firstRow = sheet.getRow(0);
            //行遍历 跳过表头直接从数据开始读取
            for (int i = 1; i < rows+1; i++) {
                Row row = sheet.getRow(i);
                if (row.getCell(i) != null){
                    Map<String,String> map = new LinkedHashMap<>();
                    Battery battery = new Battery();
                    Inverter inverter = new Inverter();
                    if ("电池".equals(fileName)) {
                        battery.setBattery_type("1");
                        battery.setBattery_name(getCellValue(row.getCell(1)));
                        battery.setBattery_pic(pictureMap.get(PicturePosition.newInstance(i, 2)));

                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            Cell fistRowCell = firstRow.getCell(j);
                            if (j == 2){
                                map.put(getCellValue(fistRowCell),pictureMap.get(PicturePosition.newInstance(i, 2)));
                            }else {
                                map.put(getCellValue(fistRowCell),getCellValue(cell));
                            }
                        }
                        battery.setBattery_json(JSON.toJSONString(map));
                        if (battery.getBattery_name().length() != 0){
                            list.add(battery);
                        }
                    }else if ("逆变器".equals(fileName)){
                        String title = "";
                        inverter.setInverter_pic(pictureMap.get(PicturePosition.newInstance(i, 5)));
                        inverter.setInverter_name(getCellValue(row.getCell(2)));
                        inverter.setInverter_type("光储一体机".equals(getCellValue(row.getCell(0))) ? "2" : "1");
                        inverter.setInverter_lower_limit(StringUtils.substringBefore(getCellValue(row.getCell(6)),"~"));
                        inverter.setInverter_up_limit(StringUtils.substringBetween(getCellValue(row.getCell(6)),"~","V"));
                        inverter.setInverter_output_power(StringUtils.substringBefore(getCellValue(row.getCell(8)),"k"));
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            Cell fistRowCell = firstRow.getCell(j);
                            if (j == 5){
                                map.put(getCellValue(fistRowCell),pictureMap.get(PicturePosition.newInstance(i, 5)));
                            }else {
                                String firstCellValue = getCellValue(fistRowCell);
                                switch (Objects.requireNonNull(firstCellValue)){
                                    case "PV输入参数":
                                        title = "(PV)";break;
                                    case "直流侧参数":
                                        title = "(直流)";break;
                                    case "交流测 (并网)":
                                        title = "(并网)";break;
                                    case "交流参数 (离网端)":
                                        title = "(离网)";break;
                                    case "效率":
                                        title = "";break;
                                }
                                map.put((firstCellValue + title).trim(),getCellValue(cell));
                            }
                        }
                        inverter.setInverter_json(JSON.toJSONString(map));
                        if (inverter.getInverter_name().length() != 0){
                            list.add(inverter);
                        }
                    }
                }
            }
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


 
    /**
     * cell数据格式转换
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell){
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                //如果为时间格式的内容
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    //注：format格式 yyyy-MM-dd hh:mm:ss 中小时为12小时制，若要24小时制，则把小h变为H即可，yyyy-MM-dd HH:mm:ss
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    return sdf.format(HSSFDateUtil.getJavaDate(cell.
                            getNumericCellValue())).toString();
                } else {
                    return new DecimalFormat("0").format(cell.getNumericCellValue());
                }
            case HSSFCell.CELL_TYPE_STRING: // 字符串
                return cell.getStringCellValue();
            case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                return cell.getBooleanCellValue() + "";
            case HSSFCell.CELL_TYPE_FORMULA: // 公式
                return cell.getCellFormula() + "";
            case HSSFCell.CELL_TYPE_BLANK: // 空值
                return "";
            case HSSFCell.CELL_TYPE_ERROR: // 故障
                return null;
            default:
                return null;
        }
    }
    /**
     * 获取Excel2003的图片
     *
     * @param workbook
     */
    private static void getPicturesXLS(Workbook workbook) {
        List<HSSFPictureData> pictures = (List<HSSFPictureData>) workbook.getAllPictures();
        HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(0);
        if (pictures.size() != 0) {
            for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
                HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
                if (shape instanceof HSSFPicture) {
                    HSSFPicture pic = (HSSFPicture) shape;
                    int pictureIndex = pic.getPictureIndex() - 1;
                    HSSFPictureData picData = pictures.get(pictureIndex);
                    PicturePosition picturePosition = PicturePosition.newInstance(anchor.getRow1(), anchor.getCol1());
                    pictureMap.put(picturePosition, printImg(picData));
                }
            }
        }
    }
 
    /**
     * 获取Excel2007的图片
     *
     * @param workbook
     */
    private static void getPicturesXLSX(Workbook workbook) {
        XSSFSheet xssfSheet = (XSSFSheet) workbook.getSheetAt(0);
        for (POIXMLDocumentPart dr : xssfSheet.getRelations()) {
            if (dr instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) dr;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture pic = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = pic.getPreferredSize();
                    CTMarker ctMarker = anchor.getFrom();
                    PicturePosition picturePosition = PicturePosition.newInstance(ctMarker.getRow(), ctMarker.getCol());
                    pictureMap.put(picturePosition, printImg(pic.getPictureData()));
                }
            }
        }
    }
 
    /**
     * 保存图片并返回存储地址
     *
     * @param pic
     * @return
     */
    public static String printImg(PictureData pic) {
        try {
            String ext = pic.suggestFileExtension(); //图片格式
            String filename = UUID.randomUUID().toString();
//            String filePath = "E:\\pic\\pic" + filename + "." + ext;
            String filePath = "D:\\pic\\pic" + filename + "." + ext;
            byte[] data = pic.getData();
            FileOutputStream out = new FileOutputStream(filePath);
            out.write(data);
            out.close();
            return "http://localhost:8083/pic/pic" + filename + "." + ext;
        } catch (Exception e) {
            return "";
        }
    }
 
    /**
     * 图片位置
     * 行row 列 col
     */
    @Data
    public static class PicturePosition {
        private int row;
        private int col;
 
        public static PicturePosition newInstance(int row, int col) {
            PicturePosition picturePosition = new PicturePosition();
            picturePosition.setRow(row);
            picturePosition.setCol(col);
            return picturePosition;
        }
    }
 
    /**
     * 枚举excel格式
     */
    public enum ExcelFormatEnum {
        XLS(0, ".xls"),
        XLSX(1, ".xlsx");
 
        private Integer key;
        private String value;
 
        ExcelFormatEnum(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
 
        public Integer getKey() {
            return key;
        }
 
        public String getValue() {
            return value;
        }
    }


}