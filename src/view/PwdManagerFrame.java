package view;

import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import entity.CompanyGUI;
import service.Staff;
import utils.StringUtil;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.awt.event.ActionEvent;
/**
 * @author: yqw
 * @date: 2020/7/9
 * @description: 密码管理窗口
 */
public class PwdManagerFrame extends JInternalFrame {
	private CompanyGUI company;
	private String currentID;
	
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PwdManagerFrame frame = new PwdManagerFrame("B001");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public PwdManagerFrame(String id) throws IOException, ParseException {
		company = CompanyGUI.getInstance();
		this.currentID = id;
		
		setTitle("密码修改");
		setClosable(true);
		setBounds(100, 100, 450, 300);
		
		JLabel lblNewLabel = new JLabel("新密码");
		
		passwordField = new JPasswordField();
		
		JLabel lblNewLabel_1 = new JLabel("再次确认");
		
		passwordField_1 = new JPasswordField();
		
		JButton btnNewButton = new JButton("修改");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 修改密码
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				String password = passwordField.getText();
				String password1 = passwordField_1.getText();
				if(notEmpty(password, password1)) {
					if(password.equals(password1)) {
						Staff staff = company.search(currentID);
						try {
							company.deleteEmployee(staff);
							company.removeEmployee(staff);
							staff.setPassword(password);
							company.addEmployee(staff);
							company.writeEmployee(staff);
							JOptionPane.showMessageDialog(null, "密码修改成功!");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}else {
						JOptionPane.showMessageDialog(null, "密码不一致,修改失败!");
					}
					resetText();
				}
			}
		});
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(89)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel)
								.addComponent(lblNewLabel_1))
							.addGap(37)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(passwordField_1)
								.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(168)
							.addComponent(btnNewButton)))
					.addContainerGap(101, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(75)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addGap(33)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(passwordField_1, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addGap(35))
		);
		getContentPane().setLayout(groupLayout);
		
		ImageIcon image = new ImageIcon("pictures/sky.jpg");
		JLabel imageLabel = new JLabel(image);
		this.getLayeredPane().add(imageLabel,new Integer(Integer.MIN_VALUE));
		imageLabel.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
		Container container = this.getContentPane();
		((JPanel)container).setOpaque(false);
	}
	
	public void resetText() {
		/*
		 * 功能描述: 清空输入框
		 * @Param: []
		 * @Return: void
		 * @Date: 2020/7/10
		 */
		this.passwordField.setText("");
		this.passwordField_1.setText("");
	}
	
	public boolean notEmpty(String id, String password) {
		if(StringUtil.isEmpty(id)){
			JOptionPane.showMessageDialog(null, "密码不能为空!");
			return false;
		}
		if(StringUtil.isEmpty(password)) {
			JOptionPane.showMessageDialog(null, "再次确认密码不能为空!");
			return false;
		}
		return true;
	}
	
	
}
