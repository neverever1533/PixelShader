package cn.imaginary.toolkit.io;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.Collections;
import java.util.List;

import cn.imaginary.toolkit.image.ImageLayer;

import javaev.imageio.ImageIOUtils;

import javaev.io.FileUtils;

import psd.model.Layer;
import psd.model.Psd;

public class PSDFileReader {
	private FileUtils fileUtils = FileUtils.getInstance();

	public List<ImageLayer> read(File file, List<ImageLayer> layerList) {
		if (null != file && null != layerList) {
			try {
				Psd psd = new Psd(file);
				int layersCount = psd.getLayersCount();
				Layer layer;
				BufferedImage image;
				File fileTemp;
				float alpha;
				int x;
				int y;
				boolean isVisible;
				String name;
				String suffix;
				ImageLayer imageLayer;
//				for (int i = 0; i < layersCount; i++) {
				for (int i = layersCount - 1; i >= 0; i--) {
					layer = psd.getLayer(i);
					if (null != layer) {
						image = layer.getImage();
						x = layer.getX();
						y = layer.getY();
						alpha = layer.getAlpha() / 255;
						isVisible = layer.isVisible();
						name = layer.toString();
						if (name.isEmpty() || null == name) {
							name = String.valueOf(i);
						}
						suffix = ImageIOUtils.FileSuffixes_Default;
						fileTemp = fileUtils.getFileReplace(file, name, suffix);
						imageLayer = new ImageLayer();
						imageLayer.setName(name);
						imageLayer.setSuffix(suffix);
						imageLayer.setImage(image);
						imageLayer.setImagePath(fileTemp.getPath());
						imageLayer.setAlpha(alpha);
						imageLayer.isVisible(isVisible);
						imageLayer.setDepth(i);
						imageLayer.setLocation(x, y);
						imageLayer.setRotated(0);
						layerList.add(imageLayer);
					}
				}
				Collections.reverse(layerList);
				return layerList;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
