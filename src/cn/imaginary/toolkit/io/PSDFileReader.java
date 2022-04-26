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
				Psd p = new Psd(file);
				int lc = p.getLayersCount();
				Layer layer;
				BufferedImage image;
				int index = 0;
				File fileTemp;
				float alpha;
				int x;
				int y;
				ImageLayer imageLayer;
				for (int i = 0; i < lc; i++) {
					layer = p.getLayer(i);
					if (null != layer) {
						image = layer.getImage();
						x = layer.getX();
						y = layer.getY();
//						System.out.println(x);
//						System.out.println(y);
						alpha = layer.getAlpha() / 255;
						fileTemp = fileUtils.getFile(file, index++, ImageIOUtils.FileSuffixes_Default);
						imageLayer = new ImageLayer();
						imageLayer.setImage(image);
						imageLayer.setImagePath(fileTemp.getPath());
						imageLayer.setLocation(x, y);
						imageLayer.setAlpha(alpha);
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
