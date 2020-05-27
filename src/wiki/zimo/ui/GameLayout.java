package wiki.zimo.ui;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import wiki.zimo.helper.ImageIconHelper;
/**
 * 游戏表格
 * @author zimo
 *
 */
public class GameLayout extends GridLayout implements MouseListener {
	private int rows;// 标记行数，以确定是3*3布局还是4*4布局
	private String pattern;// 标记模式，以确定是数字还是图像
	private JPanel panel;// 主面板引用，为了添加控件用
	private int size = 0;// 标记填到第几个格子
	private boolean flags[];// 标记已经选取过的图片名
	private JLabel labels[][];// 保存页面上的标签，3*3或4*4
	private int vals[][];// 保存当前局面
	private int dir[][] = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };// 标记上下左右四个方向

	public GameLayout(int rows, String pattern, JPanel panel) {
		super(rows, rows, 10, 10);
		this.rows = rows;// 3或者4，默认3
		this.pattern = pattern;// 数字或者图像，默认图像
		this.panel = panel;
		this.flags = new boolean[rows * rows - 1];// 3对应要填8个，4对应要填15个
		this.labels = new JLabel[rows][rows];// 3*3或4*4布局
		this.vals = new int[rows][rows];// 当前局面

		String res = null;// 模式不同，图片路径不同
		switch (pattern) {
		case "数字":
			res = "res/images/number/{}.jpg";
			break;

		case "图像":
			res = "res/images/people/";
			if (rows == 3) {
				res = "res/images/people/8/{}.gif";
			} else if (rows == 4) {
				res = "res/images/people/15/{}.gif";
			}
			break;
		case "自定义":
			res = "res/images/custom/";
			if (rows == 3) {
				res = "res/images/custom/8/{}.jpg";
			} else if (rows == 4) {
				res = "res/images/custom/15/{}.jpg";
			}
			break;
		}

		double scale = 0.0d;// 模式不同，图片放大缩小的比例不同
		switch (rows) {
		case 3:
			scale = 1.75;
			break;
		case 4:
			scale = 1.25;
			break;
		}

		addFinishAssembly(scale, res);
		// addAssembly(scale, res);// 添加组件

		while (isGameOver()) {// 有可能刚开局的时候游戏就已经结束了，随机数巧合，当这种情况下，应该重新添加组件
			for (int i = 0; i < rows; i++) {// 先移除之前的
				for (int j = 0; j < rows; j++) {
					this.panel.remove(labels[i][j]);
				}
			}
			// 然后重新初始化
			this.size = 0;
			this.labels = new JLabel[rows][rows];
			this.flags = new boolean[rows * rows - 1];// 3对应要填8个，4对应要填15个
			this.vals = new int[rows][rows];// 当前局面
			addAssembly(scale, res);// 再次添加组件
		}
		
		log();
	}
	
	/**
	 * 输出调试信息
	 */
	private void log() {
		System.out.println("isGameOver:"+isGameOver());
		for (int i = 0; i < rows; i++) {
			System.out.println(Arrays.toString(vals[i]));
		}
		System.out.println();
	}

	private void addFinishAssembly(double scale, String res) {
		int index = 1;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < rows; j++) {
				if (i == rows - 1 && j == rows - 1) {
					break;
				}
				vals[i][j] = index++;
				labels[i][j] = new JLabel(ImageIconHelper.scale(
						new ImageIcon(res.replace("{}", String.valueOf(vals[i][j])))/* 将通用的资源路径替换为实际的路径 */, scale)) {

					/**
					 * 圆角
					 */
					@Override
					public void paint(Graphics g) {
						RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, this.getWidth(),
								this.getHeight(), 20, 20);
						g.setClip(rect);
						super.paint(g);
					}

				};
				this.panel.add(labels[i][j]);
			}
		}
		vals[rows - 1][rows - 1] = 0;
		// 所有情况下右下角都固定的填0
		labels[rows - 1][rows - 1] = new JLabel(
				ImageIconHelper.scale(new ImageIcon("res/images/number/0.jpg"), scale)) {
			/**
			 * 圆角
			 */
			@Override
			public void paint(Graphics g) {
				RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), 20,
						20);
				g.setClip(rect);
				super.paint(g);
			}
		};
		this.panel.add(labels[rows - 1][rows - 1]);
	}

	private void addAssembly(double scale, String res) {

		while (true) {// 随机填图片，死循环意味着没填满就会一直填
			if (size >= flags.length) {// 代表已经填满了，终止循环
				break;
			}

			int random = (int) (Math.random() * flags.length);// 随机获取填的是哪张数字图片，随机数范围是[0-7]或者[0-14]
			while (flags[random]) {// 代表已经填过了
				random = (int) (Math.random() * flags.length);// 重新随机，让获取到的随机数不重复
			}

			flags[random] = true;// 标记为填过了

			int x = size / rows;// 计算当前填的这个格子在哪行
			int y = (size - x * rows) % rows;// 计算当前填的这个格子在哪列
			vals[x][y] = random + 1;// 将当前的随机数+1保存到对应的坐标，于是局面的范围变为[1,8]或者[1,15]，正好对应图片资源的名称中的数字部分，也就是res中{}应该被代替的部分

			labels[x][y] = new JLabel(ImageIconHelper
					.scale(new ImageIcon(res.replace("{}", String.valueOf(vals[x][y])))/* 将通用的资源路径替换为实际的路径 */, scale)) {

				/**
				 * 圆角
				 */
				@Override
				public void paint(Graphics g) {
					RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(),
							20, 20);
					g.setClip(rect);
					super.paint(g);
				}

			};
			labels[x][y].addMouseListener(this);
			this.panel.add(labels[x][y]);
			size++;// 填下一个格子
		}

		// 所有情况下右下角都固定的填0
		labels[rows - 1][rows - 1] = new JLabel(
				ImageIconHelper.scale(new ImageIcon("res/images/number/0.jpg"), scale)) {
			/**
			 * 圆角
			 */
			@Override
			public void paint(Graphics g) {
				RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), 20,
						20);
				g.setClip(rect);
				super.paint(g);
			}
		};
		labels[rows - 1][rows - 1].addMouseListener(this);
		this.panel.add(labels[rows - 1][rows - 1]);
	}

	/**
	 * 交换局面并且交换图片
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void swap(int x1, int y1, int x2, int y2) {
		int temp = vals[x1][y1];
		vals[x1][y1] = vals[x2][y2];
		vals[x2][y2] = temp;
		Icon itemp = labels[x1][y1].getIcon();
		labels[x1][y1].setIcon(labels[x2][y2].getIcon());
		labels[x2][y2].setIcon(itemp);
	}

	/**
	 * 判断游戏是否结束
	 * 
	 * @return
	 */
	public boolean isGameOver() {
		if (vals[rows - 1][rows - 1] != 0) {
			return false;
		}
		int index = 1;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < rows; j++) {
				if (i == rows - 1 && j == rows - 1) {
					break;
				}
				if (vals[i][j] != index++) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < rows; j++) {
				if (e.getSource() == labels[i][j]) {// 获取到当前点击的标签
					// System.out.println("点击了"+vals[i][j]+",坐标："+i+","+j);
					int clickX = i;// 当前点击的标签的横坐标
					int clickY = j;// 当前点击的标签的纵坐标
					for (int k = 0; k < dir.length; k++) {// 遍历当前点击的这个标签的上下左右四个方向，判断是否可以交换
						int nextX = clickX + dir[k][0];
						int nextY = clickY + dir[k][1];
						if (nextX >= 0 && nextX < rows && nextY >= 0 && nextY < rows && vals[nextX][nextY] == 0) {// 点击的标签的上下左右四个方向中有0，也就是空白格子
							swap(clickX, clickY, nextX, nextY);// 交换空白的格子和被点击的格子的位置
							log();// 输出调试信息
							if (isGameOver()) {// 每次有交换操作，都应该判断游戏是否结束
								JOptionPane.showMessageDialog(panel, "你赢了！", "恭喜", JOptionPane.INFORMATION_MESSAGE);// 弹出提示框
								// System.out.println("游戏结束");
							}
							return;// 因为局面中只可能出现一个0，于是完成这个操作之后，便可以终止此函数的执行
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
