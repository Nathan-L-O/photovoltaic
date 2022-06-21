package com.mt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "battery")
@ApiModel(value="battery", description="电池实体类")
public class Battery {
    @ApiModelProperty(value="电池id",name="battery_id",hidden = true)
    @TableId(type = IdType.AUTO,value = "battery_id")
    private Integer battery_id;

    @ApiModelProperty(value="电池分类",name="battery_type")
    private String battery_type;

    @ApiModelProperty(value="电池名称",name="battery_name")
    private String battery_name;

    @ApiModelProperty(value="图片地址",name="battery_pic")
    private String battery_pic;

    @ApiModelProperty(value="json",name="battery_json")
    private String battery_json;



}