package javaev.util;

import java.util.Arrays;

import javaev.lang.StringUtils;

public class ArraysUtils {
	public static int[] toArray(int[][] from, int[] to) {
		if (null != from && null != to) {
			int index = 0;
			int lenf = from.length;
			int lent = to.length;
			int[] arrf;
			for (int i = 0, ilength = lenf; i < ilength; i++) {
				arrf = from[i];
				if (null != arrf) {
					for (int j = 0, jlength = arrf.length; j < jlength; j++) {
						if (index < lent) {
							to[index] = from[i][j];
							index++;
						}
					}
				} else {
					if (index < lent) {
						to[index] = -1;
						index++;
					}
				}
			}
			return to;
		}
		return null;
	}

	public static Object[] toArray(Object[][] from, Object[] to) {
		if (null != from && null != to) {
			int index = 0;
			int lenf = from.length;
			int lent = to.length;
			Object[] arrf;
			for (int i = 0, ilength = lenf; i < ilength; i++) {
				arrf = from[i];
				if (null != arrf) {
					for (int j = 0, jlength = arrf.length; j < jlength; j++) {
						if (index < lent) {
							to[index] = from[i][j];
							index++;
						}
					}
				} else {
					if (index < lent) {
						to[index] = -1;
						index++;
					}
				}
			}
			return to;
		}
		return null;
	}

	public static String toString(Object object) {
		if (object instanceof Object[]) {
			return toString((Object[]) object);
		} else if (object instanceof Object[][]) {
			return toString((Object[][]) object);
		} else {
			return object.toString();
		}
	}

	private static String toString(Object[] array) {
		return Arrays.toString(array);
	}

	private static String toString(Object[][] arrays) {
		StringBuffer sbuf = new StringBuffer();
		for (int i = 0; i < arrays.length; i++) {
			sbuf.append(toString(arrays[i]));
			sbuf.append(StringUtils.Line_Separator);
		}
		return sbuf.toString();
	}

}
