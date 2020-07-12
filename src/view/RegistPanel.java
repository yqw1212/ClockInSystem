package view;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import entity.CompanyGUI;
import entity.Employee;
import service.EmployeeFactory;
import service.Staff;
import utils.StringUtil;

import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.awt.event.ActionEvent;
/**
 * @author: yqw
 * @date: 2020/7/9
 * @description: 注册面板
 */
public class RegistPanel extends JPanel {
	private CompanyGUI company;
	private JTextField IDTextField;
	private JTextField nameTextField;

	/**
	 * Create the panel.
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public RegistPanel() throws IOException, ParseException {
		company = CompanyGUI.getInstance();
		
		JLabel lblNewLabel = new JLabel("ID");
		
		JLabel lblNewLabel_1 = new JLabel("姓名");
		
		IDTextField = new JTextField();
		IDTextField.setColumns(10);
		
		nameTextField = new JTextField();
		nameTextField.setColumns(10);
		
		JButton btnNewButton = new JButton("注册");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 注册员工
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				String id = IDTextField.getText();
				String name = nameTextField.getText();
				if(notEmpty(id, name)) {
					if(company.search(id) == null) {
						Staff staff = EmployeeFactory.create(1, id, name);
						company.addEmployee(staff);
						try {
							company.writeEmployee(staff);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
						JOptionPane.showMessageDialog(null, "注册成功,初始密码为123456,为了账户的安全请您尽快修改密码!");
						resetText();
					}else {
						JOptionPane.showMessageDialog(null, "该ID已经存在,注册失败!");
						resetText();
					}
				}
			}
		});
		
		JButton btnNewButton_1 = new JButton("返回");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 切换到登陆面板
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				try {
					LogInFrame logInFrame = LogInFrame.getInstance();
					logInFrame.setVisible(true);
					logInFrame.changeContentPane(new LogInPanel());
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(90)
							.addComponent(btnNewButton)
							.addGap(55)
							.addComponent(btnNewButton_1))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(82)
									.addComponent(lblNewLabel_1)
									.addGap(56))
								.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
									.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(lblNewLabel)
									.addGap(64)))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(IDTextField)
								.addComponent(nameTextField, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))))
					.addContainerGap(71, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(82)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(IDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(42)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton)
						.addComponent(btnNewButton_1))
					.addContainerGap(35, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}
	
	public void resetText() {
		/*
		 * 功能描述: 清空输入框
		 * @Param: []
		 * @Return: void
		 * @Date: 2020/7/10
		 */
		this.IDTextField.setText("");
		this.nameTextField.setText("");
	}
	
	public boolean notEmpty(String id, String password) {
		if(StringUtil.isEmpty(id)){
			JOptionPane.showMessageDialog(null, "ID不能为空!");
			return false;
		}
		if(StringUtil.isEmpty(password)) {
			JOptionPane.showMessageDialog(null, "姓名不能为空!");
			return false;
		}
		return true;
	}

}
