package com.mt.utils;

import com.mt.pojo.Inverter;
import com.mt.vo.Battery;

public class CalculationUtils {
    //模组电量
    private static final double module_power = 9.216;

    /**
     * 判断是否合理 如果合理计算出发电量
     * @param inverter 逆变器
     * @param capacity 输入的电量
     * @return
     */
    public static Battery getGeneratingCapacity(Inverter inverter, String capacity){
        Double d = Double.parseDouble(capacity)/module_power;
        //判断模组类型
        ModuleType moduleType;
        int moduleTypeSize = Integer.parseInt(capacity) / Integer.parseInt(inverter.getInverter_output_power());
        if (moduleTypeSize>=2){
            moduleType = ModuleType.MODULE_TYPE_0_5C;
        }else {
            moduleType = ModuleType.MODULE_TYPE_1C;
        }
        //电池数量以及支架数量
        Battery battery = getBatteryNumber(moduleType,d);

        if (battery.getEconomic_battery_number()==null && battery.getEconomic_capacity()==null){
            //比较上下限
            Double lowerLimit = 2.8*moduleType.getName()*(battery.getPractical_battery_number()/battery.getBracket_number());
            Double outputLimit = 3.55*moduleType.getName()*(battery.getPractical_battery_number()/battery.getBracket_number());

            Double practical_capacity = module_power*battery.getPractical_battery_number();
            battery.setPractical_capacity(practical_capacity);
            //如果得出的发电量大于预期发电量 每个支架减少一个电池得出经济方案
            if (practical_capacity>Double.parseDouble(capacity)){
                battery.setEconomic_battery_number(battery.getPractical_battery_number()-battery.getBracket_number());
                battery.setEconomic_capacity(battery.getEconomic_battery_number()*module_power);

                if (2.8*moduleType.getName()*(battery.getPractical_battery_number()-battery.getBracket_number()/battery.getBracket_number()) < Double.parseDouble(inverter.getInverter_lower_limit())){
                    battery.setEconomic_errmsg("逆变器电压下限超出范围,超出:" + String.format("%.2f",
                            (Double.parseDouble(inverter.getInverter_lower_limit())-2.8*moduleType.getName()*(battery.getPractical_battery_number()-battery.getBracket_number()/battery.getBracket_number()))));
                }

                if (3.55*moduleType.getName()*(battery.getPractical_battery_number()-battery.getBracket_number()/battery.getBracket_number()) > Double.parseDouble(inverter.getInverter_up_limit()) + 5){
                    battery.setEconomic_errmsg("逆变器电压上限超出范围,超出:" + String.format("%.2f",
                            (3.55*moduleType.getName()*(battery.getPractical_battery_number()-battery.getBracket_number()/battery.getBracket_number())-Double.parseDouble(inverter.getInverter_up_limit()))));
                }
            }else {
                if (lowerLimit<Double.parseDouble(inverter.getInverter_lower_limit())){
                    battery.setPractical_errmsg("逆变器电压下限超出范围,超出:" + String.format("%.2f",
                            (Double.parseDouble(inverter.getInverter_lower_limit())-lowerLimit)));
                }
                if (outputLimit>Double.parseDouble(inverter.getInverter_up_limit()) + 5){
                    battery.setPractical_errmsg("逆变器电压上限超出范围,超出:" + String.format("%.2f",
                            (outputLimit-Double.parseDouble(inverter.getInverter_up_limit()))));
                }
            }
        }else {
            Double Economic_lowerLimit = 2.8*moduleType.getName()*(battery.getEconomic_battery_number()/battery.getBracket_number());
            Double Economic_outputLimit = 3.55*moduleType.getName()*(battery.getEconomic_battery_number()/battery.getBracket_number());

            if (Economic_lowerLimit<Double.parseDouble(inverter.getInverter_lower_limit())){
                battery.setEconomic_errmsg("逆变器电压下限超出范围,超出:" + String.format("%.2f",
                        (Double.parseDouble(inverter.getInverter_lower_limit())-Economic_lowerLimit)));
            }
            if (Economic_outputLimit>Double.parseDouble(inverter.getInverter_up_limit()) + 5){
                battery.setEconomic_errmsg("逆变器电压上限超出范围,超出:" + String.format("%.2f",
                        (Economic_outputLimit-Double.parseDouble(inverter.getInverter_up_limit()))));
            }
            if (battery.getPractical_battery_number() != null && battery.getPractical_capacity() != null){
                Double lowerLimit = 2.8*moduleType.getName()*(battery.getPractical_battery_number()/battery.getBracket_number());
                Double outputLimit = 3.55*moduleType.getName()*(battery.getPractical_battery_number()/battery.getBracket_number());

                if (lowerLimit<Double.parseDouble(inverter.getInverter_lower_limit())){
                    battery.setPractical_errmsg("逆变器电压下限超出范围,超出:" + String.format("%.2f",
                            (Double.parseDouble(inverter.getInverter_lower_limit())-lowerLimit)));
                }
                if (outputLimit>Double.parseDouble(inverter.getInverter_up_limit()) + 5){
                    battery.setPractical_errmsg("逆变器电压上限超出范围,超出:" + String.format("%.2f",
                            (outputLimit-Double.parseDouble(inverter.getInverter_up_limit()))));
                }
            }
        }
        return battery;
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
    private static Battery getBatteryNumber(ModuleType moduleType,Double number){
        Battery battery = new Battery();
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
                battery.setBracket_number(1);
                battery.setPractical_battery_number(next);
                battery.setEconomic_battery_number(previous);
                battery.setPractical_capacity(module_power * battery.getPractical_battery_number());
                battery.setEconomic_capacity(module_power * battery.getEconomic_battery_number());
            }else
                //如果电池除以支架除的尽的情况
                if (0.0 == Double.valueOf(previous)%ceil_previous_bracketNumber){
                battery.setBracket_number(ceil_previous_bracketNumber.intValue());
                battery.setPractical_battery_number(previous);
            }else if (0.0 == Double.valueOf(next)%ceil_next_bracketNumber){
                battery.setBracket_number(ceil_next_bracketNumber.intValue());
                battery.setPractical_battery_number(next);
            }else {
                //电池数除以支架数除不尽的情况
                battery.setBracket_number(new Double(Math.max(ceil_previous_bracketNumber,ceil_next_bracketNumber)).intValue());
                double previous_batteryNumber_forEveryBracket = Double.valueOf(previous) / battery.getBracket_number();
                double next_batteryNumber_forEveryBracket = Double.valueOf(next) / battery.getBracket_number();
                if (Math.floor(previous_batteryNumber_forEveryBracket) == Math.floor(next_batteryNumber_forEveryBracket)){
                    battery.setEconomic_battery_number(new Double(Math.floor(previous_batteryNumber_forEveryBracket)).intValue() * battery.getBracket_number());
                    battery.setEconomic_capacity(module_power * battery.getEconomic_battery_number());
                    battery.setPractical_battery_number(new Double(Math.ceil(next_batteryNumber_forEveryBracket)).intValue() * battery.getBracket_number());
                    battery.setPractical_capacity(module_power * battery.getPractical_battery_number());
                }else {
                    battery.setEconomic_battery_number(new Double(Math.floor(previous_batteryNumber_forEveryBracket)).intValue() * battery.getBracket_number());
                    battery.setEconomic_capacity(module_power * battery.getEconomic_battery_number());
                    battery.setPractical_battery_number(new Double(Math.floor(next_batteryNumber_forEveryBracket)).intValue() * battery.getBracket_number());
                    battery.setPractical_capacity(module_power * battery.getPractical_battery_number());
                }
            }
        }


        return battery;
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
        Double d = 100.219132142143;
        System.out.println(String.format("%.2f", d));
    }
}
