package com.mt.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Calendar;

/**
 * 定时任务工具类
 */

@Component
public class TaskUtil {

    @Value("${code.path}")
    private String path;

    @Scheduled(cron = "0 0 0 1 * ?")
//    @Scheduled(cron = "0 4 13 * * ?") //测试
    public void printCodeInfo() throws IOException {
        OutputStream outputStream = null;
        String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String srcFile = System.getProperty("user.dir")+path + year + ".0" + month;
        File file = new File(srcFile);
        try {
            if (file.exists()){
                outputStream = new FileOutputStream(srcFile + ".zip");
                ZipUtil.toZip(srcFile,outputStream,true);
                ZipUtil.DeleteFolder(srcFile);
                outputStream.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
