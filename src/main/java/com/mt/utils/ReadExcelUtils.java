package com.mt.utils;

import com.mt.mapper.BatteryMapper;
import com.mt.pojo.Battery;
import lombok.Data;
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

    private static Map<PicturePosition, String> pictureMap;
    public static List readExcel(MultipartFile file,String type) throws Exception {
        List list = new ArrayList();
        //初始化图片容器
        pictureMap = new HashMap<>();

        String fileName = file.getOriginalFilename();// 文件名
        String fileExtension = ""; // 文件扩展名
        fileExtension = fileName.substring(fileName.lastIndexOf("."));

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
            //行遍历 跳过表头直接从数据开始读取
            for (int i = 1; i < rows+1; i++) {
                Row row = sheet.getRow(i);
                StringBuilder sb = new StringBuilder();
                Battery battery = new Battery();
                if ("battery".equals(type)) {
                    battery.setBattery_type(getCellValue(row.getCell(0)));
                    battery.setBattery_brand(getCellValue(row.getCell(1)));
                    battery.setBattery_name(getCellValue(row.getCell(2)));
                    battery.setBattery_size(getCellValue(row.getCell(3)));
                    battery.setBattery_pic(pictureMap.get(PicturePosition.newInstance(4, 4)));
                    battery.setBattery_voltage(getCellValue(row.getCell(5)));
                    battery.setBattery_electric_current(getCellValue(row.getCell(6)));
                    if (battery.getBattery_type().length()!=0){
                        list.add(battery);
                    }
                }
//                for (int j = 0; j < row.getLastCellNum(); j++) {
//                    Cell cell = row.getCell(j);
//                    if (j == 4){
//                        sb.append(pictureMap.get(PicturePosition.newInstance(i, 4)));
//                    }else {
//                        sb.append(getCellValue(cell));
//                    }
//                }
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
            String filePath = "D:\\pic\\pic" + UUID.randomUUID().toString() + "." + ext;
            byte[] data = pic.getData();
            FileOutputStream out = new FileOutputStream(filePath);
            out.write(data);
            out.close();
            return filePath;
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