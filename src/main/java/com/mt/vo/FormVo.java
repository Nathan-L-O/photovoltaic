package com.mt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("表格")
@Data
public class FormVo {
    @ApiModelProperty(value="勾选")
    private Boolean select;

    @ApiModelProperty(value="序号")
    private Double id;

    @ApiModelProperty(value="名称")
    private String name;

    @ApiModelProperty(value="数量")
    private Integer num;

    @ApiModelProperty(value="备注")
    private String remake;

    @ApiModelProperty(value="单价")
    private Double price;

    @ApiModelProperty(value="关联项")
    private Double pid;

    public FormVo(Boolean select, Double id, String name, Integer num, String remake, Double price, Double pid) {
        this.select = select;
        this.id = id;
        this.name = name;
        this.num = num;
        this.remake = remake;
        this.price = price;
        this.pid = pid;
    }

    public FormVo() {
    }
}
