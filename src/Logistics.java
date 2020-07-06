import java.util.ArrayList;

/**
 * @author: yqw
 * @date: 2020/7/6
 * @description: 公司后勤部门类
 */
public class Logistics implements Department {
    private ArrayList<ClockInfo> allInfo;

    Logistics(){
        this.allInfo = new ArrayList<ClockInfo>();
    }

    @Override
    public void addClockInfo(ClockInfo clockInfo) {
        /*
         * 功能描述: 添加打卡信息类
         * @Param: [clockInfo]
         * @Return: void
         */
        this.allInfo.add(clockInfo);
    }

    @Override
    public void showClockInfo() {
        /*
         * 功能描述: 展示打卡信息
         * @Param: []
         * @Return: void
         */
        for (ClockInfo clockInfo : allInfo){
            System.out.println(clockInfo);
        }
    }
}
