package wiki.zimo.ui;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * 主窗体
 * @author zimo
 *
 */
public class MainFrame extends JFrame {

	public static final int WIDTH = 600;
	public static final int HEIGHT = 800;

	public MainFrame() {
		setDefaultLookAndFeelDecorated(false);
		setTitle("魔板");// 设置标w题
		setSize(WIDTH, HEIGHT);// 设置宽高
		setLocationRelativeTo(null);// 居中
		setResizable(false);// 不可调整大小
		// Toolkit tool = Toolkit.getDefaultToolkit(); // 创建系统该默认组件工具包
		// Dimension d = tool.getScreenSize(); // 获取屏幕尺寸，赋给一个二维坐标对象
		// setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);//
		// 让主窗体在屏幕中间显示
		addListener();// 添加事件监听
		setPanel(new MainPanel());// 添加面板
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);// 点击关闭的时候什么都不做
		setVisible(true);// 设置窗体可见
	}

	/**
	 * 添加组件监听
	 */
	private void addListener() {
		addWindowListener(new WindowAdapter() {// 添加窗体事件监听
			public void windowClosing(WindowEvent e) {// 窗体关闭时
				int closeCode = JOptionPane.showConfirmDialog(MainFrame.this, "确定退出游戏？", "提示！",
						JOptionPane.YES_NO_OPTION);// 弹出选择对话框，并记录用户选择
				if (closeCode == JOptionPane.YES_OPTION) {// 如果用户选择确定
					System.exit(0);// 关闭程序
				}
			}
		});
	}

	/**
	 * 设置主窗体中的面板
	 * 
	 * @param panel
	 *            面板
	 */
	public void setPanel(JPanel panel) {
		Container c = getContentPane();// 获取主容器对象
		c.removeAll();// 删除容器中所有组件
		c.add(panel);// 容器添加面板
		c.validate();// 容器重新验证所有组件
	}
}
