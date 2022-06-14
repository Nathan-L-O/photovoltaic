package com.mt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("电池实体类")
@Data
public class BatteryVo {
    //数量
    @ApiModelProperty(value="电池数量")
    private Integer battery_number;
    //发电量
    @ApiModelProperty(value="实际发电量")
    private String capacity;

    @ApiModelProperty(value="错误原因")
    private String errmsg;

    //支架数量
    @ApiModelProperty(value="支架数量")
    private Integer bracket_number;

}
