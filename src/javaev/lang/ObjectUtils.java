package javaev.lang;

import java.awt.Dimension;
import java.awt.Point;

import java.util.Properties;

public class ObjectUtils {
	public static String tag_boolean = "boolean";
	public static String tag_boolean_false = "false";
	public static String tag_boolean_true = "true";
	public static String tag_bracket_close = "]";
	public static String tag_bracket_close_shift = "}";
	public static String tag_bracket_open = "[";
	public static String tag_bracket_open_shift = "{";
	public static String tag_byte = "byte";
	public static String tag_class = "java";
	public static String tag_comma = ",";
	public static String tag_dimension = "dimension";
	public static String tag_double = "double";
	public static String tag_equals = "=";
	public static String tag_float = "float";
	public static String tag_height = "height";
	public static String tag_int = "int";
	public static String tag_integer = "integer";
	public static String tag_long = "long";
	public static String tag_point = "point";
	public static String tag_short = "short";
	public static String tag_split = ", ";
	public static String tag_width = "width";
	public static String tag_x = "x";
	public static String tag_y = "y";

	public static boolean equals(Object object, Object object1) {
		if ((null == object && null == object1) || (null != object && object.equals(object1))) {
			return true;
		}
		return false;
	}

	public static boolean isObjectBasic(Object object) {
		if ((null != object) && (object instanceof Boolean || object instanceof Character || object instanceof Number
				|| object instanceof String)) {
			return true;
		}
		return false;
	}

	private String tag_regex = "-*\\d+\\.*\\d*";

	public Object getObject(String stringObject) {
		if (null != stringObject) {
			stringObject = stringObject.trim().toLowerCase();
			String classType;
			String classProperties;
			int indexPrefix;
			int indexSuffix;
			double w = -1;
			double h = -1;
			double x = -1;
			double y = -1;
			String key;
			String value;
			String[] array;
			if (stringObject.startsWith(tag_class)) {
				if (stringObject.contains(tag_bracket_open) && stringObject.contains(tag_bracket_close)
						&& stringObject.contains(tag_comma)) {
					indexPrefix = stringObject.indexOf(tag_bracket_open);
					indexSuffix = stringObject.indexOf(tag_bracket_close);
					classType = stringObject.substring(0, indexPrefix);
					classProperties = stringObject.substring(indexPrefix + 1, indexSuffix);
					array = classProperties.split(tag_comma);// ,
					key = array[0];
					value = array[1];
					if (key.contains(tag_equals)) {
						if (key.startsWith(tag_width)) {
							w = Integer.valueOf(key.substring(6));
						} else if (key.startsWith(tag_x)) {
							x = Integer.valueOf(key.substring(2));
						}
					}
					if (value.contains(tag_equals)) {
						if (value.startsWith(tag_height)) {
							h = Integer.valueOf(value.substring(7));
						} else if (value.startsWith(tag_y)) {
							y = Integer.valueOf(value.substring(2));
						}
					}
					if (w != -1 && h != -1 && classType.endsWith(tag_dimension)) {
						Dimension size = new Dimension();
						size.setSize(w, h);
						return size;
					} else if (x != -1 && y != -1 && classType.endsWith(tag_point)) {
						Point location = new Point();
						location.setLocation(x, y);
						return location;
					}
				}
			} else {
				if (stringObject.equalsIgnoreCase(tag_boolean_true)
						|| stringObject.equalsIgnoreCase(tag_boolean_false)) {
					return Boolean.valueOf(stringObject);
				} else if (stringObject.matches(tag_regex)) {
					return Double.valueOf(stringObject);
				}
			}
		}
		return stringObject;
	}

	public Object getProperties(String stringProperties) {
		if (null != stringProperties) {
			stringProperties = stringProperties.trim();
			String classProperties;
			String[] arrayObject;
			String objectEntry;
			Object objectKey;
			Object objectValue;
			int index;
			if (stringProperties.startsWith(tag_bracket_open_shift)
					&& stringProperties.endsWith(tag_bracket_close_shift)) {
				Properties prop = new Properties();
				classProperties = stringProperties.substring(1, stringProperties.length() - 1);
				arrayObject = classProperties.split(tag_split);// (, )
				if (null != arrayObject) {
					for (int i = 0; i < arrayObject.length; i++) {
						objectEntry = arrayObject[i];
						if (null != objectEntry && objectEntry.contains(tag_equals)) {
							index = objectEntry.indexOf(tag_equals);
							objectKey = objectEntry.substring(0, index);
							objectValue = getObject(objectEntry.substring(index + 1));
							if (null != objectKey && null != objectValue) {
								prop.put(objectKey, objectValue);
							}
						}
					}
					return prop;
				}
			}
		}
		return null;
	}
}
