import java.io.*;
import java.text.ParseException;
import java.util.StringTokenizer;

/**
 * @author: yqw
 * @date: 2020/7/2
 * @description: 主方法类
 */
public class Main {
    private static BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    private static PrintWriter stdOut = new PrintWriter(System.out,true);
    private static PrintWriter stdErr = new PrintWriter(System.err,true);
    private Company company;

    public static void main(String[] args) {
        try{
            Main main = new Main();
        }catch (IOException e){
            stdErr.println("读入错误");
        }catch (InterruptedException e){
            stdErr.println("Time wrong");
        }catch (ParseException e){
            stdErr.println("日期转换错误");
        }
    }

    Main() throws IOException, InterruptedException, ParseException {
        this.company = Company.getInstance();
        run();
    }

    public void run() throws IOException, InterruptedException {
        /*
         * 功能描述: 主运行方法
         * @Param: []
         * @Return: void
         */
        int choice = getChoice();
        while (choice!= 0) {
            if(choice == 1){
                System.out.println("请输入员工ID:");
                String id = stdIn.readLine();
                System.out.println("请输入密码:");
                String password = stdIn.readLine();
                //先登录
                if(company.logIn(id,password)){
                    company.signIn(id);//调用签到方法
                }else {
                    System.out.println("签到失败!");
                }
            }else if(choice == 2){
                System.out.println("请输入员工ID:");
                String id = stdIn.readLine();
                System.out.println("请输入密码:");
                String password = stdIn.readLine();
                //先登录
                if (company.logIn(id,password)) {
                    company.signBack(id);//调用签退方法
                }else {
                    System.out.println("签退失败!");
                }
            }else if(choice == 3){
                stdErr.println("----查看打卡信息----");
                stdErr.println("[1]--------------今日记录");
                stdErr.println("[2]--------------历史记录");
                stdErr.println("[3]--------------个人记录");
                int i = Integer.parseInt(stdIn.readLine());
                switch (i){
                    case 1:{
                        company.showSighInfo();
                        break;
                    }
                    case 2:{
                        company.history();
                        break;
                    }
                    case 3:{
                        System.out.println("请输入ID:");
                        String id = stdIn.readLine();
                        company.personalSignInfo(id);
                        break;
                    }
                    default:
                        break;
                }

            }else if(choice == 4){
                company.showAllEmployee();
            }else if(choice == 5){
                System.out.println("请输入员工ID和姓名,例如:B666_轩轩");
                String s = stdIn.readLine();
                StringTokenizer stringTokenizer = new StringTokenizer(s,"_");
                if(stringTokenizer.countTokens() == 2) {
                    String id = stringTokenizer.nextToken();
                    if(company.search(id) != null){
                        System.err.println("该ID已存在,注册员工失败!");
                    }else {
                        company.addEmployee(EmployeeFactory.create(1, id, stringTokenizer.nextToken()));
                        company.writeEmployee(company.search(id));
                        System.out.println("注册成功,初始密码为123456,为了账户的安全请您尽快修改密码!");
                    }
                }else {
                    System.err.println("输入格式错误,注册失败!");
                }
            }else if(choice == 6){
                System.out.println("请输入要删除的员工ID");
                String id = stdIn.readLine();
                company.deleteEmployee(company.search(id));//顺序不可改变
                company.removeEmployee(company.search(id));//顺序不可改变
            }else if(choice == 7){
                System.out.println("请输入员工ID:");
                String id = stdIn.readLine();
                System.out.println("请输入密码:");
                String password = stdIn.readLine();
                //先登录
                if (company.logIn(id,password)){
                    System.out.println("请输入新密码:");
                    String code = stdIn.readLine();
                    Staff staff = company.search(id);
                    company.deleteEmployee(staff);//删除旧员工信息
                    staff.setPassword(code);
                    company.writeEmployee(staff);//添加新员工信息
                    System.out.println("密码修改成功!");
                }else {
                    System.out.println("密码修改失败!");
                }
            }else if(choice == 8){
                this.company.showDepartmentRecord();
            }

            Thread.sleep(300);
            choice = getChoice();
        }
    }

    public int getChoice() throws IOException {
        /*
         * 功能描述: 选择要执行的功能
         * @Param: [void]
         * @Return: int
         */
        int in;
        while (true) {
            stdErr.println("----员工打卡系统----");
            stdErr.println("[0]-----------退出");
            stdErr.println("[1]-----------签到");
            stdErr.println("[2]-----------签退");
            stdErr.println("[3]-----------查看打卡信息");
            stdErr.println("[4]-----------展示所有员工");
            stdErr.println("[5]-----------注册员工");
            stdErr.println("[6]-----------删除员工");
            stdErr.println("[7]-----------修改密码");
            stdErr.println("[8]-----------后勤人事记录");
            in = Integer.parseInt(stdIn.readLine());
            if (in >= 0 && in <= 8) {
                break;
            } else {
                stdErr.println("Invalid choice: " + in);
            }
        }
        return in;
    }
}
