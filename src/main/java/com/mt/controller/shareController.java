package com.mt.controller;

import com.mt.service.ProgrammeService;
import com.mt.utils.HashUtil;
import com.mt.utils.Result;
import io.swagger.annotations.Api;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.*;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

@RestController
@RequestMapping("/share")
@Api(tags = "分享链接")
public class shareController {

    @Resource
    ProgrammeService programmeService;

    private static final Integer SALT_LENGTH = 12;

    private static final String PASSWORD = "1qaz2wsx";

    @RequestMapping(value = "/{md5Url}/**")
    public Object redirects(@PathVariable("md5Url") String md5Url, HttpServletRequest request, HttpServletResponse response) {
        try{
            final String pathq =
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
            final String bestMatchingPattern =
                    request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
            String arguments = new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern,pathq);
            String moduleName;
            if(null!= arguments&&!arguments.isEmpty()){
                moduleName = md5Url +'/'+ arguments;
            } else {
                moduleName = md5Url;
            }

            byte[] base64decodedBytes = Base64.getDecoder().decode(moduleName);
            String JM = HashUtil.decrypt(new String(base64decodedBytes, "utf-8"),PASSWORD);
            String str = StringUtils.substring(JM,SALT_LENGTH);
            String programme_id = StringUtils.substringBefore(str,"~");
            String rang = StringUtils.substringAfter(str,"~");
            Map<String,Object> map = programmeService.selectProgrammeDetails(Integer.valueOf(programme_id));
            map.put("range",rang);
            return Result.success(map);
        }catch (Exception e){
            return Result.fail("解析失败");
        }


    }

    @RequestMapping(value = "getURL",method = RequestMethod.POST)
    public Result getURL(Integer programmer_id,String range) {
        try {
            String url = "";
            String random = RandomStringUtils.randomNumeric(SALT_LENGTH);
            String saltId = random + programmer_id +"~"+ range;
            url = HashUtil.encrypt(saltId.getBytes(),PASSWORD);

            return Result.success(Base64.getUrlEncoder().encodeToString(url.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
