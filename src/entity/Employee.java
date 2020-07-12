package entity;

import java.util.Set;

import service.Staff;

/**
 * @author: yqw
 * @date: 2020/7/2
 * @description: 员工的实体类
 */
public class Employee implements Staff {
    private String id;
    private String name;
    private String password;//员工登陆密码
    int root;
//    private int count;

    public Employee(String id, String name){
        setId(id);
        setName(name);
//        this.count = 0;
        this.password = "123456";//新注册员工密码默认为"123456"
        this.root = 0;
    }
    public Employee(String id, String name,String password,int root){
        /*
         * 功能描述: 这个构造函数用于构造初始化时从文件读入的Employee
         * @Param: [id, name, password]
         * @Return:
         */
        setId(id);
        setName(name);
        setPassword(password);
        setRoot(root);
    }
    
    public void setRoot(int i) {
    	this.root = i;
    }
    
    public int getRoot() {
    	return this.root;
    }

    public void setPassword(String password) {
        /*
         * 功能描述: 修改密码
         * @Param: [password]
         * @Return: void
         */
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
//    public void addCount(){
//        this.count++;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }

    public String toString() {
        return "id:" + id  + ", name:" + name ;
    }

//    public String toString() {
//        return "id:" + id  + ", name:" + name + ",累计签到:" + count +"次";
//    }
}
