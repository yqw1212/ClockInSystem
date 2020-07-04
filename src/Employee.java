/**
 * @author: yqw
 * @date: 2020/7/2
 * @description: 员工的实体类
 */
public class Employee {
    private String id;
    private String name;
    private String password;
    private int count;

    Employee(String id, String name){
        setId(id);
        setName(name);
//        this.count = 0;
        this.password = "123456";
    }
    Employee(String id, String name,String password){
        setId(id);
        setName(name);
        setPassword(password);
    }

    public void setPassword(String password) {
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
    public void addCount(){
        this.count++;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String toString() {
        return "id:" + id  + ", name:" + name + ",累计签到:" + count +"次";
    }
}
