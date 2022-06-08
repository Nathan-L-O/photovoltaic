package com.mt.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.mt.utils.common.ToString;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 123
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 11:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseDO extends ToString {

    private static final long serialVersionUID = 2056670696961429082L;

    @TableField(value = "createTime", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
