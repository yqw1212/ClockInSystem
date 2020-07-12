package service;
/**
 * @author: yqw
 * @date: 2020/7/6
 * @description: 员工接口
 */
public interface Staff {

    public String getName();

    public void setName(String name);

    public String getId();

    public void setId(String id);

    public String getPassword();

    public void setPassword(String password);
    
    public int getRoot();
    
    public void setRoot(int i);

    public String toString();

}
