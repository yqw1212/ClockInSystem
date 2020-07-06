import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: yqw
 * @date: 2020/7/2
 * @description: 打卡信息实体类
 */
public class ClockInfo {
    private SimpleDateFormat simpleDateFormat;
    private Calendar calendar;
    private String id;

    private Date in;//签到时间
    private Date back;//签退时间

    private String inString;//签到时间
    private String backString;//签退时间

    ClockInfo(String id){
        simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        calendar = Calendar.getInstance();
        setId(id);
        this.in = null;
        this.back = null;
    }

    public String toString() {
        /*
         * 功能描述: 以字符串形式返回打卡信息
         * @Param: [void]
         * @Return: java.lang.String
         */
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

    public void setInTime(String s) throws ParseException {
        /*
         * 功能描述: 将签到时间字符串解析为Date,并赋值给私有属性
         * @Param: [s]
         * @Return: void
         * @throws: ParseException
         */
        this.inString = s;
        this.in = simpleDateFormat.parse(this.inString);//将字符串解析为Date类型
    }
    public void setBackTime(String s) throws ParseException {
        /*
         * 功能描述: 将签退时间字符串解析为Date,并赋值给私有属性
         * @Param: [s]
         * @Return: void
         * @throws: ParseException
         */
        this.backString = s;
        this.back = simpleDateFormat.parse(this.backString);//将字符串解析为Date类型
    }

    public void setIn() throws FileNotFoundException {
        /*
         * 功能描述: 签到方法
         * @Param: [void]
         * @Return: void
         * @throws: FileNotFoundException
         */
        this.in = new Date();
        this.inString = simpleDateFormat.format(getIn());
        this.writeInfo("签到");
    }

    public void setBack() throws FileNotFoundException {
        /*
         * 功能描述: 签退方法
         * @Param: [void]
         * @Return: void
         * @throws: FileNotFoundException
         */
        this.back = new Date();
        this.backString = simpleDateFormat.format(getBack());
        this.writeInfo("签退");
    }

    public void writeInfo(String s) throws FileNotFoundException {
        /*
         * 功能描述: 将签到或签退信息写入文件
         * @Param: [s]
         * @Return: void
         * @throws: FileNotFoundException
         */
        PrintWriter printWriter = new PrintWriter(
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/signInfo.dat",true))));
        if (s.equals("签到")){
            printWriter.println(this.id+"_签到"+"_"+this.inString);
        }else if(s.equals("签退")){
            printWriter.println(this.id+"_签退"+"_"+this.backString);
        }
        printWriter.close();//关闭文件
    }
}
