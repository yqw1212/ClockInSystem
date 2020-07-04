import java.io.*;
import java.util.Date;

/**
 * @author: yqw
 * @date: 2020/7/2
 * @description: 打卡信息实体类
 */
public class ClockInfo {
    private String id;
    private Date in;
    private String inString;
    private Date back;
    private String backString;

    ClockInfo(String id){
        setId(id);
        this.in = null;
        this.back = null;
    }

    public String toString() {
        if(this.getBack() == null){
            return "签到:" + this.getIn() + ",签退:未完成" ;
        }else {
            return "签到:" + this.getIn() + ",签退:" + this.getBack() ;
        }
    }

    public String getId() {
        return id;
    }
    public Date getIn() {
        return in;
    }
    public Date getBack() {
        return back;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setIn() throws FileNotFoundException {
        this.in = new Date();
        this.inString = (getIn().getYear()+1900)+":"+(getIn().getMonth()+1)+":"+getIn().getDate()+":"+
                                getIn().getHours()+":"+ getIn().getMinutes()+":"+getIn().getSeconds();
        this.writeInfo("签到");
    }
    public void setBack() throws FileNotFoundException {
        this.back = new Date();
        this.backString = (getBack().getYear()+1900)+":"+(getBack().getMonth()+1)+":"+getBack().getDate()+":"+
                                    getBack().getHours() +":"+ getBack().getMinutes()+":"+getBack().getSeconds();
        this.writeInfo("签退");
    }

    public void writeInfo(String s) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/signInfo.dat",true))));
        if (s.equals("签到")){
            printWriter.println(this.id+"_签到"+"_"+this.inString);
        }else if(s.equals("签退")){
            printWriter.println(this.id+"_签退"+"_"+this.backString);
        }
        printWriter.close();
    }
}
