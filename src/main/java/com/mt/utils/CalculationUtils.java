package com.mt.utils;

import com.mt.pojo.Inverter;
import com.mt.vo.Battery;
import io.swagger.models.auth.In;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CalculationUtils {
    //模组电量
    private static final double module_power = 9.216;

    /**
     * 判断是否合理 如果合理计算出发电量
     * @param inverter 逆变器
     * @param capacity 需求发电量
     * @param inverter_num 逆变器数量
     * @return
     */
    public static Map<String,Object> getGeneratingCapacity(Inverter inverter, String capacity,Integer inverter_num){
        Map<String,Object> map = new LinkedHashMap<>();
        //电池数量
        Double d = Double.parseDouble(capacity)/module_power;
        //判断模组类型
        ModuleType moduleType;
        int moduleTypeSize = Integer.parseInt(capacity) / (Integer.parseInt(inverter.getInverter_output_power())*inverter_num);
        if (moduleTypeSize>=2){
            moduleType = ModuleType.MODULE_TYPE_0_5C;
        }else {
            moduleType = ModuleType.MODULE_TYPE_1C;
        }
        //电池数量以及支架数量
        map = getBatteryNumber(moduleType,d);
        map.put("inverter_output_power", (Integer.parseInt(inverter.getInverter_output_power())*inverter_num));
        Battery practical = (Battery) map.get("practical");
        Battery economic = (Battery) map.get("economic");

        if (economic.getBattery_number()==null && economic.getCapacity()==null){
            //比较上下限
            Double lowerLimit = 2.8*moduleType.getName()*(practical.getBattery_number()/practical.getBracket_number());
            Double outputLimit = 3.55*moduleType.getName()*(practical.getBattery_number()/practical.getBracket_number());

            Double practical_capacity = module_power*practical.getBattery_number();
            practical.setCapacity(practical_capacity);
            //如果得出的发电量大于预期发电量 每个支架减少一个电池得出经济方案
            if (practical_capacity>Double.parseDouble(capacity)){
                economic.setBattery_number(practical.getBattery_number()-practical.getBracket_number());
                economic.setCapacity(economic.getBattery_number()*module_power);

                if (2.8*moduleType.getName()*(practical.getBattery_number()-practical.getBracket_number()/practical.getBracket_number()) < Double.parseDouble(inverter.getInverter_lower_limit())){
                    economic.setErrmsg("逆变器电压下限超出范围,超出:" + String.format("%.2f",
                            (Double.parseDouble(inverter.getInverter_lower_limit())-2.8*moduleType.getName()*(practical.getBattery_number()-practical.getBracket_number()/practical.getBracket_number()))));
                }

                if (3.55*moduleType.getName()*(practical.getBattery_number()-practical.getBracket_number()/practical.getBracket_number()) > Double.parseDouble(inverter.getInverter_up_limit()) + 5){
                    economic.setErrmsg("逆变器电压上限超出范围,超出:" + String.format("%.2f",
                            (3.55*moduleType.getName()*(practical.getBattery_number()-practical.getBracket_number()/practical.getBracket_number())-Double.parseDouble(inverter.getInverter_up_limit()))));
                }
            }else {
                if (lowerLimit<Double.parseDouble(inverter.getInverter_lower_limit())){
                    practical.setErrmsg("逆变器电压下限超出范围,超出:" + String.format("%.2f",
                            (Double.parseDouble(inverter.getInverter_lower_limit())-lowerLimit)));
                }
                if (outputLimit>Double.parseDouble(inverter.getInverter_up_limit()) + 5){
                    practical.setErrmsg("逆变器电压上限超出范围,超出:" + String.format("%.2f",
                            (outputLimit-Double.parseDouble(inverter.getInverter_up_limit()))));
                }
            }
        }else {
            Double Economic_lowerLimit = 2.8*moduleType.getName()*(economic.getBattery_number()/economic.getBracket_number());
            Double Economic_outputLimit = 3.55*moduleType.getName()*(economic.getBattery_number()/economic.getBracket_number());

            if (Economic_lowerLimit<Double.parseDouble(inverter.getInverter_lower_limit())){
                economic.setErrmsg("逆变器电压下限超出范围,超出:" + String.format("%.2f",
                        (Double.parseDouble(inverter.getInverter_lower_limit())-Economic_lowerLimit)));
            }
            if (Economic_outputLimit>Double.parseDouble(inverter.getInverter_up_limit()) + 5){
                economic.setErrmsg("逆变器电压上限超出范围,超出:" + String.format("%.2f",
                        (Economic_outputLimit-Double.parseDouble(inverter.getInverter_up_limit()))));
            }
            if (practical.getBattery_number() != null && practical.getCapacity() != null){
                Double lowerLimit = 2.8*moduleType.getName()*(practical.getBattery_number()/practical.getBracket_number());
                Double outputLimit = 3.55*moduleType.getName()*(practical.getBattery_number()/practical.getBracket_number());

                if (lowerLimit<Double.parseDouble(inverter.getInverter_lower_limit())){
                    practical.setErrmsg("逆变器电压下限超出范围,超出:" + String.format("%.2f",
                            (Double.parseDouble(inverter.getInverter_lower_limit())-lowerLimit)));
                }
                if (outputLimit>Double.parseDouble(inverter.getInverter_up_limit()) + 5){
                    practical.setErrmsg("逆变器电压上限超出范围,超出:" + String.format("%.2f",
                            (outputLimit-Double.parseDouble(inverter.getInverter_up_limit()))));
                }
            }
        }

        map.put("practical",practical);
        map.put("economic",economic);
        map.put("inverter_number",inverter_num);
        return map;
    }

    //取上一个偶数
    private static Integer getPreviousEvenNumber(Double d){
        Double previous = Math.floor(d);
        int i = previous.intValue();
        if (i%2==0){
            return i;
        }else {
            return i-1;
        }
    }
    //取下一个偶数
    private static Integer getNextEvenNumber(Double d){
        Double previous = Math.ceil(d);
        int i = previous.intValue();
        if (i%2==0){
            return i;
        }else {
            return i+1;
        }
    }

    /**
     * 获取正确的电池个数
     * @param moduleType 模组类型
     * @param number 电池个数
     * @return
     */
    private static Map<String,Object> getBatteryNumber(ModuleType moduleType,Double number){
        Map<String,Object> map = new LinkedHashMap<>();
        Battery practical = new Battery();
        Battery economic = new Battery();
        Integer previous = null;
        Integer next = null;

        if (ModuleType.MODULE_TYPE_1C == moduleType){
            if (number > 10){
                //电池个数
                //例：next 82/previous 80
                previous =  getPreviousEvenNumber(number);
                next = getNextEvenNumber(number);
            }else if (number == 10){
                previous = number.intValue();
                next = number.intValue();
            }else if (number < 10){
                previous = new Double(Math.floor(number)).intValue();
                next = previous+1;
            }
        }else if (ModuleType.MODULE_TYPE_0_5C == moduleType){
            if (number > 19){
                //电池个数
                //例：next 82/previous 80
                previous =  getPreviousEvenNumber(number);
                next = getNextEvenNumber(number);
            }else if (number == 19){
                previous = number.intValue();
                next = number.intValue();
            }else if (number < 19){
                previous = new Double(Math.floor(number)).intValue();
                next = previous+1;
            }
        }


        if (next != null && previous != null  ){
            double previous_bracketNumber = Double.valueOf(previous)/ (double) moduleType.getIndex();
            double next_bracketNumber = Double.valueOf(next)/ (double) moduleType.getIndex();
            //少的情况需要的支架数量
            Double ceil_previous_bracketNumber = Math.ceil(previous_bracketNumber);
            //多的情况需要的支架数量
            Double ceil_next_bracketNumber = Math.ceil(next_bracketNumber);

            //支架数为1的情况
            if (ceil_previous_bracketNumber == 1 && ceil_next_bracketNumber == 1){
                practical.setBracket_number(1);
                economic.setBracket_number(1);
                practical.setBattery_number(next);
                economic.setBattery_number(previous);
                practical.setCapacity(module_power * practical.getBattery_number());
                economic.setCapacity(module_power * economic.getBattery_number());
            }else if (0.0 == Double.valueOf(previous)%ceil_previous_bracketNumber){
                //如果电池除以支架除的尽的情况
                practical.setBracket_number(ceil_previous_bracketNumber.intValue());
                practical.setBattery_number(previous);
            }else if (0.0 == Double.valueOf(next)%ceil_next_bracketNumber){
                //如果电池除以支架除的尽的情况
                practical.setBracket_number(ceil_next_bracketNumber.intValue());
                practical.setBattery_number(next);
            }else {
                //电池数除以支架数除不尽的情况
                double bracket_number = Math.max(ceil_previous_bracketNumber,ceil_next_bracketNumber);
                practical.setBracket_number((int) bracket_number);
                economic.setBracket_number((int) bracket_number);
                double previous_batteryNumber_forEveryBracket = Double.valueOf(previous) / bracket_number;
                double next_batteryNumber_forEveryBracket = Double.valueOf(next) / bracket_number;
                if (Math.floor(previous_batteryNumber_forEveryBracket) == Math.floor(next_batteryNumber_forEveryBracket)){
                    economic.setBattery_number(new Double(Math.floor(previous_batteryNumber_forEveryBracket)).intValue() * (int) bracket_number);
                    economic.setCapacity(module_power * economic.getBattery_number());
                    practical.setBattery_number(new Double(Math.ceil(next_batteryNumber_forEveryBracket)).intValue() * (int) bracket_number);
                    practical.setCapacity(module_power * practical.getBattery_number());
                }else {
                    economic.setBattery_number(new Double(Math.floor(previous_batteryNumber_forEveryBracket)).intValue() * (int) bracket_number);
                    economic.setCapacity(module_power * economic.getBattery_number());
                    practical.setBattery_number(new Double(Math.floor(next_batteryNumber_forEveryBracket)).intValue() * (int) bracket_number);
                    practical.setCapacity(module_power * practical.getBattery_number());
                }
            }
        }
            map.put("practical",practical);
            map.put("economic",economic);

        return map;
    }

    //电池数除以支架数除不尽的情况

    public static void main(String[] args) {
//        Inverter inverter = new Inverter();
//        inverter.setInverter_output_power("250");
//        inverter.setInverter_up_limit("850");
//        inverter.setInverter_lower_limit("400");
//        try{
//            System.out.println(getGeneratingCapacity(inverter,"750"));
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
        Map map = new LinkedHashMap();
        map.put("1",1);
        map.put("2",2);
        map.put("2",3);
        System.out.println(map);
    }
}
