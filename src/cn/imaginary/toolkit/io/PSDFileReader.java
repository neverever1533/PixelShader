package cn.imaginary.toolkit.io;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cn.imaginary.toolkit.image.ImageLayer;

import javaev.imageio.ImageIOUtils;

import javaev.io.FileUtils;

import psd.model.Layer;
import psd.model.Psd;

public class PSDFileReader {
	private FileUtils fileUtils = FileUtils.getInstance();

	/*public static void main(String[] args) {
		PSDFileReader pfr = new PSDFileReader();
		ArrayList<ImageLayer> aList = new ArrayList<>();
		String fp = "e:\\Downloads\\Models\\Evolution Vector Model\\未标题-1.psd";
		File f = new File(fp);
		pfr.read(f, aList);
		ImageLayer layer;
		for (Iterator<ImageLayer> iterator = aList.iterator(); iterator.hasNext();) {
			layer = (ImageLayer) iterator.next();
			if (null != layer) {
				System.out.println(layer.getProperties());
			}
		}
	}*/

	public List<ImageLayer> read(File file, List<ImageLayer> layerList) {
		if (null != file && null != layerList) {
			try {
				Psd p = new Psd(file);
				int lc = p.getLayersCount();
				Layer layer;
				BufferedImage image;
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
						alpha = layer.getAlpha() / 255;
						fileTemp = fileUtils.getFile(file, i, ImageIOUtils.FileSuffixes_Default);
						imageLayer = new ImageLayer();
						imageLayer.setImage(image);
						imageLayer.setImagePath(fileTemp.getPath());
						imageLayer.setAlpha(alpha);
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
