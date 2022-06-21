package com.mt.service;

import com.mt.pojo.user.User;
import com.mt.pojo.user.vo.BasicUser;
import com.mt.request.UserBaseRequest;

import java.util.Map;

public interface ProgrammeService {

    Map<String,Object> selectProgrammeDetails (Integer programme_id);

}
