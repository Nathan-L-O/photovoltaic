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
@TableName(value = "form")
@ApiModel(value="form", description="表格类")
public class Form {
    @ApiModelProperty(value="逆变器id",name="inverter_id",hidden = true)
    @TableId(type = IdType.AUTO,value = "form_id")
    private Integer form_id;

    @ApiModelProperty(value="表格json",name="form_json")
    private String form_json;

    @ApiModelProperty(value="方案id",name="programme_id")
    private Integer programme_id;

    @ApiModelProperty(value="需求发电量",name="demand_capacity")
    private String demand_capacity;

    @ApiModelProperty(value="实际发电量",name="actual_capacity")
    private String actual_capacity;

    @ApiModelProperty(value="错误信息",name="errmsg")
    private String errmsg;

    @ApiModelProperty(value="是否选用",name="chose")
    private boolean chose;



    public Form(String form_json,  String demand_capacity, String actual_capacity, String errmsg) {
        this.form_json = form_json;
        this.demand_capacity = demand_capacity;
        this.actual_capacity = actual_capacity;
        this.errmsg = errmsg;
    }

}