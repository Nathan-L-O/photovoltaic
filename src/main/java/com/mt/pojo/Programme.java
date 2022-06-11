package com.mt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "programme")
@ApiModel(value="programme", description="方案实体类")
public class Programme {
    @ApiModelProperty(value="方案id",name="programme_id",hidden = true)
    @TableId(type = IdType.AUTO,value = "programme_id")
    private Integer programme_id;

    @ApiModelProperty(value="方案名称",name="programme_name")
    private String programme_name;

    @ApiModelProperty(value="用户id",name="user_id")
    private String user_id;

    @ApiModelProperty(value="更新日期",name = "update_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date update_date;

    @ApiModelProperty(value="收藏（1收藏",name="isCollection")
    private Integer isCollection;

    @ApiModelProperty(value="删除（1删除",name="isDelete")
    private Integer isDelete;

    @ApiModelProperty(value="需求发电量",name="demand_capacity")
    private String demand_capacity;

    @ApiModelProperty(value="实际发电量",name="actual_capacity")
    private String actual_capacity;

    @ApiModelProperty(value="方案状态",name="programme_state")
    private String programme_state;
}