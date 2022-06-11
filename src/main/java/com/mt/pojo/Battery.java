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

    @ApiModelProperty(value="电池品牌",name="battery_brand")
    private String battery_brand;

    @ApiModelProperty(value="电池型号",name="battery_name")
    private String battery_name;

    @ApiModelProperty(value="电池尺寸",name="battery_size")
    private String battery_size;

    @ApiModelProperty(value="图片地址",name="battery_pic")
    private String battery_pic;

    @ApiModelProperty(value="电池电压",name="battery_voltage")
    private String battery_voltage;

    @ApiModelProperty(value="电池电流",name="battery_electric_current")
    private String battery_electric_current;

    public Battery(String battery_type, String battery_brand, String battery_name, String battery_size, String battery_pic, String battery_voltage, String battery_electric_current) {
        this.battery_type = battery_type;
        this.battery_brand = battery_brand;
        this.battery_name = battery_name;
        this.battery_size = battery_size;
        this.battery_pic = battery_pic;
        this.battery_voltage = battery_voltage;
        this.battery_electric_current = battery_electric_current;
    }
}