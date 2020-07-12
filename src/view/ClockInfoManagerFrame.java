package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JInternalFrame;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import entity.ClockInfo;
import entity.CompanyGUI;
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
 * @description: 打卡信息管理窗口
 */
public class ClockInfoManagerFrame extends JInternalFrame {
	private CompanyGUI company;
	private SimpleDateFormat simpleDateFormat;
	
	private JTable table;
	private JTextField IDTextField;
	private JTextField nameTextField;
	private JTextField inTextField;
	private JTextField backTextField;
	private JTextField searchIDTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClockInfoManagerFrame frame = new ClockInfoManagerFrame();
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
	public ClockInfoManagerFrame() throws IOException, ParseException {
		company = CompanyGUI.getInstance();
		simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
		setClosable(true);
		setBounds(100, 100, 650, 450);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
						.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE)
					.addGap(50))
		);
		
		JLabel lblNewLabel = new JLabel("员工ID");
		
		IDTextField = new JTextField();
		IDTextField.setEditable(false);
		IDTextField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("员工姓名");
		
		nameTextField = new JTextField();
		nameTextField.setEditable(false);
		nameTextField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("签到时间");
		
		inTextField = new JTextField();
		inTextField.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("签退时间");
		
		backTextField = new JTextField();
		backTextField.setColumns(10);
		
		JButton btnNewButton = new JButton("修改时间");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 修改打卡时间
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				if(StringUtil.isEmpty(inTextField.getText())) {
					JOptionPane.showMessageDialog(null, "签到时间不能为空!");
				}else {
					try {
						ClockInfo clockInfo = company.searchClockInfo(IDTextField.getText(), simpleDateFormat.parse(backTextField.getText()));
						clockInfo.setIn(simpleDateFormat.parse(inTextField.getText()));
						clockInfo.setBack(simpleDateFormat.parse(backTextField.getText()));
						JOptionPane.showMessageDialog(null, "修改成功!");
						init();
					} catch (ParseException e1) {
						e1.printStackTrace();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		JButton btnNewButton_1 = new JButton("删除");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 删除打卡信息
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				if(StringUtil.isEmpty(inTextField.getText())) {
					JOptionPane.showMessageDialog(null, "签到时间不能为空!");
				}else if (backTextField.getText()==null || "".equals(backTextField.getText().trim())) {
					JOptionPane.showMessageDialog(null, "签退时间不能为空!");
				}else {
					String id = IDTextField.getText();
					try {
						Date date = simpleDateFormat.parse(inTextField.getText());
						ClockInfo clockInfo = company.searchClockInfo(id, date.toString());
						company.deleteClockInfo(clockInfo);
						company.removeClockInfo(clockInfo);
						init();
					} catch (ParseException e1) {
						JOptionPane.showMessageDialog(null, "日期格式错误!");
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		JLabel lblNewLabel_4 = new JLabel("ID");
		
		searchIDTextField = new JTextField();
		searchIDTextField.setColumns(10);
		
		JButton btnNewButton_2 = new JButton("查询");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 查询打卡信息
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				if(StringUtil.isEmpty(searchIDTextField.getText())) {
					JOptionPane.showMessageDialog(null, "查询ID不能为空!");
				}else {
					DefaultTableModel defaultTableModel = (DefaultTableModel) table.getModel();
					defaultTableModel.setRowCount(0);
					for(ClockInfo clockInfo : company.onlyEmployee(searchIDTextField.getText())) {
						fillTable(clockInfo);
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
							.addGap(47)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel_2)
										.addComponent(lblNewLabel))
									.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(IDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(inTextField, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblNewLabel_4)
									.addGap(30)
									.addComponent(searchIDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblNewLabel_3)
								.addComponent(lblNewLabel_1)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(109)
							.addComponent(btnNewButton)
							.addPreferredGap(ComponentPlacement.RELATED, 115, Short.MAX_VALUE)
							.addComponent(btnNewButton_1)))
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(12)
							.addComponent(backTextField, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_2)))
					.addContainerGap(31, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(IDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1)
						.addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(inTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_3)
						.addComponent(backTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnNewButton)
								.addComponent(btnNewButton_1))
							.addGap(35))
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnNewButton_2)
								.addComponent(lblNewLabel_4)
								.addComponent(searchIDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addContainerGap(27, Short.MAX_VALUE))
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
				inTextField.setText((String) table.getValueAt(row, 2));
				backTextField.setText((String) table.getValueAt(row, 3));
			}
		});
		table.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] {"员工ID", "员工姓名", "签到时间", "签退时间"}) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(84);
		table.getColumnModel().getColumn(1).setPreferredWidth(111);
		table.getColumnModel().getColumn(2).setPreferredWidth(149);
		table.getColumnModel().getColumn(3).setPreferredWidth(168);
		scrollPane.setViewportView(table);
		getContentPane().setLayout(groupLayout);
		
		ImageIcon image = new ImageIcon("pictures/snow.jpg");
		JLabel imageLabel = new JLabel(image);
		this.getLayeredPane().add(imageLabel,new Integer(Integer.MIN_VALUE));
		imageLabel.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
		Container container = this.getContentPane();
		((JPanel)container).setOpaque(false);
		panel.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		table.setOpaque(false);
		
		init();
	}
	
	public void fillTable(ClockInfo clockInfo) {
		/*
		 * 功能描述: 将clockInfo信息填充到表格上
		 * @Param: [clockInfo]
		 * @Return: void
		 * @Date: 2020/7/10
		 */
		DefaultTableModel defaultTableModel = (DefaultTableModel) this.table.getModel();
		Vector vector = new Vector<>();
		vector.add(clockInfo.getId());
		vector.add(this.company.search(clockInfo.getId()).getName());
		vector.add(simpleDateFormat.format(clockInfo.getIn()));
		if(clockInfo.getBack() != null) {
			vector.add(simpleDateFormat.format(clockInfo.getBack()));
		}else {
			vector.add(clockInfo.getBack());
		}
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
		for(ClockInfo clockInfo : this.company.getSAllInfo()) {
			fillTable(clockInfo);
		}
	}
}
