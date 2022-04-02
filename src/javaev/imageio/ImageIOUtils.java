package javaev.imageio;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javaev.io.FileNameMapUtils;
import javaev.io.FileUtils;

import javaev.util.ArraysUtils;

public class ImageIOUtils {

	private FileNameMapUtils fileNameMapUtils = new FileNameMapUtils();

	private FileUtils fileUtils = FileUtils.getInstance();

	public BufferedImage read(File file) {
		if (file.isAbsolute()) {
			if (fileNameMapUtils.isFileImage(file)) {
				BufferedImage image = null;
				try {
					image = ImageIO.read(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return image;
			}
		}
		return null;
	}

	public BufferedImage[] read(File[] fileArray) {
		if (null != fileArray) {
			int len = fileArray.length;
			if(len != 0) {
				BufferedImage[] temp = new BufferedImage[len];
				for (int i = 0, ilength = len; i < ilength; i++) {
					temp[i] = read(fileArray[i]);
				}
				return temp;
			}
		}
		return null;
	}

	public BufferedImage write(File file, BufferedImage image) {
		if (file.isAbsolute() && null != image) {
			if (fileNameMapUtils.isFileImage(file)) {
				String suffix = fileUtils.getFileNameSuffix(file);
				if (null != suffix) {
					String formatName = suffix.substring(1);
					try {
						ImageIO.write(image, formatName, file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return image;
	}

	public void write(File file, BufferedImage[] bufferedImageArray) {
		if (null != bufferedImageArray && file.isAbsolute()) {
			File dir;
			if (!file.isDirectory()) {
				dir = file.getParentFile();
				if (null != dir) {
					dir = new File(dir, "Export");
				}
			} else {
				dir = file;
			}
			if (null == dir) {
				return;
			}
			for (int i = 0, ilength = bufferedImageArray.length; i < ilength; i++) {
				write(new File(dir, ArraysUtils.toString(i)), bufferedImageArray[i]);
			}
		}
	}

}
