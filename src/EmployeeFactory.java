/**
 * @author: yqw
 * @date: 2020/7/6
 * @description: 工厂模式类
 */
public class EmployeeFactory {

    public static Employee create(int which, String id, String name,String password){
        if(which == 1){
            return new Employee(id,name,password);
        }
        return null;
    }

    public static Employee create(int which, String id, String name){
        if(which == 1){
            return new Employee(id,name);
        }
        return null;
    }
}
