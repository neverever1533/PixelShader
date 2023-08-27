package cn.imaginary.utils;

import java.awt.Component;

import java.io.File;

import java.util.List;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javaev.io.FileUtilsAlpha;

public class PixelShader {

	private static PixelShader instance;

	public static PixelShader getInstance() {
		if (null == instance) {
			syncInit();
		}
		return instance;
	}

	private static synchronized void syncInit() {
		if (null == instance) {
			instance = new PixelShader();
		}
	}

	private String encodingDefault = "utf-8";

	private File[] fileArrayResources;

	private File directoryResources;
	private File fileLanguage;
	private File fileResources;
	private File fileSettings;

	private FileUtilsAlpha fileUtils = FileUtilsAlpha.getInstance();

	private JFileChooser jfChooser;

	private Properties propertiesLanguage;
	private Properties propertiesResources;
	private Properties propertiesSettings;

	private String[] suffixArray;
	private String[] suffixArrayDefault = { "gif", "tiff", "bmp", "jpg", "png", "jpeg" };

	private String suffixProperties = "xml";

	private PixelShader() {
	}

	public File getDirectoryResources() {
		return directoryResources;
	}

	public File[] getfileArrayResources() {
		return fileArrayResources;
	}

	public String[] getfilenameArrayResources() {
		return fileUtils.getFileNameArray(getfileArrayResources());
	}

	public String[] getfilePathArrayResources() {
		return fileUtils.getFilePathArray(getfileArrayResources());
	}

	public Properties getPropertiesLanguage() {
		return propertiesLanguage;
	}

	public Properties getPropertiesResources() {
		return propertiesResources;
	}

	public Properties getPropertiesSettings() {
		return propertiesSettings;
	}

	public String[] getSuffixArray() {
		if (null == suffixArray || suffixArray.length == 0) {
			suffixArray = suffixArrayDefault;
		}
		return suffixArray;
	}

	public boolean isFileProperties(File file) {
		String name = file.getName();
		if (name.equalsIgnoreCase(fileResources.getName()) || name.toLowerCase().endsWith(suffixProperties)) {
			return true;
		}
		return false;
	}

	private Properties loadLanguage(File file) {
		if (!file.exists()) {
			storeLanguage(file);
		}
		return fileUtils.loadProperties(file);
	}

	private Properties loadSettings(File file) {
		if (!file.exists()) {
			storeSettings(file);
		}
		return fileUtils.loadProperties(file);
	}

	public File[] openFiles(Component parent) {
		if (null == jfChooser) {
			jfChooser = new JFileChooser();
		}
		jfChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		String description = "文件 & 文件夹";
		String[] extensions = getSuffixArray();
		FileNameExtensionFilter fneFilter = new FileNameExtensionFilter(description, extensions);
		jfChooser.setFileFilter(fneFilter);
		int returnVal = jfChooser.showOpenDialog(parent);
//		if (returnVal == JFileChooser.APPROVE_OPTION) {
//		}
		if (returnVal == JFileChooser.CANCEL_OPTION) {
			return null;
		}
		jfChooser.setMultiSelectionEnabled(true);
		File[] fileArray = jfChooser.getSelectedFiles();
		return fileArray;
	}

	public void setPropertiesLanguage(Properties properties) {
		propertiesLanguage = properties;
	}

	public void setPropertiesResources(Properties properties) {
		propertiesResources = properties;
	}

	public void setPropertiesSettings(Properties properties) {
		propertiesSettings = properties;
	}

	public void setSuffixArray(String[] array) {
		suffixArray = array;
	}

	private void storeLanguage(File file) {
		storeLanguage(file, getPropertiesLanguage());
	}

	private void storeLanguage(File file, Properties properties) {
		storeLanguage(file, properties, encodingDefault);
	}

	public void storeLanguage(File file, Properties properties, String encoding) {
		fileUtils.storeProperties(file, properties, encoding);
	}

	public void storeReaources(File file, Properties properties, String encoding) {
		fileUtils.storeProperties(file, properties, encoding);
	}

	private void storeSettings(File file) {
		storeSettings(file, getPropertiesSettings());
	}

	private void storeSettings(File file, Properties properties) {
		storeSettings(file, properties, encodingDefault);
	}

	public void storeSettings(File file, Properties properties, String encoding) {
		fileUtils.storeProperties(file, properties, encoding);
	}

	public List<File> toList(File[] fileArray) {
		return fileUtils.toList(fileArray);
	}

	public void updateUI() {
		File resourcesLocal = new File("LocalResources").getAbsoluteFile();
		File language = new File(resourcesLocal, "Locale");
		fileLanguage = new File(language, "PixelShader_zh_CN.xml");
		fileSettings = new File(resourcesLocal, "PixelShader_Settings.xml");
		fileResources = new File(resourcesLocal, "PixelShader_Resources.xml");
		loadSettings(fileSettings);
		loadLanguage(fileLanguage);
	}
}
