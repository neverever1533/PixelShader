package javaev.util;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtils {
	private Properties propertiesLocal;

	public PropertiesUtils() {
	}

	public PropertiesUtils(Properties properties) {
		propertiesLocal = properties;
	}

	public Properties getProperties() {
		return propertiesLocal;
	}

	public Properties getPropertiesString() {
		return getPropertiesString(getProperties());
	}

	public Properties getPropertiesString(Properties properties) {
		if (null != properties) {
			Properties prop = new Properties();
			Entry<Object, Object> entry;
			Object key;
			Object value;
			Set<Entry<Object, Object>> eSet = properties.entrySet();
			for (Iterator<Entry<Object, Object>> iterator = eSet.iterator(); iterator.hasNext();) {
				entry = iterator.next();
				if (null != entry) {
					key = entry.getKey();
					value = entry.getValue();
					if (null != key && null != value) {
						prop.put(key.toString(), value.toString());
					}
				}
			}
			return prop;
		}
		return null;
	}

	public void setProperties(Properties properties) {
		propertiesLocal = properties;
	}
}
