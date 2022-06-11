package com.mt.controller;

import com.mt.mapper.BatteryMapper;
import com.mt.pojo.Battery;
import com.mt.utils.ReadExcelUtils;
import com.mt.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
@Api(tags = "文件上传")
public class UploadController {

    @Autowired
    private BatteryMapper batteryMapper;

    @RequestMapping(value = "uploadExcel",method = RequestMethod.POST)
    public Result<Object> pubggupload(@RequestParam("file") MultipartFile file,String type){
        try{
            List list = ReadExcelUtils.readExcel(file,type);
            if ("battery".equals(type)){
                for (Object battery : list) {
                    batteryMapper.insert((Battery) battery);
                }
            }
            return Result.success("上传成功");
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }
}
