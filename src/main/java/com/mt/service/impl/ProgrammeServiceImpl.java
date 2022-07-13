package com.mt.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mt.mapper.*;
import com.mt.pojo.Battery;
import com.mt.pojo.Form;
import com.mt.pojo.Inverter;
import com.mt.pojo.Programme;
import com.mt.pojo.user.User;
import com.mt.pojo.user.UserInfo;
import com.mt.pojo.user.vo.BasicUser;
import com.mt.request.UserBaseRequest;
import com.mt.service.ProgrammeService;
import com.mt.service.UserService;
import com.mt.utils.AssertUtil;
import com.mt.utils.BatteryType;
import com.mt.utils.HashUtil;
import com.mt.utils.UUIDUtil;
import com.mt.utils.enums.RestResultCode;
import com.mt.utils.verification.VerificationCodeUtil;
import com.mt.vo.FormVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ProgrammeServiceImpl implements ProgrammeService {

    @Autowired
    private ProgrammeMapper programmeMapper;

    @Autowired
    private FormMapper formMapper;

    @Autowired
    private InverterMapper inverterMapper;

    @Autowired
    private BatteryMapper batteryMapper;

    @Value("${image.ImageIp}")
    private String Ip;

    @Override
    public Map<String, Object> selectProgrammeDetails(Integer programme_id) {
        Map<String,Object> returnMap = new LinkedHashMap<>();
        Map<String,Object> map;
        Map<String,Object> battery_cluster;
        Battery battery = new Battery();
        Inverter inverter = new Inverter();
        Programme programme = programmeMapper.selectById(programme_id);
        List<Form> forms = formMapper.selectList(new QueryWrapper<Form>().eq("programme_id",programme_id));
        for (Form f : forms) {
            //每个架子的电池数量
            Integer battery_nums = null;
            battery_cluster = new LinkedHashMap<>();
            map = new LinkedHashMap<>();
            if (f.getChose() != null){
                map.put("form",f);
                List<FormVo> formVos = JSON.parseArray(f.getForm_json(), FormVo.class);
                for (FormVo formvo: formVos) {
                    if (formvo.getId() == 1.0){
                        inverter = inverterMapper.selectOne(new QueryWrapper<Inverter>()
                                .eq("inverter_name", StringUtils.substring(formvo.getName(),3)));
                    }
                    if (formvo.getName().contains("电池模组")){
                        battery_nums = formvo.getNum();
                        battery = batteryMapper.selectOne(new QueryWrapper<Battery>()
                                .eq("battery_name", Objects.equals(formvo.getName(), "电池模组 1C") ? "HJESLFP-76120" : "HJESLFP-38240"));
                    }
                    if (formvo.getName().contains("支架")){
                        if (battery_nums != null)
                        battery_nums = battery_nums/formvo.getNum();
                    }
                }
                battery_cluster = getBatteryCluster(battery_nums,battery_cluster,battery);
                map.put("battery_cluster",JSON.toJSONString(battery_cluster));
                map.put("battery",battery);
                map.put("inverter",inverter);
                programme.setInverter_type(inverter.getInverter_type());
                returnMap.put("programme",programme);
                if (f.getChose() == 1){
                    returnMap.put("Enable",map);
                }else if (f.getChose() == 0) {
                    returnMap.put("Deactivate",map);
                }
            }

        }
        if (returnMap.get("Enable") == null && returnMap.get("Deactivate") == null){
            if (forms.size() == 2) {
                //输出的第一个为实际发电量-需求发电量最小的方案
                Collections.sort(forms, Comparator.comparingInt(o -> (int) (Double.parseDouble(o.getDemand_capacity()) - Double.parseDouble(o.getActual_capacity()))));
                //如果第一个方案超出逆变器电压范围则将第二个方案设置为最优
                if (forms.get(0).getErrmsg() != null && forms.get(1).getErrmsg() == null) {
                    Form form = forms.get(1);
                    forms.set(1, forms.get(0));
                    forms.set(0, form);
                }
            }
            returnMap.put("forms",forms);
        }
        return returnMap;
    }

    public Map<String,Object> getBatteryCluster(Integer batteryNum, Map<String,Object> battery_cluster, Battery battery){
        BatteryType batteryType = Objects.equals(battery.getBattery_name(), "HJESLFP-76120") ? BatteryType.MODULE_TYPE_1C : BatteryType.MODULE_TYPE_0_5C;
        battery_cluster.put("品牌","海基");
        battery_cluster.put("型号",batteryType == BatteryType.MODULE_TYPE_1C ? "1C":"0.5C");
        battery_cluster.put("数量",batteryNum);
        battery_cluster.put("图片",Ip+"/"+(batteryType == BatteryType.MODULE_TYPE_1C ? "1C":"0.5C")+"/"+batteryNum+".png");
        battery_cluster.put("最大尺寸",batteryType == BatteryType.MODULE_TYPE_1C ? "551*728*2250mm":"1086*733*2250mm");
        battery_cluster.put("重量",batteryType == BatteryType.MODULE_TYPE_1C ? 1200-(85*(9-batteryNum))+"kg":2000-(85*(19-batteryNum))+"kg");
        battery_cluster.put("标称能量",String.format("%.2f",9.216*batteryNum)+"kWh");
        battery_cluster.put("标称容量",batteryType == BatteryType.MODULE_TYPE_1C ? "120AH @1C,25℃":"240AH@0.5C,25℃");
        battery_cluster.put("标称电压",batteryType == BatteryType.MODULE_TYPE_1C ? String.format("%.2f",76.8*batteryNum)+"V":String.format("%.2f",38.4*batteryNum)+"V");
        battery_cluster.put("工作电压范围",batteryType == BatteryType.MODULE_TYPE_1C ? String.format("%.2f",67.2*batteryNum)+"~"+String.format("%.2f",85.2*batteryNum)+"V":String.format("%.2f",33.6*batteryNum)+"~"+String.format("%.2f",42.6*batteryNum)+"V");
        battery_cluster.put("最大持续充电倍率",batteryType == BatteryType.MODULE_TYPE_1C ? "1C@25℃":"0.5C@25℃");
        battery_cluster.put("最大持续放电倍率",batteryType == BatteryType.MODULE_TYPE_1C ? "1C@25℃":"0.5C@25℃");
        battery_cluster.put("单体最大充电电压","3.65V");
        battery_cluster.put("单体最小充电电压","2.5V");
        battery_cluster.put("绝缘标准","电池箱绝缘电阻＞1GΩ(1000VDC)");
        battery_cluster.put("耐压标准","3110VDC，不发生击穿或闪络现象");
        battery_cluster.put("放电过流保护电流","150A@5S");
        battery_cluster.put("充电高温保护","45℃");
        battery_cluster.put("放电高温保护","50℃");
        battery_cluster.put("充电低温保护","0℃");
        battery_cluster.put("放点低温保护","-20℃");

        return battery_cluster;
    }

}
