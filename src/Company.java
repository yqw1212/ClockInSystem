import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: yqw
 * @date: 2020/7/2
 * @description: 容器类
 */
public class Company {
    private List<Department> departments;

    private static BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    private HashSet<Staff> allEmployee;
    private ArrayList<ClockInfo> allInfo;
    private int ini_inTime, ini_backTime;
    public static Company company = null;


    public static Company getInstance() throws IOException, ParseException {
        /*
         * 功能描述: 单例模式
         * @Param: []
         * @Return: Company
         */
        if (company == null){
            company = new Company();
        }
        return company;
    }

    private Company() throws IOException, ParseException {
        this.departments = new LinkedList<Department>();
        this.addDepartment(new Logistics());
        this.addDepartment(new Human());

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

    public void personalSignInfo(String id){
        /*
         * 功能描述: 展示个人打卡信息
         * @Param: [id]员工ID号
         * @Return: void
         */
        ArrayList<ClockInfo> arrayList = this.onlyEmployee(id);
        //取得个人打卡信息集合
        if (arrayList == null){
            System.err.println("无此ID员工");
        }else {
            Staff staff = search(id);
            System.out.println(staff.getName()+","+staff.getId());
            for(ClockInfo clockInfo : arrayList){
                System.out.println(" "+ clockInfo);
            }
        }
    }

    public void showSighInfo(){
        /*
         * 功能描述: 展示签到信息，今日记录
         * @Param: [void]
         * @Return: void
         */
        System.out.println("今日情况");
        for(Staff staff : allEmployee){
            int flag = 0;
            for(ClockInfo d : allInfo){
                if(d.getId().equals(staff.getId()) && d.getIn().getDate()==new Date().getDate() &&
                        d.getIn().getMonth() == new Date().getMonth() && d.getIn().getYear()== new Date().getYear()){
                    System.out.print(staff.getName()+"--签到:"+d.getIn());
                    if(d.getBack()==null){
                        System.out.println(",签退:未完成");
                    }else {
                        System.out.println(",签退:"+d.getBack());
                    }
                    flag = 1;
                    //今天签过到,flag赋值为1
                    break;
                }
            }
            if (flag == 1){
                continue;
            }
            System.out.println(staff.getName()+"--签到:未完成,签退:未完成");
        }
    }

    public void signBack(String id) throws FileNotFoundException {
        /*
         * 功能描述: 签退，可以重复签退，但两次间隔不能小于一分钟
         * @Param: [id]:员工ID号
         * @Return: void
         * @throws: FileNotFoundException
         */
        Date date = new Date();
        ArrayList<ClockInfo> onePersonInfos = this.onlyEmployee(id);

        if(onePersonInfos == null){
            if(search(id)==null){
                System.err.println("无此ID员工");
            }else{
                System.err.println("卡号："+id+",今天还没有签到，无法签退!");
            }
        }else {
            for(ClockInfo d : onePersonInfos){
                if(d.getIn().getDate()==new Date().getDate() && d.getIn().getMonth() == new Date().getMonth()
                        && d.getIn().getYear()== new Date().getYear()){
                    if(d.getBack() == null){
                        d.setBack();//调用签退方法
                        if (Integer.parseInt(d.getBack().getHours()+""+d.getBack().getMinutes()) >= ini_backTime &&
                                Integer.parseInt(d.getIn().getHours()+""+d.getIn().getMinutes()) <= ini_inTime) {
                            this.transmit(d);
                        }
                        System.out.println("卡号："+id+",签退成功! "+d.getBack());
                    }else {
                        if(new Date().getTime()-d.getBack().getTime()>60000){
                            //判断两次打卡时间间隔是否小于一分钟
                            d.setBack();//调用签退方法
                            if (Integer.parseInt(d.getBack().getHours()+""+d.getBack().getMinutes()) >= ini_backTime &&
                                    Integer.parseInt(d.getIn().getHours()+""+d.getIn().getMinutes()) <= ini_inTime) {
                                this.transmit(d);
                            }
                            System.out.println("卡号："+id+",签退成功! "+d.getBack());
                        }else {
                            System.out.println("与上次打卡时间小于一分钟，请不要打卡过于频繁!");
                        }
                    }
                    return ;
                }
            }
            System.err.println("卡号："+id+",今天还没有签到，无法签退!");
        }
    }

    public void signIn(String id) throws FileNotFoundException {
        /*
         * 功能描述: 签到，不可重复
         * @Param: [id]员工ID
         * @Return: void
         * @throws: FileNotFoundException
         */
        ClockInfo clockInfo = new ClockInfo(id);

        ArrayList<ClockInfo> onePersonInfos = this.onlyEmployee(id);
        if (onePersonInfos==null) {
            if(search(id) != null) {
                clockInfo.setIn();//调用签到方法
                allInfo.add(clockInfo);//打卡信息加入信息集合
//                search(id).addCount();
                System.out.println("卡号："+id+",打卡成功!"+ clockInfo.getIn());
            }else {
                System.err.println("无此ID员工");
            }
        }else{
            for(ClockInfo d : onePersonInfos){
                if(d.getIn().getDate()==new Date().getDate() && d.getIn().getMonth() == new Date().getMonth()
                        && d.getIn().getYear()== new Date().getYear()){
                    System.err.println("今天已经打过卡了");
                    //不能重复签到
                    return ;
                }
            }
            clockInfo.setIn();//调用签到方法
            allInfo.add(clockInfo);//打卡信息加入信息集合
//            search(id).addCount();
            System.out.println("卡号："+id+",打卡成功!"+ clockInfo.getIn());
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

    public void writeEmployee(Staff staff) throws FileNotFoundException {
        /*
         * 功能描述: 添加员工，写入储存员工的文件
         * @Param: [employee]
         * @Return: void
         * @throws: FileNotFoundException
         */
        PrintWriter printWriter = new PrintWriter(
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/employees.dat",true))));
        printWriter.println(staff.getId()+"_"+staff.getName()+"_"+staff.getPassword());//以"_"分隔
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
                //如果这行信息是要删除的内容，则break,不写入临时文件
                break;
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

    public void showAllEmployee(){
        /*
         * 功能描述: 展示公司所有员工信息
         * @Param: [void]
         * @Return: void
         */
        for(Staff staff : allEmployee) {
            //遍历所有员工
            System.out.println(staff);
        }
    }

    public void showDepartmentRecord(){
        for(Department department : this.departments){
            department.showClockInfo();
        }
    }

    public void transmit(ClockInfo clockInfo){
        for(Department department : this.departments){
            department.addClockInfo(clockInfo);
        }
    }

    public void addDepartment(Department department){
        this.departments.add(department);
    }

    public void init() throws IOException, ParseException {
        /*
         * 功能描述: 公司初始化，设置公司最迟签到时间和最早签退时间，将员工信息从文件录入
         * @Param: [void]
         * @Return: void
         * @throws: IOException, ParseException
         */
        System.out.println("设置公司最迟签到时间,格式为: hh:mm");
        String s = stdIn.readLine();
        s = s.replace(":","");
        this.ini_inTime = Integer.parseInt(s);

        System.out.println("设置公司最早签退时间,格式为: hh:mm");
        s = stdIn.readLine();
        s = s.replace(":","");
        this.ini_backTime = Integer.parseInt(s);

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream("data/employees.dat")));
        String line = bufferedReader.readLine();

        //从文件读入公司员工信息
        while (line != null){
            StringTokenizer stringTokenizer = new StringTokenizer(line, "_");
            if (stringTokenizer.countTokens()==3) {
                this.addEmployee(
                        EmployeeFactory.create(1, stringTokenizer.nextToken(), stringTokenizer.nextToken(), stringTokenizer.nextToken()));
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
                ClockInfo clockInfo = new ClockInfo(stringTokenizer.nextToken());
                String which = stringTokenizer.nextToken();
                String time = stringTokenizer.nextToken();

                if(which.equals("签到")){
                    clockInfo.setInTime(time);
                    this.allInfo.add(clockInfo);
                }else if(which.equals("签退")){
                    Date date = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss").parse(time);
                    //字符串解析为Date
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
//        this.addEmployee(new Employee("A001","小王"));
//        this.addEmployee(new Employee("B001","小森"));
//        this.addEmployee(new Employee("C001","小明"));
//        this.addEmployee(new Employee("D001","小刚"));
//        this.addEmployee(new Employee("E001","冰冰"));
        bufferedReader.close();//关闭文件
    }
}
