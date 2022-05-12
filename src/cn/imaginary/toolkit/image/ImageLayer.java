package cn.imaginary.toolkit.image;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;

import java.io.File;

import java.util.Properties;

import javaev.imageio.ImageIOUtils;

import javaev.lang.ObjectUtils;

public class ImageLayer {
	private float alphaLayer = 1.0f;

	private Point anchorLayer;

	private int depthLayer = -1;

	private double gravityLayer;

	private ImageIOUtils imageIOUtils = new ImageIOUtils();

	private BufferedImage imageLayer;

	private String imagePathLayer;

	private boolean isAlphaLayer;
	private boolean isGravityLayer;
	private boolean isVisibleLayer;

	private Point locationLayer;

	private ObjectUtils objectUtils = new ObjectUtils();

	private Point rootLayer;

	private double rotateLayer;

	private Dimension scaleLayer;
	private Dimension sizeLayer;

	public float getAlpha() {
		return alphaLayer;
	}

	public Point getAnchor() {
		return anchorLayer;
	}

	public int getDepth() {
		return depthLayer;
	}

	public double getGravity() {
		return gravityLayer;
	}

	public BufferedImage getImage() {
		return imageLayer;
	}

	public String getImagePath() {
		return imagePathLayer;
	}

	public Point getLocation() {
		return locationLayer;
	}

	public Properties getProperties() {
		Properties properties = new Properties();
		properties.put("alpha", getAlpha());
		Point anchor = getAnchor();
		if (null != anchor) {
			properties.put("anchor", anchor);
		}
		properties.put("depth", getDepth());
		properties.put("gravity", getGravity());
		String imagePath = getImagePath();
		if (null != imagePath) {
			properties.put("imagePath", imagePath);
		}
		Point location = getLocation();
		if (null != location) {
			properties.put("location", location);
		}
		properties.put("rotated", getRotated());
		Dimension scale = getScale();
		if (null != scale) {
			properties.put("scale", scale);
		}
		Dimension size = getSize();
		if (null != size) {
			properties.put("size", size);
		}
		Point root = getRoot();
		if (null != root) {
			properties.put("root", root);
		}
		properties.put("isAlpha", isAlpha());
		properties.put("isGravity", isGravity());
		properties.put("isVisible", isVisible());
		return properties;
	}

	public Point getRoot() {
		return rootLayer;
	}

	public double getRotated() {
		return rotateLayer;
	}

	public Dimension getScale() {
		return scaleLayer;
	}

	public Dimension getSize() {
		return sizeLayer;
	}

	public boolean isAlpha() {
		return isAlphaLayer;
	}

	public void isAlpha(boolean isAlpha) {
		isAlphaLayer = isAlpha;
	}

	public boolean isGravity() {
		return isGravityLayer;
	}

	public void isGravity(boolean isGravity) {
		if (isGravityLayer != isGravity) {
			isGravityLayer = isGravity;
		}
	}

	public boolean isVisible() {
		return isVisibleLayer;
	}

	public void isVisible(boolean isVisible) {
		if (isVisibleLayer != isVisible) {
			isVisibleLayer = isVisible;
		}
	}

	public void read(File file) {
		if (null != file) {
			BufferedImage image = imageIOUtils.read(file);
			if (null != image) {
				setImage(image);
				setImagePath(file.getPath());
			}
		}
	}

	public void read(String filePath) {
		if (null != filePath) {
			read(new File(filePath));
		}
	}

	public void scale(Dimension scale) {
		scaleLayer = scale;
	}

	public void scale(double scaleWidth, double scaleHeight) {
		Dimension dimension = new Dimension();
		dimension.setSize(scaleWidth, scaleHeight);
		scale(dimension);
	}

	public void setAlpha(float alpha) {
		if (alpha < 0 || alpha > 1) {
			return;
		}
		alphaLayer = alpha;
		if (alphaLayer == 1) {
			isAlpha(false);
		} else {
			isAlpha(true);
		}
	}

	public void setAnchor(double ax, double ay) {
		Point point = new Point();
		point.setLocation(ax, ay);
		setAnchor(point);
	}

	public void setAnchor(Point anchor) {
		anchorLayer = anchor;
	}

	public void setDepth(int depth) {
		depthLayer = depth;
	}

	public void setGravity(double angle) {
		gravityLayer = angle % 360;
	}

	public void setImage(BufferedImage image) {
		if (null != image) {
			imageLayer = image;
			isVisible(true);
			setLocation(0, 0);
			setSize(image.getWidth(), image.getHeight());
		} else {
			isVisible(false);
			setLocation(null);
			setSize(null);
		}
		isAlpha(false);
	}

	public void setImagePath(String filePath) {
		imagePathLayer = filePath;
	}

	public void setLocation(double tx, double ty) {
		Point point = new Point();
		point.setLocation(tx, ty);
		setLocation(point);
	}

	public void setLocation(Point location) {
		locationLayer = location;
	}

	public void setProperties(Properties properties) {
		if (null != properties) {
			Object object;
			String value;
			value = properties.getProperty("imagePath");
			if (null != value) {
				read(value);
			}
			value = properties.getProperty("size");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Dimension) {
					setSize((Dimension) object);
				}
			}
			value = properties.getProperty("alpha");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Double) {
					setAlpha(Float.valueOf(value));
				}
			}
			value = properties.getProperty("anchor");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Point) {
					setAnchor((Point) object);
				}
			}
			value = properties.getProperty("gravity");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Double) {
					setGravity((double) object);
				}
			}
			value = properties.getProperty("isAlpha");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Boolean) {
					isAlpha((boolean) object);
				}
			}
			value = properties.getProperty("isGravity");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Boolean) {
					isGravity((boolean) object);
				}
			}
			value = properties.getProperty("isVisible");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Boolean) {
					isVisible((boolean) object);
				}
			}
			value = properties.getProperty("depth");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Double) {
					setDepth(Integer.valueOf(value));
				}
			}
			value = properties.getProperty("location");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Point) {
					setLocation((Point) object);
				}
			}
			value = properties.getProperty("root");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Point) {
					setRoot((Point) object);
				}
			}
			value = properties.getProperty("rotated");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Double) {
					setRotated((double) object);
				}
			}
			value = properties.getProperty("scale");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Dimension) {
					scale((Dimension) object);
				}
			}
		}
	}

	public void setRoot(double rx, double ry) {
		Point point = new Point();
		point.setLocation(rx, ry);
		setRoot(point);
	}

	public void setRoot(Point root) {
		rootLayer = root;
	}

	public void setRotated(double angle) {
		angle = angle % 360;
		rotateLayer = angle;
	}

	public void setSize(Dimension dimension) {
		if (null != dimension) {
			double scaleWidth;
			double scaleHeight;
			if (null != sizeLayer) {
				scaleWidth = dimension.getWidth() / sizeLayer.getWidth();
				scaleHeight = dimension.getHeight() / sizeLayer.getHeight();
			} else {
				scaleWidth = 1;
				scaleHeight = 1;
			}
			scale(scaleWidth, scaleHeight);
			double x = dimension.getWidth() / 2;
			double y = dimension.getHeight() / 2;
			setAnchor(x, y);
			setRoot(x, y);
		} else {
			setAnchor(null);
			setRoot(null);
		}
		sizeLayer = dimension;
	}

	public void setSize(double width, double height) {
		Dimension dimension = new Dimension();
		dimension.setSize(width, height);
		setSize(dimension);
	}
}
