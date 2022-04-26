package javaev.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CollectionsUtils {
	public static <T> String toString(List<T> list) {
		if (null != list) {
			StringBuffer sbuf = new StringBuffer();
			Object object;
			for (Iterator<T> iterator = list.iterator(); iterator.hasNext();) {
				object = iterator.next();
				if (null != object) {
					sbuf.append(object.toString());
				}
			}
			return sbuf.toString();
		}
		return null;
	}

	public <T> void add(List<T> list, T object) {
		if (null != list) {
			if (Collections.frequency(list, object) == 0) {
				list.add(object);
			}
		}
	}

	/**
	 * 筛选重复元素并添加到新列表；
	 * @param <T> 对象类型；
	 * @param src  含有复数Object的列表；
	 * @param dest 含有单数Object的列表；
	 */
	public <T> void removeRepetition(List<T> src, List<T> dest) {
		if (null != src && null != dest) {
			for (Iterator<T> iterator = src.iterator(); iterator.hasNext();) {
				add(dest, iterator.next());
			}
		}
	}
}
