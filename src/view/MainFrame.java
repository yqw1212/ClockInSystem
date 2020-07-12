package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;

import entity.ClockInfo;
import entity.CompanyGUI;
import service.ShowIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JDesktopPane;
/**
 * @author: yqw
 * @date: 2020/7/9
 * @description: 主界面窗口
 */
public class MainFrame extends JFrame implements ShowIcon{
	private CompanyGUI company;
	
	private String currentID;
	
	private JDesktopPane desktopPane;
	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame("A001");
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
	public MainFrame(String ID) throws IOException, ParseException {
		this.currentID = ID;
		this.company = CompanyGUI.getInstance();
		
		desktopPane = new JDesktopPane();
		
		setTitle("员工打卡系统");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 650);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("打卡");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("签到");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 签到
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				try {
					int result = company.signIn(currentID);
					if(result == -2) {
						JOptionPane.showMessageDialog(null, "今天已经签过到了！");
					}
					else  { 
						if(result == -1) {
							JOptionPane.showMessageDialog(null, "签到成功!");
						}else {
							JOptionPane.showMessageDialog(null, "您已迟到" + result/60 + "小时" + result%60 + "分钟");
						}
						init();
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("签退");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 签退
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				try {
					int result = company.signBack(currentID);
					if(result == 0) {
						JOptionPane.showMessageDialog(null, "今天还没有签到，无法签退!");
					}else if(result == -2){
						JOptionPane.showMessageDialog(null, "与上次打卡时间小于一分钟，请不要打卡过于频繁!");
					}else {
						if(result == -1) {
							JOptionPane.showMessageDialog(null, "签退成功!");
						}else {
							JOptionPane.showMessageDialog(null, "您已早退" + result/60 + "小时" + result%60 + "分钟");
						}
						init();
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenu mnNewMenu_1 = new JMenu("账户管理");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("修改密码");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 修改密码
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				try {
					PwdManagerFrame pwdManagerFrame = new PwdManagerFrame(currentID);
					desktopPane.add(pwdManagerFrame);
					pwdManagerFrame.setVisible(true);
					
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_2);
		
		JMenu mnNewMenu_2 = new JMenu("管理员操作");
		menuBar.add(mnNewMenu_2);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("员工管理");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 打开员工管理窗口
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				if(company.search(currentID).getRoot() == 1) {
					EmployeeManagerFrame employeeManagerFrame;
					try {
						employeeManagerFrame = new EmployeeManagerFrame();
						desktopPane.add(employeeManagerFrame);
						employeeManagerFrame.setVisible(true);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}else {
					JOptionPane.showMessageDialog(null, "您不是管理员,没有操作权限");
				}
			}
		});
		mnNewMenu_2.add(mntmNewMenuItem_3);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("打卡信息管理");
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 功能描述: 打开打卡信息管理窗口
				 * @Param: [e]
				 * @Return: void
				 * @Date: 2020/7/10
				 */
				if(company.search(currentID).getRoot() == 1) {
					ClockInfoManagerFrame clockInfoManagerFrame;
					try {
						clockInfoManagerFrame = new ClockInfoManagerFrame();
						desktopPane.add(clockInfoManagerFrame);
						clockInfoManagerFrame.setVisible(true);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}else {
					JOptionPane.showMessageDialog(null, "您不是管理员,没有操作权限");
				}
			}
		});
		mnNewMenu_2.add(mntmNewMenuItem_4);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		contentPane.add(desktopPane, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 15, 788, 523);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		
		
		desktopPane.add(scrollPane);
		
		table = new JTable() {
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
			    Component c = super.prepareRenderer(renderer, row, column);
			    if (c instanceof JComponent) {
			    	((JComponent) c).setOpaque(false);
			    }
			    return c;
			}
		};
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
		table.getColumnModel().getColumn(0).setPreferredWidth(101);
		table.getColumnModel().getColumn(1).setPreferredWidth(103);
		table.getColumnModel().getColumn(2).setPreferredWidth(230);
		table.getColumnModel().getColumn(3).setPreferredWidth(249);
		
		((DefaultTableCellRenderer)table.getDefaultRenderer(Object.class)).setOpaque(false);
		table.setOpaque(false);
		
		scrollPane.setColumnHeaderView(table.getTableHeader());//设置头部（HeaderView部分）
		scrollPane.getColumnHeader().setOpaque(false);//再取出头部，并设置为透明
		
		JTableHeader header = table.getTableHeader();//获取头部 
        header.setOpaque(false);//设置头部为透明
        header.getTable().setOpaque(false);//设置头部里面的表格透明
		
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setOpaque(false);
		header.setDefaultRenderer(render);
		TableCellRenderer headerRenderer = header.getDefaultRenderer();
//		if(headerRenderer instanceof JLabel) {
//			((JLabel)headerRenderer).setHorizontalAlignment(JLabel.CENTER);
//			((JLabel)headerRenderer).setOpaque(false);
//		}
		
		scrollPane.setViewportView(table);
		
		ImageIcon image = new ImageIcon("pictures/sky.jpg");
		JLabel imageLabel = new JLabel(image);
		this.getLayeredPane().add(imageLabel,new Integer(Integer.MIN_VALUE));
		imageLabel.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
		Container container = this.getContentPane();
		((JPanel)container).setOpaque(false);
		desktopPane.setOpaque(false);
		
		setIcon();
		init();
		
	}
	
	public void fillTable(ClockInfo clockInfo) {
		/*
		 * 功能描述: 将clockInfo的信息填充到表格中
		 * @Param: [clockInfo]
		 * @Return: void
		 * @Date: 2020/7/10
		 */
		DefaultTableModel defaultTableModel = (DefaultTableModel) this.table.getModel();
		Vector vector = new Vector<>();
		vector.add(clockInfo.getId());
		vector.add(this.company.search(clockInfo.getId()).getName());
		vector.add(clockInfo.getIn());
		vector.add(clockInfo.getBack());
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

	@Override
	public void setIcon() {
		/*
		 * 功能描述: 设置小图标
		 * @Param: []
		 * @Return: void
		 * @Date: 2020/7/10
		 */
		ImageIcon icon = new ImageIcon("pictures/clock.jpg");
		setIconImage(icon.getImage());
	}
}
