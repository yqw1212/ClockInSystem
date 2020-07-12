package view;

import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import service.ShowIcon;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.io.IOException;
import java.text.ParseException;
/**
 * @author: yqw
 * @date: 2020/7/9
 * @description: 登录注册窗口
 */
public class LogInFrame extends JFrame implements ShowIcon{
	
	private static LogInFrame logInFrame = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogInFrame frame = LogInFrame.getInstance();
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
	private LogInFrame() throws IOException, ParseException {
		setResizable(false);
		setTitle("员工打卡系统登录");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 375, 300);
		changeContentPane(new LogInPanel());
		
		ImageIcon image = new ImageIcon("pictures/snow.jpg");
		JLabel imageLabel = new JLabel(image);
		this.getLayeredPane().add(imageLabel,new Integer(Integer.MIN_VALUE));
		imageLabel.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
		
		setIcon();
		
	}
	
	public void changeContentPane(Container contentPane) {
		/*
		 * 功能描述: 切换面板
		 * @Param: [contentPane]
		 * @Return: void
		 * @Date: 2020/7/10
		 */
		this.setContentPane(contentPane);
		this.revalidate();
		Container container = this.getContentPane();
		((JPanel)container).setOpaque(false);
	}
	
	public static LogInFrame getInstance() throws IOException, ParseException {
		if (logInFrame == null) {
			logInFrame = new LogInFrame();
		}
		return logInFrame;
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


