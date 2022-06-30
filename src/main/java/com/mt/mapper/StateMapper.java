package com.mt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mt.pojo.Battery;
import com.mt.pojo.State;
import org.springframework.stereotype.Repository;


@Repository
public interface StateMapper extends BaseMapper<State> {
    //所有的crud都编写完成了
}
