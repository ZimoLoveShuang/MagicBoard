package wiki.zimo.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import wiki.zimo.helper.ColorHelper;
import wiki.zimo.helper.ImageHelper;
/**
 * 主面板
 * @author zimo
 *
 */
public class MainPanel extends JPanel implements ItemListener {
	private JLabel gameType;
	private String gameTypeStr = "数字魔板";
	private JLabel gameTips;
	private String gameTipsStr = "『移动方块使数字顺序排列』";
	private ButtonGroup difficulty;// 难度
	private ButtonGroup pattern;// 模式
	private JTextArea time;// 用时
	private Timer timer;// 定时器
	private TimeTask timeTask;// 定时任务
	private int minute = 0;
	private int second = 0;
	private JLabel refresh;// 刷新
	private GamePanel gamePanel;
	int rows = 3;
	String patternStr = "数字";

	public MainPanel() {
		setLayout(null);// 绝对布局

		gameType = new JLabel();
		gameType.setText(gameTypeStr);
		gameType.setBounds(30, 10, 300, 40);
		gameType.setFont(new Font("楷体", Font.BOLD, 40));
		add(gameType);

		gameTips = new JLabel();
		gameTips.setText(gameTipsStr);
		gameTips.setFont(new Font("微软雅黑", Font.CENTER_BASELINE, 20));
		gameTips.setBounds(30, 60, 300, 40);
		add(gameTips);

		JLabel l1 = new JLabel();
		l1.setText(" 难度：");
		l1.setBounds(40, 110, 100, 60);
		add(l1);

		difficulty = new ButtonGroup();
		JRadioButton c1 = new JRadioButton("初级", true);
		c1.setBounds(80, 130, 60, 20);
		c1.setBackground(new Color(239, 242, 229));
		JRadioButton c2 = new JRadioButton("高级");
		c2.setBounds(140, 130, 60, 20);
		c2.setBackground(new Color(239, 242, 229));
		difficulty.add(c1);
		difficulty.add(c2);
		add(c1);
		add(c2);

		c1.addItemListener(this);
		c2.addItemListener(this);

		JLabel l2 = new JLabel();
		l2.setBounds(240, 110, 100, 60);
		l2.setText(" 模式：");
		add(l2);

		pattern = new ButtonGroup();
		JRadioButton c3 = new JRadioButton("数字", true);
		c3.setBounds(280, 130, 60, 20);
		c3.setBackground(new Color(239, 242, 229));
		JRadioButton c4 = new JRadioButton("图像");
		c4.setBounds(340, 130, 60, 20);
		c4.setBackground(new Color(239, 242, 229));
		JRadioButton c5 = new JRadioButton("自定义");
		c5.setBounds(400, 130, 80, 20);
		c5.setBackground(new Color(239, 242, 229));
		pattern.add(c3);
		pattern.add(c4);
		pattern.add(c5);
		add(c3);
		add(c4);
		add(c5);

		c3.addItemListener(this);
		c4.addItemListener(this);
		c5.addItemListener(this);

		time = new JTextArea() {
			/**
			 * 圆角
			 */
//			@Override
//			public void paint(Graphics g) {
//				RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), 20,
//						20);
//				g.setClip(rect);
//				super.paint(g);
//			}
		};
		time.setEnabled(false);
		time.setText("用 时\n00:00");
		time.setBounds(500, 10, 65, 60);
		time.setFont(new Font("楷体", Font.BOLD, 20));
		time.setBackground(ColorHelper.parseToColor("#baaca0"));
		time.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(time);

		refresh = new JLabel() {
			/**
			 * 图片自适应JLabel大小
			 */
			@Override
			protected void paintComponent(Graphics g) {
				ImageIcon icon = new ImageIcon("res/images/refresh.png");
				g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), icon.getImageObserver());
			}
		};
		refresh.setBounds(500, 80, 60, 60);
		refresh.setOpaque(true);
		refresh.setBackground(ColorHelper.parseToColor("#baaca0"));
		add(refresh);

		refresh.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				remove(gamePanel);
				repaint();
				gamePanel = new GamePanel(rows, patternStr);
				gameType.setText(gameTypeStr);
				gameTips.setText(gameTipsStr);
				add(gamePanel);
				timeTask.reset();
			}
		});

		gamePanel = new GamePanel(3);
		add(gamePanel);// 添加游戏面板

		JLabel l = new JLabel();
		l.setText("『鼠标点按空白方块旁边的方块以进行移动』");
		l.setFont(new Font("微软雅黑", Font.CENTER_BASELINE, 20));
		l.setBounds(90, 710, 400, 40);
		add(l);

		timer = new Timer("timer");
		timeTask = new TimeTask();
		timer.schedule(new TimeTask(), 0, 1 * 1000);
	}

	/**
	 * 渐变色背景
	 * 
	 * @param g1
	 */
	@Override
	protected void paintComponent(Graphics g1) {// 重写绘制组件外观
		Graphics2D g = (Graphics2D) g1;
		super.paintComponent(g);// 执行超类方法
		int width = getWidth();// 获取组件大小
		int height = getHeight();
		GradientPaint paint = new GradientPaint(0, 0, ColorHelper.parseToColor("#edf7ef"), 0, height,
				ColorHelper.parseToColor("#fbe0ba"));// 创建填充模式对象
		g.setPaint(paint);// 设置绘图对象的填充模式
		g.fillRect(0, 0, width, height);// 绘制矩形填充控件界面
	}

	/**
	 * 模式和难度切换
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			remove(gamePanel);
			repaint();
			JRadioButton b = (JRadioButton) e.getSource();
			if (b.getText().equals("高级")) {
				rows = 4;
			} else if (b.getText().equals("初级")) {
				rows = 3;
			}

			if (b.getText().equals("数字")) {
				gameTypeStr = "数字魔板";
				gameTipsStr = "『移动方块使数字顺序排列』";
				patternStr = "数字";
			} else if (b.getText().equals("图像")) {
				gameTypeStr = "图像魔板";
				gameTipsStr = "『移动方块使图像恢复原图』";
				patternStr = "图像";
			} else if (b.getText().equals("自定义")) {
				gameTypeStr = "图像魔板";
				gameTipsStr = "『移动方块使图像恢复原图』";
				patternStr = "自定义";

				// 弹出文件选择
				JFileChooser fileChooser = new JFileChooser();
				// 只选择图片
				FileFilter filter = new FileNameExtensionFilter("图像文件（JPG/GIF）", "JPG", "JPEG", "GIF");
				fileChooser.setFileFilter(filter);

				int code = fileChooser.showOpenDialog(getParent());
				// 判断用户单击的是否为打开按钮
				if (code == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();// 获得选中的图片对象
					try {
						ImageHelper.dealAndSaveImage(file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// System.out.print(file.getAbsolutePath());
				}
			}

			// System.out.println(rows+","+patternStr);
			gamePanel = new GamePanel(rows, patternStr);
			gameType.setText(gameTypeStr);
			gameTips.setText(gameTipsStr);
			add(gamePanel);
			repaint();
			timeTask.reset();
		}
	}

	/**
	 * 定时任务
	 */
	class TimeTask extends TimerTask {

		@Override
		public void run() {
			second++;
			minute += second / 60;
			second = second % 60;
			time.setText("用 时\n" + String.format("%02d:%02d", minute, second));
		}

		public void reset() {
			second = 0;
			minute = 0;
			time.setText("用 时\n" + String.format("%02d:%02d", minute, second));
		}
	}
}
