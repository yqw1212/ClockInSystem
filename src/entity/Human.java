package entity;
import java.util.ArrayList;

import service.Department;

/**
 * @author: yqw
 * @date: 2020/7/6
 * @description: 公司人力部门类
 */
public class Human implements Department {
    private ArrayList<ClockInfo> allInfo;

    Human(){
        this.allInfo = new ArrayList<ClockInfo>();
    }

    @Override
    public void addClockInfo(ClockInfo clockInfo) {
        /*
         * 功能描述: 添加打卡信息类
         * @Param: [clockInfo]
         * @Return: void
         */
        if (this.allInfo.size() == 0){
            this.allInfo.add(clockInfo);
        }else {
            if(clockInfo.getBack() == null){
                this.allInfo.add(clockInfo);
            }else {
                //传来的打卡信息是签退信息
                for (ClockInfo c : this.allInfo) {
                    if (c.getId().equals(clockInfo.getId()) && c.getIn().getTime() == clockInfo.getIn().getTime()) {
                        c = clockInfo;
                    }
                }
            }
        }
        System.out.println("人事部收到");
    }

    @Override
    public void showClockInfo() {
        /*
         * 功能描述: 展示打卡信息
         * @Param: []
         * @Return: void
         */
        for (ClockInfo clockInfo : allInfo){
            System.out.println("ID:"+clockInfo.getId()+clockInfo);
        }
    }
}
