package com.mt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mt.pojo.Battery;
import com.mt.pojo.Form;
import org.springframework.stereotype.Repository;


@Repository
public interface BatteryMapper extends BaseMapper<Battery> {
    //所有的crud都编写完成了
}
