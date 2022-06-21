package com.mt.controller;

import com.mt.mapper.BatteryMapper;
import com.mt.mapper.InverterMapper;
import com.mt.pojo.Battery;
import com.mt.pojo.Inverter;
import com.mt.utils.ReadExcelUtils;
import com.mt.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @Autowired
    private InverterMapper inverterMapper;


    @ApiOperation(value="上传excel读取保存对于的实体类")
    @RequestMapping(value = "uploadExcel",method = RequestMethod.POST)
    public Result<Object> pubggupload(@RequestParam("file") MultipartFile file){
        try{
            String originalFilename = file.getOriginalFilename();
            String fileSuffix  =originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = originalFilename.substring(0,originalFilename.lastIndexOf("."));
            List list = ReadExcelUtils.readExcel(file);
            if ("电池".equals(fileName)){
                for (Object battery : list) {
                    batteryMapper.insert((Battery) battery);
                }
            }else if ("逆变器".equals(fileName)){
                for (Object inverter : list) {
                    inverterMapper.insert((Inverter) inverter);
                }
            }else {
                return Result.fail("文件名错误，请上传电池/逆变器");
            }
            return Result.success("上传成功");
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }
}
