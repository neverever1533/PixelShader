package javaev.awt;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;

import java.io.File;

import java.util.Properties;
import java.util.Vector;

import javaev.imageio.ImageIOUtils;

public class BufferedImageUtils {

	public static boolean hasAlpha(BufferedImage image) {
		if (null != image) {
			return image.getColorModel().hasAlpha();
		}
		return false;
	}

	Graphics2DUtils g2dUtils = new Graphics2DUtils();

	ImageIOUtils imageIOUtils = new ImageIOUtils();

	public BufferedImage getOpaqueImage(BufferedImage image, Color color) {
		if (hasAlpha(image)) {
			int w = image.getWidth();
			int h = image.getHeight();
			BufferedImage rootImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
			return g2dUtils.clipImage(rootImage, image, 0, 0, -1, -1, color, null);
		}
		return image;
	}

	public BufferedImage getOpaqueImage(File file, Color color) {
		return getOpaqueImage(imageIOUtils.read(file), color);
	}

	public Properties getProperties(BufferedImage image) {
		Properties prop = null;
		if (null != image) {
			prop = new Properties();
			prop.put("AccelerationPriority", image.getAccelerationPriority());
			WritableRaster alphaRaster = image.getAlphaRaster();
			if (null != alphaRaster) {
				prop.put("AlphaRaster", image.getAlphaRaster());
			}
			prop.put("ColorModel", image.getColorModel());
			prop.put("Data", image.getData());
			prop.put("Graphics", image.getGraphics());
			prop.put("Height", image.getHeight());
			prop.put("MinTileX", image.getMinTileX());
			prop.put("MinTileY", image.getMinTileY());
			prop.put("MinX", image.getMinX());
			prop.put("MinY", image.getMinY());
			prop.put("NumXTiles", image.getNumXTiles());
			prop.put("NumYTiles", image.getNumYTiles());
			prop.put("Raster", image.getRaster());
			prop.put("SampleModel", image.getSampleModel());
			prop.put("Source", image.getSource());
			Vector<RenderedImage> sources = image.getSources();
			if (null != sources) {
				prop.put("Sources", sources);
			}
			prop.put("TileGridXOffset", image.getTileGridXOffset());
			prop.put("TileGridYOffset", image.getTileGridYOffset());
			prop.put("TileHeight", image.getTileHeight());
			prop.put("TileWidth", image.getTileWidth());
			prop.put("Transparency", image.getTransparency());
			prop.put("Type", image.getType());
			prop.put("Width", image.getWidth());
			prop.put("HasTileWriters", image.hasTileWriters());
			prop.put("IsAlphaPremultiplied", image.isAlphaPremultiplied());
		}
		return prop;
	}

	public Properties getProperties(File file) {
		return getProperties(imageIOUtils.read(file));
	}

	public BufferedImage getSubimage(BufferedImage image, int x, int y, int width, int height, Color bgcolor,
			ImageObserver observer) {
		if (null != image) {
			return g2dUtils.clipImage(null, image, x, y, width, height, bgcolor, observer);
		}
		return image;
	}

	public BufferedImage getSubimage(BufferedImage image, Rectangle rectangle) {
		if (null != image && null != rectangle) {
			int width = image.getWidth();
			int height = image.getHeight();
			int x = rectangle.x;
			int y = rectangle.y;
			int w = rectangle.width;
			int h = rectangle.height;
			if (w > 0 && h > 0 && x + w <= width && y + h <= height) {
				return image.getSubimage(x, y, w, h);
			}
		}
		return null;
	}

	public BufferedImage[] getSubimageArray(BufferedImage image, Rectangle[] arr) {
		if (null == image || null == arr) {
			return null;
		}
		int len = arr.length;
		BufferedImage[] temp = new BufferedImage[len];
		for (int i = 0, ilength = len; i < ilength; i++) {
			temp[i] = getSubimage(image, arr[i]);
		}
		return temp;
	}

}
