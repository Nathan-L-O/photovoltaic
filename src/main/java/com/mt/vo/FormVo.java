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
    private String num;

    @ApiModelProperty(value="备注")
    private String remake;

    @ApiModelProperty(value="单价")
    private String price;

    @ApiModelProperty(value="关联项")
    private Double pid;

    @ApiModelProperty(value="修改数量 true可以 false不可以")
    private Boolean required_input_num;

    @ApiModelProperty(value="修改勾选")
    private Boolean required_select;

    @ApiModelProperty(value="下拉")
    private String[] options;


    public FormVo(Boolean select, Double id, String name, String num, String remake, String price, Double pid, Boolean required_input_num, Boolean required_select,String[] options) {
        this.select = select;
        this.id = id;
        this.name = name;
        this.num = num;
        this.remake = remake;
        this.price = price;
        this.pid = pid;
        this.required_input_num = required_input_num;
        this.required_select = required_select;
        this.options = options;
    }

    public FormVo() {
    }
}
