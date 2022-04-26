package javaev.lang;

import java.awt.Dimension;
import java.awt.Point;

import java.util.Properties;

public class ObjectUtils {
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

	public Object getObject(String stringObject) {
		if (null != stringObject) {
			stringObject = stringObject.trim();
			String symbolPrefixArray = "[";
			String symbolSuffixArray = "]";
			String symbolClass = "java";
			String symbolEquals = "=";
			String symbolComma = ",";
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
			if (stringObject.startsWith(symbolClass)) {
				if (stringObject.contains(symbolPrefixArray) && stringObject.contains(symbolSuffixArray)
						&& stringObject.contains(symbolComma)) {
					indexPrefix = stringObject.indexOf(symbolPrefixArray);
					indexSuffix = stringObject.indexOf(symbolSuffixArray);
					classType = stringObject.substring(0, indexPrefix);
					classProperties = stringObject.substring(indexPrefix + 1, indexSuffix);
					array = classProperties.split(symbolComma);// ,
					key = array[0];
					value = array[1];
					if (key.contains(symbolEquals)) {
						if (key.startsWith("width")) {
							w = Integer.valueOf(key.substring(6));
						} else if (key.startsWith("x")) {
							x = Integer.valueOf(key.substring(2));
						}
					}
					if (value.contains(symbolEquals)) {
						if (value.startsWith("height")) {
							h = Integer.valueOf(value.substring(7));
						} else if (value.startsWith("y")) {
							y = Integer.valueOf(value.substring(2));
						}
					}
					if (w != -1 && h != -1 && classType.endsWith("Dimension")) {
						Dimension size = new Dimension();
						size.setSize(w, h);
						return size;
					} else if (x != -1 && y != -1 && classType.endsWith("Point")) {
						Point location = new Point();
						location.setLocation(x, y);
						return location;
					}
				}
			} else {
				String regex = "-*\\d+\\.*\\d*";
				if (stringObject.equalsIgnoreCase("true") || stringObject.equalsIgnoreCase("false")) {
					return Boolean.valueOf(stringObject);
				} else if (stringObject.matches(regex)) {
					return Double.valueOf(stringObject);
				}
			}

		}
		return stringObject;
	}

	public Object getProperties(String stringProperties) {
		if (null != stringProperties) {
			stringProperties = stringProperties.trim();
			String symbolPrefixProperties = "{";
			String symbolSuffixProperties = "}";
			String symbolSplitObject = ", ";
			String symbolEqualsObject = "=";
			String classProperties;
			String[] arrayObject;
			String objectEntry;
			Object objectKey;
			Object objectValue;
			int index;
			if (stringProperties.startsWith(symbolPrefixProperties)
					&& stringProperties.endsWith(symbolSuffixProperties)) {
				Properties prop = new Properties();
				classProperties = stringProperties.substring(1, stringProperties.length() - 1);
				arrayObject = classProperties.split(symbolSplitObject);// (, )
				if (null != arrayObject) {
					for (int i = 0; i < arrayObject.length; i++) {
						objectEntry = arrayObject[i];
						if (null != objectEntry && objectEntry.contains(symbolEqualsObject)) {
							index = objectEntry.indexOf(symbolEqualsObject);
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
