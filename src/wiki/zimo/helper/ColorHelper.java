package wiki.zimo.helper;

import java.awt.Color;

public class ColorHelper {
	/**
	 * 将'#edf7ef'这样的字符串转换为color对象
	 * 
	 * @param c
	 * @return
	 */
	public static Color parseToColor(String c) {
		if (c.matches("#([0-9]|[a-f]){6}")) {
			return new Color(Integer.parseInt(c.replace("#", ""), 16));
		}
		return null;
	}
}
