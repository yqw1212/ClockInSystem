package entity;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import service.Department;
import service.EmployeeFactory;
import service.Staff;
import view.MainFrame;

/**
 * @author: yqw
 * @date: 2020/7/7
 * @description: 容器类
 */
public class CompanyGUI {
	private SimpleDateFormat simpleDateFormat;
    private List<Department> departments;

    private static BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    private HashSet<Staff> allEmployee;
    private ArrayList<ClockInfo> allInfo;
    private int ini_inTime = 480, ini_backTime = 1080;
    public static CompanyGUI company = null;


    public static CompanyGUI getInstance() throws IOException, ParseException {
        /*
         * 功能描述: 单例模式
         * @Param: []
         * @Return: Company
         * @throws: IOException, ParseException
         */
        if (company == null){
            ini();
        }
        return company;
    }

    private CompanyGUI() throws IOException, ParseException {
        /*
         * 功能描述: 构造方法
         * @Param: []
         * @Return:
         * @throws: IOException, ParseException
         */
        this.departments = new LinkedList<Department>();
        this.addDepartment(new Logistics());
        this.addDepartment(new Human());
        simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");

        this.allEmployee = new HashSet<Staff>();
        this.allInfo = new ArrayList<ClockInfo>();
        
        init();
    }

    public void history(){
        /*
         * 功能描述: 展示打卡信息，所有历史记录
         * @Param: [void]
         * @Return: void
         */
        System.out.println("历史记录");
        //遍历所有员工
        for(Staff staff : allEmployee){
            //取得个人的打卡信息集合
            ArrayList<ClockInfo> arrayList = this.onlyEmployee(staff.getId());
            if(arrayList != null){
                System.out.println(staff.getName()+","+staff.getId());
                for(ClockInfo clockInfo : arrayList){
                    System.out.println(" "+ clockInfo);
                }
            }
        }
    }

    public int signBack(String id) throws FileNotFoundException {
        /*
         * 功能描述: 签退，可以重复签退，但两次间隔不能小于一分钟
         * @Param: [id]:员工ID号
         * @Return: int
         * @throws: FileNotFoundException
         */
        Date date = new Date();
        ArrayList<ClockInfo> onePersonInfos = this.onlyEmployee(id);

        if(onePersonInfos == null){
            System.err.println("卡号："+id+",今天还没有签到，无法签退!");
            return 0;
        }else {
            for(ClockInfo d : onePersonInfos){
                if(d.getIn().getDate()==new Date().getDate() && d.getIn().getMonth() == new Date().getMonth()
                        && d.getIn().getYear()== new Date().getYear()){
                    if(d.getBack() == null){
                        d.setBack();//调用签退方法
                        System.out.println("卡号："+id+",签退成功! "+d.getBack());
                        if ((d.getBack().getHours()*60 + d.getBack().getMinutes()) >= ini_backTime ) {
                            this.transmit(d);
                            return -1;
                        }else {
                            int minutes = ini_backTime - (d.getBack().getHours()*60 + d.getBack().getMinutes());
                            System.out.println("您已早退" + minutes/60 + "小时" + minutes%60 + "分钟");
                            return minutes;
                        }
                    }else {
                        if(new Date().getTime()-d.getBack().getTime()>60000){
                            //判断两次打卡时间间隔是否小于一分钟
                            d.setBack();//调用签退方法
                            System.out.println("卡号："+id+",签退成功! "+d.getBack());
                            if ((d.getBack().getHours()*60 + d.getBack().getMinutes()) >= ini_backTime){
                                this.transmit(d);
                                return -1;
                            }else {
                                int minutes = ini_backTime - (d.getBack().getHours()*60 + d.getBack().getMinutes());
                                System.out.println("您已早退" + minutes/60 + "小时" + minutes%60 + "分钟");
                                return minutes;
                            }
                        }else {
                            System.out.println("与上次打卡时间小于一分钟，请不要打卡过于频繁!");
                            return -2;
                        }
                    }
                }
            }
            System.err.println("卡号："+id+",今天还没有签到，无法签退!");
            return 0;
        }
    }

    public int signIn(String id) throws FileNotFoundException {
        /*
         * 功能描述: 签到，不可重复
         * @Param: [id]员工ID
         * @Return: int
         * @throws: FileNotFoundException
         */
        ClockInfo clockInfo = new ClockInfo(id);

        ArrayList<ClockInfo> onePersonInfos = this.onlyEmployee(id);
        if (onePersonInfos == null) {
            clockInfo.setIn();//调用签到方法
            allInfo.add(clockInfo);//打卡信息加入信息集合
//                search(id).addCount();
            System.out.println("卡号："+id+",打卡成功!"+ clockInfo.getIn());
            if((clockInfo.getIn().getHours()*60 + clockInfo.getIn().getMinutes()) <= ini_inTime){
                transmit(clockInfo);
                return -1;
            }else{
                int minutes = clockInfo.getIn().getHours()*60 + clockInfo.getIn().getMinutes() - ini_inTime;
                System.out.println("您已迟到" + minutes/60 + "小时" + minutes%60 + "分钟");
                return minutes;
            }
        }else{
            for(ClockInfo d : onePersonInfos){
                if(d.getIn().getDate()==new Date().getDate() && d.getIn().getMonth() == new Date().getMonth()
                        && d.getIn().getYear()== new Date().getYear()){
                    System.err.println("今天已经打过卡了");
                    //不能重复签到
                    return -2;
                }
            }
            clockInfo.setIn();//调用签到方法
            allInfo.add(clockInfo);//打卡信息加入信息集合
//            search(id).addCount();
            System.out.println("卡号：" + id + ",打卡成功!" + clockInfo.getIn());
            if((clockInfo.getIn().getHours()*60 + clockInfo.getIn().getMinutes()) <= ini_inTime){
                transmit(clockInfo);
                return -1;
            }else{
                int minutes = clockInfo.getIn().getHours()*60 + clockInfo.getIn().getMinutes() - ini_inTime;
                System.out.println("您已迟到" + minutes/60 + "小时" + minutes%60 + "分钟");
                return minutes;
            }
        }
    }

    public boolean logIn(String id,String password){
        /*
         * 功能描述: 员工登录方法
         * @Param: [id, password]
         * @Return: boolean
         */
        if(search(id)==null){
            System.err.println("ID错误,该员工不存在!");
            return false;
        }else {
            if(search(id).getPassword().equals(password)){
                System.out.println("登录成功!");
                return true;
            }else {
                System.out.println("密码错误,登陆失败");
                return false;
            }
        }
    }


    public ArrayList<ClockInfo> onlyEmployee(String id){
        /*
         * 功能描述: 从公司所有打卡信息中取得一个员工的所有打卡信息
         * @Param: [id]员工ID
         * @Return: java.util.ArrayList<ClockInfo>
         */
        int flag = 0;
        ArrayList<ClockInfo> clockInfos = new ArrayList<ClockInfo>();
        //遍历所有员工
        for(ClockInfo clockInfo :this.allInfo){
            if(clockInfo.getId().equals(id)){
                clockInfos.add(clockInfo);
                flag = 1;
            }
        }
        if(flag == 1){
            return clockInfos;
        }else{
            return null;
        }
    }

    public void addEmployee(Staff staff) {
        /*
         * 功能描述: 添加员工，加入公司员工集合
         * @Param: [employee]
         * @Return: void
         */
        this.allEmployee.add(staff);
    }
    
    public void addClockInfo(ClockInfo clockInfo) {
        /*
         * 功能描述: 将clockInfo加入到ClockInfo集合中
         * @Param: [clockInfo]
         * @Return: void
         * @Date: 2020/7/10
         */
    	this.allInfo.add(clockInfo);
    }

    public void writeEmployee(Staff staff) throws FileNotFoundException {
        /*
         * 功能描述: 添加员工，写入储存员工的文件
         * @Param: [employee]
         * @Return: void
         * @throws: FileNotFoundException
         */
        PrintWriter printWriter = new PrintWriter(
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/employees.dat",true))));
        printWriter.println(staff.getId()+"_"+staff.getName()+"_"+staff.getPassword()+"_"+staff.getRoot());//以"_"分隔
        printWriter.close();
    }

    public void removeEmployee(Staff staff){
        /*
         * 功能描述: 从公司删除员工，从员工集合中的删除
         * @Param: [employee]
         * @Return: void
         */
        this.allEmployee.remove(staff);
    }
    
    public void removeClockInfo(ClockInfo clockInfo) {
        /*
         * 功能描述: 从ClockInfo集合中移除clockInfo
         * @Param: [clockInfo]
         * @Return: void
         * @Date: 2020/7/10
         */
    	this.allInfo.remove(clockInfo);
    }
    
    public void deleteClockInfo(ClockInfo clockInfo) throws IOException, ParseException {
        /*
         * 功能描述: 从文件中删除指定clockInfo
         * @Param: [clockInfo]
         * @Return: void
         * @Date: 2020/7/10
         */
    	BufferedReader bufferedReader = new BufferedReader(
    			new InputStreamReader(new FileInputStream("data/signInfo.dat")));
        //临时文件
    	PrintWriter printWriter = new PrintWriter(
    			new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/ctmp.dat"))));
    	String s = bufferedReader.readLine();
    	while(s != null) {
    		StringTokenizer stringTokenizer = new StringTokenizer(s,"_");
    		stringTokenizer.nextToken();
    		stringTokenizer.nextToken();
    		Date date = simpleDateFormat.parse(stringTokenizer.nextToken());
    		if (s.startsWith(clockInfo.getId())&&date.getYear()==clockInfo.getIn().getYear()&&
    				date.getMonth()==clockInfo.getIn().getMonth()&&date.getDate()==clockInfo.getIn().getDate()){
                //如果这行信息是要删除的内容，则continue,不写入临时文件
            	s = bufferedReader.readLine();
                continue;
            }
    		printWriter.println(s);
            s = bufferedReader.readLine();
    	}
        //关闭文件
    	printWriter.close();
        bufferedReader.close();
        
        File file = new File("data/signInfo.dat");
        if(!file.delete()){
            System.out.println("删除失败!");
        }
        File from = new File("data/ctmp.dat");
        File to = new File("data/signInfo.dat");
        if (from.renameTo(to)) {
            System.out.println("成功!");
        } else {
            System.out.println("失败!");
        }
    }

    public void deleteEmployee(Staff staff) throws IOException {
        /*
         * 功能描述: 从公司删除员工，删除储存员工文件中的信息
         * @Param: [employee]
         * @Return: void
         * @throws: IOException
         */
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream("data/employees.dat")));
        //临时文件
        PrintWriter printWriter = new PrintWriter(
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/tmp.dat"))));

        //将文件内容除了要删除的部分写入一个临时文件,以达到删除特定内容的目的
        String s = bufferedReader.readLine();
        while (s != null){
            if (s.startsWith(staff.getId())){
                //如果这行信息是要删除的内容，则continue,不写入临时文件
            	s = bufferedReader.readLine();
                continue;
            }
            printWriter.println(s);
            s = bufferedReader.readLine();
        }
        //关闭文件
        printWriter.close();
        bufferedReader.close();

        //删除原文件
        File file = new File("data/employees.dat");
        if(!file.delete()){
            System.out.println("删除失败!");
        }

        //将临时文件重命名为原来文件的名字
        File from = new File("data/tmp.dat");
        File to = new File("data/employees.dat");
        //给出提示
        if (from.renameTo(to)) {
            System.out.println("成功!");
        } else {
            System.out.println("失败!");
        }
    }

    public Staff search(String id){
        /*
         * 功能描述: 查找员工
         * @Param: [void]
         * @Return: Employee
         */
        for(Staff staff : allEmployee){
            //遍历所有员工
            if (staff.getId().equals(id))
                return staff;
        }
        return null;
    }
    
    public ClockInfo searchClockInfo(String id, String inString) {
        /*
         * 功能描述: 根据ID和日期的字符串从ClockInfo集合中查找ClockInfo
         * @Param: [id, inString]
         * @Return: entity.ClockInfo
         * @Date: 2020/7/10
         */
    	for(ClockInfo clockInfo : this.allInfo) {
    		if (clockInfo.getId().equals(id)&&clockInfo.getIn().toString().equals(inString)) {
				return clockInfo;
			}
    	}
    	return null;
    }
    
    public ClockInfo searchClockInfo(String id, Date date) {
        /*
         * 功能描述: 根据ID和日期从ClockInfo集合中查找ClockInfo
         * @Param: [id, inString]
         * @Return: entity.ClockInfo
         * @Date: 2020/7/10
         */
    	for(ClockInfo clockInfo : this.allInfo) {
    		if (clockInfo.getId().equals(id)&&clockInfo.getIn().getYear()==date.getYear()&&
    				clockInfo.getIn().getMonth()==date.getMonth()&&clockInfo.getIn().getDate()==date.getDate()) {
				return clockInfo;
			}
    	}
    	return null;
    }

    public void transmit(ClockInfo clockInfo){
        /*
         * 功能描述: 给部门传递消息
         * @Param: [clockInfo]
         * @Return: void
         */
        for(Department department : this.departments){
            department.addClockInfo(clockInfo);
        }
    }

    public void addDepartment(Department department){
        /*
         * 功能描述: 添加部门
         * @Param: [department]
         * @Return: void
         */
        this.departments.add(department);
    }

    public void init() throws IOException, ParseException {
        /*
         * 功能描述: 公司初始化，设置公司最迟签到时间和最早签退时间，将员工信息从文件录入
         * @Param: [void]
         * @Return: void
         * @throws: IOException, ParseException
         */
//        System.out.println("设置公司最迟签到时间,格式为: hh:mm");
//        String s = stdIn.readLine();
//        StringTokenizer sTokenizer = new StringTokenizer(s,":");
//        this.ini_inTime = Integer.parseInt(sTokenizer.nextToken())*60 + Integer.parseInt(sTokenizer.nextToken());
//
//        System.out.println("设置公司最早签退时间,格式为: hh:mm");
//        s = stdIn.readLine();
//        sTokenizer = new StringTokenizer(s, ":");
//        this.ini_backTime = Integer.parseInt(sTokenizer.nextToken())*60 + Integer.parseInt(sTokenizer.nextToken());

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream("data/employees.dat")));
        String line = bufferedReader.readLine();

        //从文件读入公司员工信息
        while (line != null){
            StringTokenizer stringTokenizer = new StringTokenizer(line, "_");
            if (stringTokenizer.countTokens() == 4) {
                this.addEmployee(
                        EmployeeFactory.create(1, stringTokenizer.nextToken(), stringTokenizer.nextToken(),
                        		stringTokenizer.nextToken(),Integer.parseInt(stringTokenizer.nextToken())));
                line = bufferedReader.readLine();
            }else {
                System.err.println("初始化失败,数据格式错误!");
                break;
            }
        }

        BufferedReader bfReader = new BufferedReader(
                new InputStreamReader(new FileInputStream("data/signInfo.dat")));
        //从文件读入公司打卡信息
        String in = bfReader.readLine();
        while(in != null){
            StringTokenizer stringTokenizer = new StringTokenizer(in,"_");
            if(stringTokenizer.countTokens()==3){
            	String id = stringTokenizer.nextToken();
                ClockInfo clockInfo = new ClockInfo(id);
                String which = stringTokenizer.nextToken();
                String time = stringTokenizer.nextToken();
                Date date = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss").parse(time);
                //字符串解析为Date
                if(which.equals("签到")){
                	int flag = 0;
                	for(ClockInfo c : this.allInfo) {
                		if(c.getIn().getYear() == date.getYear()&&c.getIn().getMonth()==date.getMonth()&&
                                c.getIn().getDate()==date.getDate()&&c.getId().equals(id)) {
                			c.setInTime(time);
                			flag=1;
                			break;
                		}
                	}
                	if(flag == 0) {
                		clockInfo.setInTime(time);
                		this.allInfo.add(clockInfo);
                	}
                }else if(which.equals("签退")){
                    
                    for(ClockInfo c : this.allInfo){
                        if(c.getIn().getYear() == date.getYear()&&c.getIn().getMonth()==date.getMonth()&&
                                c.getIn().getDate()==date.getDate()){
                            c.setBackTime(time);
                            //由于签退可以多次,所以签退信息可以覆盖
                        }
                    }
                }
            }else {
                System.out.println("初始化失败,数据格式错误!");
            }
            in = bfReader.readLine();
        }

        bufferedReader.close();//关闭文件
        bfReader.close();
    }
    private synchronized static void ini() throws IOException, ParseException {
        /*
         * 功能描述: synchronized同步,保证线程安全
         * @Param: []
         * @Return: void
         */
        if(company == null){
            company = new CompanyGUI();
        }
    }
    
    public ArrayList<ClockInfo> getSAllInfo(){
        /*
         * 功能描述: 返回公司的打卡信息集合
         * @Param: []
         * @Return: java.util.ArrayList<entity.ClockInfo>
         * @Date: 2020/7/10
         */
    	return this.allInfo;
    }
    
    public HashSet<Staff> getStaffs(){
        /*
         * 功能描述: 返回公司的员工集合
         * @Param: []
         * @Return: java.util.HashSet<service.Staff>
         * @Date: 2020/7/10
         */
    	return this.allEmployee;
    }
    
}
