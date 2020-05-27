package wiki.zimo.helper;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageIconHelper {
	/**
	 * 缩放图标图片
	 * @param image
	 * @param scale
	 * @return
	 */
	public static ImageIcon scale(ImageIcon image, double scale) {
		int width = (int) (image.getIconWidth() * scale);
		int height = (int) (image.getIconHeight() * scale);
		Image img = image.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
		ImageIcon image2 = new ImageIcon(img);
		return image2;
	}
}
