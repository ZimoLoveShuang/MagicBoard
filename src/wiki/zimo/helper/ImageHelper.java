package wiki.zimo.helper;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageHelper {
	/**
	 * 处理图片
	 * @param file
	 * @throws IOException
	 */
	public static void dealAndSaveImage(File file) throws IOException {
		dealAndSaveImage(ImageIO.read(file));
	}

	/**
	 * 处理图片
	 * @param image
	 * @throws IOException
	 */
	public static void dealAndSaveImage(BufferedImage image) throws IOException {
		cut(image, 3);
		cut(image, 4);
	}

	/**
	 * 裁剪并保存图片
	 * @param image
	 * @param block
	 * @throws IOException
	 */
	private static void cut(BufferedImage image, int block) throws IOException {
		int width = image.getWidth();
		int height = image.getHeight();

		// 按高和宽中较小的切，所有图片都会被当做正方形切，多余的部分将忽略
		int min = Math.min(width, height);
		int dis = min / block;
		int pos = 1;
		for (int i = 0; i < block; i++) {
			for (int j = 0; j < block; j++) {
				int startY = i * dis;
				int startX = j * dis;
				// System.out.println(startX + "," + startY);
				BufferedImage subimage = image.getSubimage(startX, startY, dis, dis);
				BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
				Graphics graphics = img.getGraphics();
				graphics.drawImage(subimage, 0, 0, 100, 100, null);
				ImageIO.write(img, "jpg", new File("res/images/custom/" + (block * block - 1) + "/", pos + ".jpg"));
				pos++;
				if (pos == block * block) {
					break;
				}
			}
		}
	}
}
