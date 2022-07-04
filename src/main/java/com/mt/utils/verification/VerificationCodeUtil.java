package com.mt.utils.verification;

import com.alibaba.fastjson.JSONObject;
import com.mt.utils.AssertUtil;
import com.mt.utils.enums.CaptchaType;
import com.mt.utils.enums.CommonResultCode;
import com.mt.utils.enums.RestResultCode;
import com.mt.utils.push.MessageUtil;
import com.mt.utils.verification.model.UserVerificationCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证码生成及校验工具
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/8 14:49
 */
@Component
public class VerificationCodeUtil {

    /**
     * 验证码长度
     */
    private static final int CODE_LENGTH = 4;

    /**
     * 数字验证码长度
     */
    private static final int NUM_CODE_LENGTH = 6;

    /**
     * 计划任务 Quartz Cron 表达式 : CleanCycle
     */
    private static final String CORN_STRING_CLEAN = "0 */1 * * * ?";

    /**
     * 计划任务 Quartz Cron 表达式 : Control
     */
    private static final String CORN_STRING_CONTROL = "0 0 12 * * ?";

    /**
     * 验证码容器
     */
    private static final Map<String, UserVerificationCode> V_POOL = new ConcurrentHashMap<>();

    /**
     * 校验容器
     */
    private static final Map<String, Long> P_POOL = new ConcurrentHashMap<>();

    /**
     * 控制容器
     */
    private static final Map<String, Integer> C_POOL = new ConcurrentHashMap<>();

    /**
     * 校验有效期
     */
    private static final int PERIOD_OF_VALIDITY = 60;

    /**
     * 短信验证码频率控制间隔
     */
    private static final int FREQ_FLAG = 120;

    /**
     * 同一号码单日最大发送上限
     */
    private static final int MAX = 10;

    /**
     * 纯数字正则
     */
    private static final String NUM_CODE_REGEX = "[0-9]+";

    /**
     * 手机号正则
     */
    private static final String MOBILE_PHONE_REGEX = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";


    /**
     * 注入验证码生命周期
     */
    @Value("${mengtu.util.verificationcode.lifetime}")
    private int lifetime;

    @Resource
    private MessageUtil messageUtil;

    /**
     * 记录验证码文件路径
     */
    @Value("${code.path}")
    private String path;

    /**
     * 生成随机（字母+数字）验证码
     *
     * @return
     */
    public static List<String> getCode() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < CODE_LENGTH; i++) {
            if (random.nextInt(2) % 2 == 0) {
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                int offset = random.nextInt(26);
                code.append((char) (offset == 14 ? offset + 1 + temp : offset + temp));
            } else {
                code.append(random.nextInt(9) + 1);
            }
        }

        List<String> codeObject = new ArrayList<>();
        codeObject.add(code.toString());

        return codeObject;
    }

    /**
     * 生成随机（数字）验证码
     *
     * @return
     */
    public static List<String> getNumCode() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < NUM_CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }

        List<String> codeObject = new ArrayList<>();
        codeObject.add(code.toString());

        return codeObject;
    }

    /**
     * 生成验证码并返回
     *
     * @param mobile username
     * @return code
     */
    public static String initCode(String mobile, CaptchaType captchaType) {
        AssertUtil.assertStringNotBlank(mobile);
        UserVerificationCode userVerificationCode = new UserVerificationCode(mobile, captchaType);
        V_POOL.put(mobile, userVerificationCode);
        return userVerificationCode.getCode();
    }


    /**
     * 短信验证码请求频率控制
     */
    private static void frequencyControl(String userId) {
        UserVerificationCode userVerificationCode = V_POOL.get(userId);
        if (userVerificationCode != null && userVerificationCode.getCode().matches(NUM_CODE_REGEX)) {
            long timeDifference = System.currentTimeMillis() / 1000 - userVerificationCode.getTimestamp();
            AssertUtil.assertTrue(timeDifference >= FREQ_FLAG, CommonResultCode.SYSTEM_ERROR, "操作太频繁啦，请" + (FREQ_FLAG - timeDifference) + "秒后再试");
        }
    }

    /**
     * 校验，一次有效
     *
     * @param mobile username
     * @param code 验证码
     */
    public static void validate(String mobile, String code) {
        AssertUtil.assertStringNotBlank(code);
        AssertUtil.assertTrue(V_POOL.containsKey(mobile), "验证码已失效");
        AssertUtil.assertTrue(V_POOL.get(mobile).getValidateCode().equalsIgnoreCase(code), "验证码错误");
        P_POOL.put(mobile, System.currentTimeMillis() / 1000);
        V_POOL.remove(mobile);
    }

    /**
     * 验证状态判断
     *
     * @param mobile username
     * @return
     */
    public static boolean validate(String mobile) {
        return P_POOL.containsKey(mobile);
    }

    /**
     * 验证状态更新
     *
     * @param mobile username
     */
    public static void afterCaptcha(String mobile) {
        P_POOL.remove(mobile);
    }

    /**
     * 生成登陆验证码（数字）并发送短信
     *
     * @param mobile
     */
    public void initLoginSmsCode(String mobile) {
        initSmsCode(mobile, "进行登陆操作");
    }

    /**
     * 生成注册验证码（数字）并发送短信
     *
     * @param mobile
     */
    public void initRegisterSmsCode(String mobile) {
        initSmsCode(mobile, "注册新账号");
    }

    /**
     * 生成密码重置验证码（数字）并发送短信
     *
     * @param mobile
     */
    public void initResetSmsCode(String mobile) {
        initSmsCode(mobile, "重置密码");
    }

    private void initSmsCode(String mobile, String action) {
        AssertUtil.assertTrue(validateMobile(mobile), RestResultCode.ILLEGAL_PARAMETERS, "手机号格式非法");
        frequencyControl(mobile);

        if (C_POOL.containsKey(mobile)) {
            int current = C_POOL.get(mobile);
            AssertUtil.assertTrue(current <= MAX, RestResultCode.PARTIAL_CONTENT, "当日获取验证码已超限");
            C_POOL.replace(mobile, current, ++current);
        } else {
            C_POOL.put(mobile, 1);
        }

        List<String> variableList = new ArrayList<>(4);
        variableList.add(0, action);
        variableList.add(1, initCode(mobile, CaptchaType.NUMBER));
        variableList.add(2, String.valueOf(lifetime));

        JSONObject jsonObject = messageUtil.send(mobile, variableList);
        recoding(jsonObject,mobile);
        AssertUtil.assertTrue(jsonObject.get("status").equals(0), "短信接口异常");
    }

    public boolean validateMobile(String mobile) {
        return mobile.matches(MOBILE_PHONE_REGEX);
    }

    /**
     * 验证码生命周期自动化管理。
     */
    @Scheduled(cron = CORN_STRING_CLEAN)
    private void cleanCycle() {
        AssertUtil.assertNotNull(lifetime, "生命周期设置异常");
        long currTime = System.currentTimeMillis() / 1000;
        for (Map.Entry<String, UserVerificationCode> i : V_POOL.entrySet()) {
            if (i.getValue().getTimestamp() + lifetime * 60 < currTime) {
                V_POOL.remove(i.getKey());
            }
        }
        for (Map.Entry<String, Long> i : P_POOL.entrySet()) {
            if (i.getValue() + PERIOD_OF_VALIDITY < currTime) {
                P_POOL.remove(i.getKey());
            }
        }
    }

    @Scheduled(cron = CORN_STRING_CONTROL)
    private void control() {
        for (Map.Entry<String, Integer> i : C_POOL.entrySet()) {
            C_POOL.remove(i.getKey());
        }
    }

    /**
     * 记录验证码
     */
    private void recoding(JSONObject jsonObject,String mobile){
        BufferedWriter out = null;
        String content = "";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM");
        String mikName = simpleDateFormat.format(date);
        simpleDateFormat = new SimpleDateFormat("MMdd");
        String fileName = simpleDateFormat.format(date);
        simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        String dateNow = simpleDateFormat.format(date);
        File writeFile = null;
        try {
            File mkdirsName = new File(path + mikName);
            if(!mkdirsName.exists()){
                mkdirsName.mkdirs();
            }
            if (jsonObject.get("status").equals(0)){
                content = dateNow + "   success:" + mobile;
                writeFile = new File(path + mikName + "/" + fileName + "_success" + ".txt");
                // 判断文件是否存在，不存在即新建
                // 存在即根据操作系统添加换行符
                if(!writeFile.exists()) {
                    writeFile.createNewFile(); // 创建新文件
                } else {
                    String osName = System.getProperties().getProperty("os.name");
                    if (osName.equals("Linux")) {
                        content = content + "\r";
                    } else {
                        content = content + "\r\n";
                    }
                }
            }else {
                content = dateNow + "   defeat:" + mobile + "   msgId:" + jsonObject.get("msgid") + "   status:" + jsonObject.get("status");
                writeFile = new File(path + mikName + "/" + fileName + "_defeat" + ".txt");
                // 判断文件是否存在，不存在即新建
                // 存在即根据操作系统添加换行符
                if(!writeFile.exists()) {
                    writeFile.createNewFile(); // 创建新文件
                } else {
                    String osName = System.getProperties().getProperty("os.name");
                    if (osName.equals("Linux")) {
                        content = content + "\r";
                    } else {
                        content = content + "\r\n";
                    }
                }
            }
            // 如果是在原有基础上写入则append属性为true，默认为false
            out = new BufferedWriter(new FileWriter(writeFile,true));
            out.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (out != null){
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
