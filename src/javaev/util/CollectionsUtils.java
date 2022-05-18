package javaev.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CollectionsUtils {
	public static String toString(List<Object> list) {
		if (null != list) {
			StringBuffer sbuf = new StringBuffer();
			sbuf.append("[");
			int index = 0;
			Object object;
			for (Iterator<Object> iterator = list.iterator(); iterator.hasNext();) {
				object = iterator.next();
				if (index != 0) {
					sbuf.append(", ");
				}
				if (null != object) {
					sbuf.append(object.toString());
				} else {
					sbuf.append("null");
				}
				index++;
			}
			sbuf.append("]");
			return sbuf.toString();
		}
		return null;
	}

	public void add(List<Object> list, Object object) {
		if (null != list) {
			if (Collections.frequency(list, object) == 0) {
				list.add(object);
			}
		}
	}

	/**
	 * 筛选重复元素并添加到新列表；
	 * 
	 * @param <T>  对象类型；
	 * @param src  含有复数Object的列表；
	 * @param dest 含有单数Object的列表；
	 * @return 返回dest；
	 */
	public void removeRepetition(List<Object> src, List<Object> dest) {
		if (null != src && null != dest) {
			for (Iterator<Object> iterator = src.iterator(); iterator.hasNext();) {
				add(dest, iterator.next());
			}
		}
	}

	public void toList(List<Object> list, Object[] array) {
		if (null != list && null != array) {
			for (int i = 0, iLength = array.length; i < iLength; i++) {
				add(list, array[i]);
			}
		}
	}
}
