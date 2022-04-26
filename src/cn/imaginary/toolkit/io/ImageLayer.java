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

	private double angleLayer;

	private int depthLayer = -1;

	private ImageIOUtils imageIOUtils = new ImageIOUtils();

//	private FileNameMapUtils fileNameMapUtils = new FileNameMapUtils();

//	private File imageFileLayer;

	private BufferedImage imageLayer;

	private String imagePathLayer;

	private boolean isAlphaLayer;
	private boolean isVisibleLayer;

//	private List<File> layerChildren;
//
//	private File layerParent;

	private Point leafLayer;
	private Point locationLayer;

	private ObjectUtils objectUtils = new ObjectUtils();

	private Point rootLayer;

	private Dimension scaleLayer;
	private Dimension sizeLayer;

//	public void addChild(ImageLayer layer) {
//		if (null != layer) {
//			if (null == layerChildren) {
//				layerChildren = new LinkedList<>();
//			}
//			File fileChild = layer.getImageFile();
//			if (layer.getChildCount() == 0 && null == layer.getParent()) {
//				layer.setParent(this);
//				layerChildren.add(fileChild);
//			}
//		}
//	}

	public Dimension geScale() {
		return scaleLayer;
	}

	public float getAlpha() {
		return alphaLayer;
	}

	public Point getAnchor() {
		return anchorLayer;
	}

	public double getAngleRotated() {
		return angleLayer;
	}

//	public int getChildCount() {
//		if (null != layerChildren) {
//			return layerChildren.size();
//		} else {
//			return 0;
//		}
//	}

//	public List<File> getChildren() {
//		return layerChildren;
//	}

	public int getDepth() {
		return depthLayer;
	}

	public BufferedImage getImage() {
		return imageLayer;
	}

	public String getImagePath() {
		return imagePathLayer;
	}

//	public File getParent() {
//		return layerParent;
//	}

	public Point getLeaf() {
		return leafLayer;
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
		properties.put("angle", getAngleRotated());
		properties.put("depth", getDepth());
		String imagePath = getImagePath();
		if (null != imagePath) {
			properties.put("imagePath", imagePath);
		}
		properties.put("isVisible", isVisible());
		Point leaf = getLeaf();
		if (null != leaf) {
			properties.put("leaf", leaf);
		}
		Point location = getLocation();
		if (null != location) {
			properties.put("location", location);
		}
		Point root = getRoot();
		if (null != root) {
			properties.put("root", root);
		}
		Dimension scale = getScale();
		if (null != scale) {
			properties.put("scale", scale);
		}
		Dimension size = getSize();
		if (null != size) {
			properties.put("size", size);
		}
		return properties;
	}

	public Point getRoot() {
		return rootLayer;
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

//	public void removeChild(ImageLayer child) {
//		if (null != child && null != layerChildren) {
//			File fileChild = child.getImageFile();
//			if (null != fileChild) {
//				child.setParent(null);
//				layerChildren.remove(fileChild);
//			}
//		}
//	}

	public boolean isVisible() {
		return isVisibleLayer;
	}

	public void isVisible(boolean isVisible) {
		isVisibleLayer = isVisible;
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

	public void setAngleRotated(double angle) {
		angle = angle % 360;
		angleLayer = angle;
	}

	public void setDepth(int depth) {
		depthLayer = depth;
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

	public void setLeaf(double lx, double ly) {
		Point point = new Point();
		point.setLocation(lx, ly);
		setLeaf(point);
	}

//	public void setParent(ImageLayer layer) {
//		if (null != layer) {
//			File file = layer.getImageFile();
//			if (null != file && getChildCount() == 0) {
//				layerParent = file;
//				layer.addChild(this);
//			}
//		}
//	}

	public void setLeaf(Point leaf) {
		leafLayer = leaf;
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
				if (null != object && object instanceof Float) {
					setAlpha((float) object);
				}
			}
			value = properties.getProperty("anchor");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Point) {
					setAnchor((Point) object);
				}
			}
			value = properties.getProperty("angle");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Double) {
					setAngleRotated((double) object);
				}
			}
			value = properties.getProperty("depth");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Integer) {
					setDepth((int) object);
				}
			}
			value = properties.getProperty("isVisible");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Boolean) {
					isVisible((boolean) object);
				}
			}
			value = properties.getProperty("leaf");
			if (null != value) {
				object = objectUtils.getObject(value);
				if (null != object && object instanceof Point) {
					setLeaf((Point) object);
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
		setLeaf(point);
	}

	public void setRoot(Point root) {
		rootLayer = root;
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
			setAnchor(dimension.getWidth() / 2, dimension.getHeight() / 2);
		} else {
			setAnchor(null);
		}
		sizeLayer = dimension;
	}

	public void setSize(double width, double height) {
		Dimension dimension = new Dimension();
		dimension.setSize(width, height);
		setSize(dimension);
	}
}
