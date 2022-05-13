package javaev.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.nio.charset.Charset;

import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtils {
	public static String encoding_default = "utf-8";
	public static String suffix_properties_xml = ".xml";

	private String encoding_properties;

	private Properties propertiesLocal;

	private String suffix_properties;

	public PropertiesUtils() {
	}

	public PropertiesUtils(Properties properties) {
		propertiesLocal = properties;
	}

	private String getEcoding() {
		if (null == encoding_properties) {
			encoding_properties = encoding_default;
		}
		return encoding_properties;
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

	public String getSuffixProperties() {
		return suffix_properties;
	}

	public boolean isFileProperties(File file) {
		String name = file.getName().toLowerCase();
		if (name.endsWith(suffix_properties)) {
			return true;
		}
		return false;
	}

	public Properties loadProperties(File file, boolean isTypeXml) {
		if (null != file) {
			InputStream stream;
			try {
				stream = new FileInputStream(file);
				if (isFileProperties(file)) {
					return loadProperties(stream, isTypeXml);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public Properties loadProperties(InputStream stream, boolean isTypeXml) {
		if (null != stream) {
			Properties properties = new Properties();
			try {
				if (isTypeXml) {
					properties.loadFromXML(stream);
				} else {
					properties.load(stream);
				}
				stream.close();
				return properties;
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public Properties loadPropertiesXml(File file) {
		return loadProperties(file, true);
	}

	private void setEcoding(String encoding) {
		if (null != encoding && Charset.isSupported(encoding)) {
			encoding_properties = encoding;
		}
	}

	public void setProperties(Properties properties) {
		propertiesLocal = properties;
	}

	public void setSuffixProperties(String suffix) {
		if (null != suffix) {
			suffix_properties = suffix.toLowerCase();
		} else {
			suffix_properties = suffix_properties_xml;
		}
	}

	public void storeProperties(boolean isTypeXml, File file, String comment, String encoding) {
		storeProperties(propertiesLocal, isTypeXml, file, comment, encoding);
	}

	public void storeProperties(Properties properties, boolean isTypeXml, File file, String comment, String encoding) {
		if (null != file) {
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				if (isFileProperties(file)) {
					storeProperties(propertiesLocal, isTypeXml, new FileOutputStream(file), comment, encoding);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void storeProperties(Properties properties, boolean isTypeXml, OutputStream stream, String comment,
			String encoding) {
		if (null != properties) {
			setEcoding(encoding);
			encoding = getEcoding();
			try {
				if (isTypeXml) {
					properties.storeToXML(stream, comment, encoding);
				} else {
					properties.store(stream, comment);
				}
				stream.flush();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void storePropertiesXml(File file, String comment, String encoding) {
		storeProperties(true, file, comment, encoding);
	}
}
