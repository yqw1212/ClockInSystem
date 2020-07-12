package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.io.IOException;
import java.text.ParseException;
import java.util.Vector;

import javax.swing.JInternalFrame;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import entity.ClockInfo;
import entity.CompanyGUI;
import service.EmployeeFactory;
import service.Staff;
import utils.StringUtil;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * @author: yqw
 * @date: 2020/7/9
 * @description: 员工信息管理窗口
 */
public class EmployeeManagerFrame extends JInternalFrame {
	private CompanyGUI company;
	private JTable table;
	private JTextField IDTextField;
	private JTextField nameTextField;
	private JTextField pwdTextField;
	private JTextField searchIDTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeeManagerFrame frame = new EmployeeManagerFrame();
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
	public EmployeeManagerFrame() throws IOException, ParseException {
		company = CompanyGUI.getInstance();
		setClosable(true);
		setTitle("员工管理");
		setBounds(100, 100, 450, 450);
		
		JPanel panel = new JPanel();
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 433, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JLabel lblNewLabel = new JLabel("员工ID");
		
		IDTextField = new JTextField();
		IDTextField.setEditable(false);
		IDTextField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("员工姓名");
		
		nameTextField = new JTextField();
		nameTextField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("员工密码");
		
		pwdTextField = new JTextField();
		pwdTextField.setColumns(10);
		
		JButton btnNewButton = new JButton("修改信息");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 修改员工信息
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				if(StringUtil.isEmpty(IDTextField.getText())) {
					JOptionPane.showMessageDialog(null, "请先选择");
				}else {
					String id = IDTextField.getText();
					String name = nameTextField.getText();
					String password = pwdTextField.getText();
					
					if(notEmpty(name, password)) {
						Staff old = company.search(id);
						
						Staff staff = EmployeeFactory.create(1, id, name);
						staff.setPassword(password);
						staff.setRoot(old.getRoot());
						
						try {
							company.deleteEmployee(old);
							company.removeEmployee(old);
							company.addEmployee(staff);
							company.writeEmployee(staff);
							JOptionPane.showMessageDialog(null, "修改成功!");
							init();
							resetText();
						} catch (IOException e1) {
							e1.printStackTrace();
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
		JButton btnNewButton_1 = new JButton("删除员工");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 删除员工
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				if(StringUtil.isEmpty(IDTextField.getText())) {
					JOptionPane.showMessageDialog(null, "请先选择");
				}else {
					String id = IDTextField.getText();
					Staff staff = company.search(id);
					try {
						company.removeEmployee(staff);
						company.deleteEmployee(staff);
						JOptionPane.showMessageDialog(null, "删除成功!");
						init();
						resetText();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		JLabel lblNewLabel_3 = new JLabel("查找ID");
		
		searchIDTextField = new JTextField();
		searchIDTextField.setColumns(10);
		
		JButton btnNewButton_2 = new JButton("查找");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 查找员工
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				if (StringUtil.isEmpty(searchIDTextField.getText())) {
					JOptionPane.showMessageDialog(null, "查询ID不能为空!");
				}else {
					Staff staff = company.search(searchIDTextField.getText());
					if(staff == null) {
						JOptionPane.showMessageDialog(null, "无此ID员工!");
					}else {
						DefaultTableModel defaultTableModel = (DefaultTableModel) table.getModel();
						defaultTableModel.setRowCount(0);
						fillTable(staff);
					}
				}
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(14)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblNewLabel)
									.addGap(20)
									.addComponent(IDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblNewLabel_2)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(pwdTextField, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)))
							.addGap(25)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblNewLabel_1)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(btnNewButton)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(btnNewButton_1))))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(22)
							.addComponent(lblNewLabel_3)
							.addGap(13)
							.addComponent(searchIDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(49)
							.addComponent(btnNewButton_2)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(26)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(IDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_1)
								.addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(19)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(pwdTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnNewButton)
								.addComponent(btnNewButton_1)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(28)
							.addComponent(lblNewLabel)
							.addGap(24)
							.addComponent(lblNewLabel_2)))
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(15)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(searchIDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_3)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(12)
							.addComponent(btnNewButton_2)))
					.addGap(16))
		);
		panel.setLayout(gl_panel);
		
		table = new JTable() {
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
			    Component c = super.prepareRenderer(renderer, row, column);
			    if (c instanceof JComponent) {
			    	((JComponent) c).setOpaque(false);
			    }
			    return c;
			}
		};
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int row = table.getSelectedRow();
				IDTextField.setText((String) table.getValueAt(row, 0));
				nameTextField.setText((String) table.getValueAt(row, 1));
				pwdTextField.setText((String) table.getValueAt(row, 2));
				
			}
		});
		table.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] {"员工ID", "员工姓名", "员工密码"}) {
			boolean[] columnEditables = new boolean[] {
				false, false, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(90);
		table.getColumnModel().getColumn(1).setPreferredWidth(108);
		table.getColumnModel().getColumn(2).setPreferredWidth(111);
		scrollPane.setViewportView(table);
		getContentPane().setLayout(groupLayout);
		
		ImageIcon image = new ImageIcon("pictures/moon.jpg");
		JLabel imageLabel = new JLabel(image);
		this.getLayeredPane().add(imageLabel,new Integer(Integer.MIN_VALUE));
		imageLabel.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
		Container container = this.getContentPane();
		((JPanel)container).setOpaque(false);
		panel.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		((DefaultTableCellRenderer)table.getDefaultRenderer(Object.class)).setOpaque(false);
		table.setOpaque(false);
		
		
		init();
	}
	
	public void fillTable(Staff staff) {
		/*
		 * 功能描述: 将staff的信息填充到表格上
		 * @Param: [staff]
		 * @Return: void
		 * @Date: 2020/7/10
		 */
		DefaultTableModel defaultTableModel = (DefaultTableModel) this.table.getModel();
		Vector vector = new Vector<>();
		vector.add(staff.getId());
		vector.add(staff.getName());
		vector.add(staff.getPassword());
		defaultTableModel.addRow(vector);
	}
	
	public void init() throws IOException, ParseException {
		/*
		 * 功能描述: 初始化窗口,填充表格
		 * @Param: []
		 * @Return: void
		 * @Date: 2020/7/10
		 */
		DefaultTableModel defaultTableModel = (DefaultTableModel) this.table.getModel();
		defaultTableModel.setRowCount(0);
		for(Staff staff : company.getStaffs()) {
			fillTable(staff);
		}
	}
	
	public boolean notEmpty(String name, String password) {
		if(StringUtil.isEmpty(name)){
			JOptionPane.showMessageDialog(null, "姓名不能为空!");
			return false;
		}
		if(StringUtil.isEmpty(password)) {
			JOptionPane.showMessageDialog(null, "密码不能为空!");
			return false;
		}
		return true;
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
		this.pwdTextField.setText("");
	}
	
}
