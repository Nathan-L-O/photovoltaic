package com.mt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "state")
@ApiModel(value="state", description="项目状态")
public class State {
    @TableId(type = IdType.AUTO,value = "state_id")
    private Integer state_id;

    private String state_name;

    private String state_color;

    private String state_css;

    private String user_id;
}