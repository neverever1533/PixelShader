package javaev.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import java.nio.charset.Charset;

import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class FileUtils {
	private static FileUtils instance;

	public static FileUtils getInstance() {
		if (null == instance) {
			syncInit();
		}
		return instance;
	}

	private static synchronized void syncInit() {
		if (null == instance) {
			instance = new FileUtils();
		}
	}

	private String[] charsetArray;
	private String[] suffixArray;

	private String suffixProperties = "xml";

	private FileUtils() {
	}

	public String[] getCharsetArray() {
		Set<String> es = Charset.availableCharsets().keySet();
		String[] array = new String[es.size()];
		es.toArray(array);
		charsetArray = array;
		return charsetArray;
	}

	public FileFilter getFileFilter() {
		return getFileFilter(getSuffixArray());
	}

	public FileFilter getFileFilter(final String[] suffixArray) {
		FileFilter fileFilter = new FileFilter() {

			public boolean accept(File pathname) {
				String fname = pathname.getName().toLowerCase();
				String temp;
				for (int i = 0, ilength = suffixArray.length; i < ilength; i++) {
					temp = suffixArray[i];
					if (pathname.isDirectory() || (null != temp && fname.endsWith(temp))) {
						return true;
					}
				}
				return false;
			}
		};
		return fileFilter;
	}

	private String getFilename(File file, boolean isSuffix) {
		String filename = file.getName();
		int ids = filename.lastIndexOf(".");
		if (isSuffix) {
			if (ids != -1) {
				return filename.substring(ids);
			}
		} else {
			if (ids != -1) {
				return filename.substring(0, ids);
			} else {
				return filename;
			}
		}
		return null;
	}

	public String[] getFilenameArray(File[] fileArray) {
		return toStringArray(fileArray, true);
	}

	public FilenameFilter getFilenameFilter() {
		return getFilenameFilter(getSuffixArray());
	}

	public FilenameFilter getFilenameFilter(final String[] suffixArray) {
		FilenameFilter fileFilter = new FilenameFilter() {

			public boolean accept(File dir, String name) {
				String fname = name.toLowerCase();
				File file = new File(dir, name);
				String temp;
				for (int i = 0, ilength = suffixArray.length; i < ilength; i++) {
					temp = suffixArray[i];
					if (file.isDirectory() || (null != temp && fname.endsWith(temp))) {
						return true;
					}
				}
				return false;
			}
		};
		return fileFilter;
	}

	public String getFilenamePrefix(File file) {
		return getFilename(file, false);
	}

	public String getFilenameSuffix(File file) {
		return getFilename(file, true);
	}

	public String[] getFilePathArray(File[] fileArray) {
		return toStringArray(fileArray, false);
	}

	public String[] getSuffixArray() {
		return suffixArray;
	}

	public String getSuffixProperties() {
		return suffixProperties;
	}

	public boolean isDirectoryArrayOnly(File[] fileArray) {
		return checkFileArray(fileArray, true);
	}

	public boolean isFileArrayOnly(File[] fileArray) {
		return checkFileArray(fileArray, false);
	}

	private boolean checkFileArray(File[] fileArray, boolean isDirectoryOnly) {
		if (null == fileArray) {
			return false;
		}
		File file;
		for (int i = 0; i < fileArray.length; i++) {
			file = fileArray[i];
			if (isDirectoryOnly) {
				if (!file.isDirectory()) {
					return false;
				}
			} else {
				if (!file.isFile()) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isFileProperties(File file) {
		String name = file.getName();
		if (name.toLowerCase().endsWith(suffixProperties)) {
			return true;
		}
		return false;
	}

	public boolean isFileProperties(File file, String suffix) {
		if (suffix.equalsIgnoreCase(suffixProperties)) {
			return isFileProperties(file);
		}
		return false;
	}

	public Properties loadProperties(File file) {
		if (isFileProperties(file)) {
			Properties properties = new Properties();
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(file);
				properties.loadFromXML(fileInputStream);
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (null != fileInputStream) {
					try {
						fileInputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return properties;
		}
		return null;
	}

	public List<File> toList(File[] fileArray) {
		if (null != fileArray) {
			List<File> list = new LinkedList<File>();
			File file;
			for (int i = 0; i < fileArray.length; i++) {
				file = fileArray[i];
				list.add(file);
			}
			return list;
		}
		return null;
	}

	public void setSuffixArray(String[] array) {
		suffixArray = array;
	}

	public void setSuffixProperties(String suffix) {
		suffixProperties = suffix;
	}

	public void storeProperties(File file, Properties properties, String encoding) {
		FileOutputStream fileOutputStream = null;
		try {
			if (!file.exists()) {
				File directory = file.getParentFile();
				if (!directory.exists()) {
					directory.mkdirs();
				}
				file.createNewFile();
			}
			fileOutputStream = new FileOutputStream(file);
			properties.storeToXML(fileOutputStream, encoding);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fileOutputStream) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String[] toStringArray(File[] fileArray, boolean isFilename) {
		int len = fileArray.length;
		String[] array = new String[len];
		for (int i = 0, iLength = len; i < iLength; i++) {
			File file = fileArray[i];
			if (isFilename) {
				array[i] = file.getName();
			} else {
				array[i] = file.getAbsolutePath();
			}
		}
		return array;
	}

}
