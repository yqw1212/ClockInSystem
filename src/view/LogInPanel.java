package view;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import entity.CompanyGUI;
import utils.StringUtil;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

import java.awt.event.ActionEvent;
/**
 * @author: yqw
 * @date: 2020/7/9
 * @description: 登录面板
 */
public class LogInPanel extends JPanel {
	private CompanyGUI company;
	
	private JTextField IDTextField;
	private JPasswordField pwdTextField;
	private JButton btnNewButton;
	private JButton btnNewButton_1;

	/**
	 * Create the panel.
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public LogInPanel() throws IOException, ParseException {
		company = CompanyGUI.getInstance();
		
		JLabel lblNewLabel = new JLabel("ID");
		
		JLabel lblNewLabel_1 = new JLabel("密码");
		
		IDTextField = new JTextField();
		IDTextField.setColumns(10);
		
		pwdTextField = new JPasswordField();
		pwdTextField.setColumns(10);
		
		btnNewButton = new JButton("登录");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 登录
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				String id = IDTextField.getText();
				String password = pwdTextField.getText();
				if(notEmpty(id, password)) {
					if(company.search(id) != null) {
						if(company.logIn(id, password)) {
							try {
								MainFrame mainFrame = new MainFrame(id);
								mainFrame.setVisible(true);
								LogInFrame logInFrame = LogInFrame.getInstance();
								logInFrame.dispose();
							} catch (IOException e1) {
								e1.printStackTrace();
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
						}else {
							JOptionPane.showMessageDialog(null, "密码不正确,登录失败!");
							resetText();
						}
					}else {
						JOptionPane.showMessageDialog(null, "无此ID员工!");
						resetText();
					}
				}
			}
		});
		
		btnNewButton_1 = new JButton("注册");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 切换到注册面板
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				try {
					LogInFrame logInFrame = LogInFrame.getInstance();
					logInFrame.setVisible(true);
					logInFrame.changeContentPane(new RegistPanel());
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
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(88)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblNewLabel)
								.addComponent(lblNewLabel_1))
							.addGap(52)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(pwdTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(IDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(100)
							.addComponent(btnNewButton)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnNewButton_1)))
					.addContainerGap(103, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(80, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(IDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(pwdTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(67)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton)
						.addComponent(btnNewButton_1))
					.addContainerGap(52, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}
	
	public void resetText() {
		/*
		 * 功能描述: 重置输入框
		 * @Param: []
		 * @Return: void
		 * @Date: 2020/7/10
		 */
		this.IDTextField.setText("");
		this.pwdTextField.setText("");
	}
	
	public boolean notEmpty(String id, String password) {
		if(StringUtil.isEmpty(id)){
			JOptionPane.showMessageDialog(null, "ID不能为空!");
			return false;
		}
		if(StringUtil.isEmpty(password)) {
			JOptionPane.showMessageDialog(null, "密码不能为空!");
			return false;
		}
		return true;
	}
	

}
