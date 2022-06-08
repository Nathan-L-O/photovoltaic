package com.mt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mt.pojo.user.User;
import org.springframework.stereotype.Repository;

/**
 * UserMapper
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 13:29
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
}
