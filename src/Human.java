import java.util.ArrayList;

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
        this.allInfo.add(clockInfo);
    }

    @Override
    public void showClockInfo() {
        for (ClockInfo clockInfo : allInfo){
            System.out.println(clockInfo);
        }
    }
}
