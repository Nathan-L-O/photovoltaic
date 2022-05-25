package com.mt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;


@ApiModel("电池实体类")
@Data
public class Battery {
    //数量
    @ApiModelProperty(value="实用型电池数量")
    private Integer practical_battery_number;
    //发电量
    @ApiModelProperty(value="实用型实际发电量")
    private Double practical_capacity;

    @ApiModelProperty(value="实用型错误原因")
    private String practical_errmsg;


    //经济型
    //数量
    @ApiModelProperty(value="经济型电池数量")
    private Integer economic_battery_number;
    //发电量
    @ApiModelProperty(value="经济型实际发电量")
    private Double economic_capacity;

    @ApiModelProperty(value="经济型错误原因")
    private String economic_errmsg;

    //支架数量
    @ApiModelProperty(value="支架数量")
    private Integer bracket_number;

}
