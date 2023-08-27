package javaev.swing;

import java.awt.Font;
import java.awt.Point;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

public class SwingUtils {

	/**
	 * 请求invoker组件从JPopupMenu上获取输入焦点;
	 * 
	 * @param jPopupMenu JPopupMenu弹出式菜单;
	 * @param invoker    JComponent组件;
	 * @return JComponent 返回获取输入焦点的invoker组件;
	 */
	public static JComponent requestFocus(JPopupMenu jPopupMenu, JComponent invoker) {
		if (null == invoker || null == jPopupMenu) {
			return null;
		}
		jPopupMenu.setVisible(false);
		invoker.setFocusable(true);
		invoker.requestFocus();
		return invoker;
	}

	/**
	 * 在组件调用者invoker的坐标空间中的位置 X、Y 显示弹出菜单jPopupMenu;
	 * 
	 * @param invoker         JComponent组件;
	 * @param jPopupMenu      JPopupMenu弹出式菜单;
	 * @param x               JPopupMenu弹出显示的坐标点x;
	 * @param y               JPopupMenu弹出显示的坐标点y;
	 * @param jComponentFocus 在JPopupMenu上显示的组件;
	 * @return JPopupMenu 返回获取输入焦点的弹出式菜单jPopupMenu;
	 */
	public static JPopupMenu show(JComponent invoker, JPopupMenu jPopupMenu, int x, int y, JComponent jComponentFocus) {
		if (null == invoker || null == jPopupMenu || null == jComponentFocus) {
			return null;
		}
		jPopupMenu.show(invoker, x, y);
		jComponentFocus.requestFocus();
		return jPopupMenu;
	}

	/**
	 * 在组件调用者invoker的坐标空间中的点位置Point显示弹出菜单jPopupMenu;
	 * 
	 * @param invoker         JComponent组件;
	 * @param jPopupMenu      JPopupMenu弹出式菜单;
	 * @param jComponentPoint JPopupMenu弹出显示的坐标点Point;
	 * @param jComponentFocus 在JPopupMenu上显示的组件;
	 * @return JPopupMenu 返回获取输入焦点的弹出式菜单jPopupMenu;
	 */
	public static JPopupMenu show(JComponent invoker, JPopupMenu jPopupMenu, Point jComponentPoint,
			JComponent jComponentFocus) {
		if (null == jComponentPoint) {
			return null;
		}
		return show(invoker, jPopupMenu, jComponentPoint.x, jComponentPoint.y, jComponentFocus);
	}

	public static void updateUI(Object fontComponent, Font font) {
		if (null != fontComponent && null != font) {
			UIManager.put(fontComponent, font);
		}
	}

	public Font getFontDefault() {
		return new Font("微软雅黑", Font.PLAIN, 12);
	}

	public void updateUI(Font font) {
		if (null != font) {
			Set<Entry<Object, Object>> eSet = UIManager.getDefaults().entrySet();
			Entry<Object, Object> entry;
			Object key;
			Object value;
			String temp;
			for (Iterator<Entry<Object, Object>> iterator = eSet.iterator(); iterator.hasNext();) {
				entry = (Entry<Object, Object>) iterator.next();
				key = entry.getKey();
				value = entry.getValue();
				if (null != key && null != value) {
					temp = key.toString().toLowerCase();
					if (temp.endsWith("font")) {
						updateUI(key, font);
					}
				}
			}
		}
	}

}
