package wiki.zimo.ui;

import java.awt.Graphics;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import wiki.zimo.helper.ColorHelper;
/**
 * 游戏面板
 * @author zimo
 *
 */
public class GamePanel extends JPanel {
	public static final int SIZE = 540;

	public GamePanel(int rows, String pattern) {
		setBackground(ColorHelper.parseToColor("#baaca0"));
		setBounds((MainFrame.WIDTH - SIZE) / 2, 160, SIZE, SIZE);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));// 设置边框
		setLayout(new GameLayout(rows, pattern, this));
	}

	public GamePanel(int rows) {
		this(rows, "数字");
	}

	/**
	 * 圆角
	 */
	@Override
	public void paint(Graphics g) {
		RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), 20, 20);
		g.setClip(rect);
		super.paint(g);
	}

}
