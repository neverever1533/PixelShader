package javaev.io;

import java.io.File;

import java.net.FileNameMap;

import java.util.Properties;

public class FileNameMapUtils implements FileNameMap {

	private static String ContentTypeUnknown = "application/octet-stream";

	public static String getContentTypeForFileNameSuffix(String suffix) {
		if(null == suffix) {
			return null;
		}
		String contentType = getContentTypePropertiesDefault().getProperty(suffix.toLowerCase());
		if (null == contentType) {
			contentType = ContentTypeUnknown;
		}
		return contentType;
	}

	private static Properties getContentTypeProperties() {
		Properties prop = new Properties();
		prop.put(".bmp", "image/bmp");
		prop.put(".png", "image/png");
		prop.put(".wbmp", "image/wbmp");
		prop.put(".datauri", "image/base64");
		prop.put(".csv", "text/csv");
		prop.put(".ini", "text/setting");
		prop.put(".xml", "text/xml");
		prop.put(".js", "text/javascrpit");
		prop.put(".css", "text/css");
		prop.put(".pdf", "document/pdf");
		prop.put(".rtf", "document/rtf");
		prop.put(".doc", "document/word 97-2003");
		prop.put(".docx", "document/word");
		prop.put(".ppt", "document/powerpoint 97-2003");
		prop.put(".pptx", "document/powerpoint");
		prop.put(".xls", "document/excel 97-2003");
		prop.put(".xlsx", "document/excel");
		prop.put(".mp4", "video/mpeg");
		prop.put(".mp3", "audio/mpeg");
		prop.put(".flac", "audio/flac");
		prop.put(".ape", "audio/ape");
		prop.put(".ogg", "audio/ogg");
		prop.put(".m4a", "audio/apple");
		prop.put(".aac", "audio/apple");
		return prop;
	}

	public static Properties getContentTypePropertiesDefault() {
		return getContentTypeProperties();
	}

	private FileUtils fileUtils = FileUtils.getInstance();

	private int type_audio = 1;
	private int type_document = 2;
	private int type_image = 3;
	private int type_text = 4;
	private int type_video = 5;

	public String getContentTypeFor(File file) {
		return getContentTypeForFileNameSuffix(fileUtils.getFileNameSuffix(file));
	}

	@Override
	public String getContentTypeFor(String fileName) {
		if (null == fileName) {
			return null;
		}
		return getContentTypeFor(new File(fileName));
	}

	public boolean isFileAudio(File file) {
		return isTypeFile(getContentTypeFor(file), type_audio);
	}

	public boolean isFileAudio(String filePath) {
		return isTypeFile(getContentTypeFor(filePath), type_audio);
	}

	public boolean isFileDocument(File file) {
		return isTypeFile(getContentTypeFor(file), type_document);
	}

	public boolean isFileDocument(String filePath) {
		return isTypeFile(getContentTypeFor(filePath), type_document);
	}

	public boolean isFileImage(File file) {
		return isTypeFile(getContentTypeFor(file), type_image);
	}

	public boolean isFileImage(String filePath) {
		return isTypeFile(getContentTypeFor(filePath), type_image);
	}

	public boolean isFileText(File file) {
		return isTypeFile(getContentTypeFor(file), type_text);
	}

	public boolean isFileText(String filePath) {
		return isTypeFile(getContentTypeFor(filePath), type_text);
	}

	private boolean isTypeFile(String typeName, int typeStyle) {
		if (null != typeName) {
			String type = typeName.toLowerCase();
			if (typeStyle == type_image) {
				if (type.startsWith("image")) {
					return true;
				}
			} else if (typeStyle == type_text) {
				if (type.startsWith("text")) {
					return true;
				}
			} else if (typeStyle == type_document) {
				if (type.startsWith("document")) {
					return true;
				}
			} else if (typeStyle == type_audio) {
				if (type.startsWith("audio")) {
					return true;
				}
			} else if (typeStyle == type_video) {
				if (type.startsWith("video")) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isVideoFile(File file) {
		return isTypeFile(getContentTypeFor(file), type_video);
	}

	public boolean isVideoFile(String filePath) {
		return isTypeFile(getContentTypeFor(filePath), type_video);
	}

}
