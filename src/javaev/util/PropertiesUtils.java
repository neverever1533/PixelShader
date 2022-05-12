package javaev.util;

import java.io.IOException;
import java.io.OutputStream;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtils {
	private Properties propertiesLocal;

	private String xmlEncoding;

	public String xmlEncodingDefault = "utf-8";

	public PropertiesUtils() {
	}

	public PropertiesUtils(Properties properties) {
		propertiesLocal = properties;
	}

	public Properties getProperties() {
		return propertiesLocal;
	}

	public Properties getPropertiesObject(Properties properties) {
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
						if (!(value instanceof Number && value instanceof Boolean && value instanceof Character)) {
							value = (Object) value;
						}
						prop.put(key.toString(), value);
					}
				}
			}
			return prop;
		}
		return null;
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

	private String getXmlEcoding() {
		return xmlEncoding;
	}

	public void setProperties(Properties properties) {
		propertiesLocal = properties;
	}

	private void setXmlEcoding(String encoding) {
		xmlEncoding = encoding;
	}

	public void storeToXML(OutputStream os, String comment, String encoding) {
		if (null != encoding) {
			setXmlEcoding(encoding);
		}
		String xmlEncoding = getXmlEcoding();
		if (null != propertiesLocal) {
			try {
				propertiesLocal.storeToXML(os, comment, xmlEncoding);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
