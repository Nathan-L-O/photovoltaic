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
@TableName(value = "inverter")
@ApiModel(value="inverter", description="逆变器")
public class Inverter {
    @ApiModelProperty(value="逆变器id",name="inverter_id",hidden = true)
    @TableId(type = IdType.AUTO,value = "inverter_id")
    private Integer inverter_id;

    @ApiModelProperty(value="逆变器名称",name="inverter_name")
    private String inverter_name;

    @ApiModelProperty(value="逆变器输出功率",name="inverter_output_power")
    private String inverter_output_power;

    @ApiModelProperty(value="价格",name="inverter_price")
    private String inverter_price;

    @ApiModelProperty(value="电压下限",name="inverter_lower_limit")
    private String inverter_lower_limit;

    @ApiModelProperty(value="电压上限",name="inverter_up_limit")
    private String inverter_up_limit;

    @ApiModelProperty(value="逆变器类型",name="inverter_up_limit")
    private String inverter_type;


}