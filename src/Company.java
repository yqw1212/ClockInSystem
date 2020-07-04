import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * @author: yqw
 * @date: 2020/7/2
 * @description: 容器类
 */
public class Company {
    private static BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    private HashSet<Employee> allEmployee;
    private ArrayList<ClockInfo> allInfo;
    private int in_hour, in_minute;
    private int back_hour, back_minute;

    Company() throws IOException {
        this.allEmployee = new HashSet<Employee>();
        this.allInfo = new ArrayList<ClockInfo>();
        init();
    }

    public void showSighInfo(){
        /*
         * 功能描述: 展示所有签到信息，包括历史记录和今日记录
         * @Param: [void]
         * @Return: void
         */
        System.out.println("历史记录");
        for(Employee employee : allEmployee){
            ArrayList<ClockInfo> arrayList = this.onlyEmployee(employee.getId());
                if(arrayList != null){
                    System.out.println(employee.getName()+","+employee.getId());
                    for(ClockInfo clockInfo : arrayList){
                        System.out.println(" "+ clockInfo);
                }
            }
        }
        System.out.println("今日情况");
        for(Employee employee : allEmployee){
            int flag = 0;
            for(ClockInfo d : allInfo){
                if(d.getId().equals(employee.getId()) && d.getIn().getDay()==new Date().getDay() &&
                        d.getIn().getMonth() == new Date().getMonth() && d.getIn().getYear()== new Date().getYear()){
                    System.out.print(employee.getName()+"--签到:"+d.getIn());
                    if(d.getBack()==null){
                        System.out.println(",签退:未完成");
                    }else {
                        System.out.println(",签退:"+d.getBack());
                    }
                    flag = 1;
                    break;
                }
            }
            if (flag == 1){
                continue;
            }
            System.out.println(employee.getName()+"--签到:未完成,签退:未完成");
        }
    }

    public void signBack(String id) throws FileNotFoundException {
        /*
         * 功能描述: 签退，可以重复签退，但两次间隔不能小于一分钟
         * @Param: [id]:员工ID号
         * @Return: void
         */
        ArrayList<ClockInfo> onePersonInfos = this.onlyEmployee(id);
        if(onePersonInfos == null){
            if(search(id)==null){
                System.err.println("无此ID员工");
            }else{
                System.err.println("卡号："+id+",今天还没有签到，无法签退!");
            }
        }else {
            for(ClockInfo d : onePersonInfos){
                if(d.getIn().getDay()==new Date().getDay() && d.getIn().getMonth() == new Date().getMonth()
                        && d.getIn().getYear()== new Date().getYear()){
                    if(d.getBack() == null){
                        d.setBack();
                        System.out.println("卡号："+id+",签退成功! "+d.getBack());
                        return ;
                    }else {
                        if(new Date().getTime()-d.getBack().getTime()>60000){
                            d.setBack();
                            System.out.println("卡号："+id+",签退成功! "+d.getBack());
                        }else {
                            System.out.println("与上次打卡时间小于一分钟，请不要打卡过于频繁!");
                        }
                        return ;
                    }
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
         */
        ClockInfo clockInfo = new ClockInfo(id);

        ArrayList<ClockInfo> onePersonInfos = this.onlyEmployee(id);
        if (onePersonInfos==null) {
            if(search(id) != null) {
                clockInfo.setIn();
                allInfo.add(clockInfo);
                search(id).addCount();
                System.out.println("卡号："+id+",打卡成功!"+ clockInfo.getIn());
            }else {
                System.err.println("无此ID员工");
            }
        }else{
            for(ClockInfo d : onePersonInfos){
                if(d.getIn().getDay()==new Date().getDay() && d.getIn().getMonth() == new Date().getMonth()
                        && d.getIn().getYear()== new Date().getYear()){
                    System.err.println("今天已经打过卡了");
                    return ;
                }
            }
            clockInfo.setIn();
            allInfo.add(clockInfo);
            search(id).addCount();
            System.out.println("卡号："+id+",打卡成功!"+ clockInfo.getIn());
        }
    }

    public boolean logIn(String id,String password){
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

    public void addEmployee(Employee employee) {
        /*
         * 功能描述: 添加员工，加入公司员工集合
         * @Param: [employee]
         * @Return: void
         */
        this.allEmployee.add(employee);
    }

    public void writeEmployee(Employee employee) throws FileNotFoundException {
        /*
         * 功能描述: 添加员工，写入储存员工的文件
         * @Param: [employee]
         * @Return: void
         */
        PrintWriter printWriter = new PrintWriter(
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/employees.dat",true))));
        printWriter.println(employee.getId()+"_"+employee.getName()+"_"+employee.getPassword());
        printWriter.close();
    }

    public void removeEmployee(Employee employee){
        /*
         * 功能描述: 从公司删除员工，从员工集合中的删除
         * @Param: [employee]
         * @Return: void
         */
        this.allEmployee.remove(employee);
    }

    public void deleteEmployee(Employee employee) throws IOException {
        /*
         * 功能描述: 从公司删除员工，删除储存员工文件中的信息
         * @Param: [employee]
         * @Return: void
         */
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream("data/employees.dat")));
        PrintWriter printWriter = new PrintWriter(
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/tmp.dat"))));

        String s = bufferedReader.readLine();
        while (s != null){
            if (s.startsWith(employee.getId())){
                break;
            }
            printWriter.println(s);
            s = bufferedReader.readLine();
        }
        printWriter.close();
        bufferedReader.close();

        File file = new File("data/employees.dat");

        if(!file.delete()){
            System.out.println("删除失败!");
        }

        File from = new File("data/tmp.dat");
        File to = new File("data/employees.dat");
        if (from.renameTo(to)) {
            System.out.println("删除成功!");
        } else {
            System.out.println("删除失败!");
        }
    }

    public Employee search(String id){
        /*
         * 功能描述: 查找员工
         * @Param: [void]
         * @Return: Employee
         */
        for(Employee employee : allEmployee){
            if (employee.getId().equals(id))
                return employee;
        }
        return null;
    }

    public void showAllEmployee(){
        /*
         * 功能描述: 展示公司所有员工信息
         * @Param: [void]
         * @Return: void
         */
        for(Employee employee : allEmployee) {
            System.out.println(employee);
        }
    }
    public void init() throws IOException {
        /*
         * 功能描述: 公司初始化，设置公司最迟签到时间和最早签退时间，将员工信息从文件录入
         * @Param: [void]
         * @Return: void
         */
//        System.out.println("设置公司最迟签到时间,格式为: hour:minute");
//        String s = stdIn.readLine();
//        StringTokenizer sTokenizer = new StringTokenizer(s, ":");
//        this.in_hour = Integer.parseInt(sTokenizer.nextToken());
//        this.in_minute = Integer.parseInt(sTokenizer.nextToken());
//
//        System.out.println("设置公司最早签退时间,格式为: hour:minute");
//        s = stdIn.readLine();
//        sTokenizer = new StringTokenizer(s, ":");
//        this.back_hour = Integer.parseInt(sTokenizer.nextToken());
//        this.back_minute = Integer.parseInt(sTokenizer.nextToken());

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream("data/employees.dat")));
        String line = bufferedReader.readLine();

        while (line != null){
            StringTokenizer stringTokenizer = new StringTokenizer(line, "_");
            if (stringTokenizer.countTokens()==3) {
                this.addEmployee(
                        new Employee(stringTokenizer.nextToken(), stringTokenizer.nextToken(),stringTokenizer.nextToken()));
                line = bufferedReader.readLine();
            }else {
                System.err.println("初始化失败,数据格式错误");
                break;
            }
        }
//        this.addEmployee(new Employee("A001","小王"));
//        this.addEmployee(new Employee("B001","小森"));
//        this.addEmployee(new Employee("C001","小明"));
//        this.addEmployee(new Employee("D001","小刚"));
//        this.addEmployee(new Employee("E001","冰冰"));
        System.out.println("初始化成功");
        bufferedReader.close();
    }
}
