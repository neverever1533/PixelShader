package javaev.awt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Graphics2DUtils {

	public BufferedImage clipImage(BufferedImage imageRoot, BufferedImage image, int x, int y, int width, int height) {
		return clipImage(imageRoot, image, x, y, width, height, null, null);
	}

	public BufferedImage clipImage(BufferedImage imageRoot, BufferedImage image, int x, int y, int width, int height,
			Color bgcolor, ImageObserver observer) {
		if (null != image) {
			if (null == imageRoot) {
				if (width > 0 && height > 0) {
					imageRoot = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
				} else {
					return null;
				}
			}
			Graphics2D g2d = imageRoot.createGraphics();
			g2d = drawImage(g2d, image, x, y, -1, -1, bgcolor, observer);
			g2d.dispose();
		}
		return imageRoot;
	}

	public BufferedImage clipShape(BufferedImage image, Shape shape) {
		return clipShape(image, shape, 0, 0);
	}

	public BufferedImage clipShape(BufferedImage image, Shape shape, int x, int y) {
		return clipShape(image, shape, x, y, null, null);
	}

	public BufferedImage clipShape(BufferedImage image, Shape shape, int x, int y, Color bgcolor,
			ImageObserver observer) {
		BufferedImage imageRoot = null;
		if (null != image) {
			imageRoot = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2d = imageRoot.createGraphics();
			int sw = shape.getBounds().width;
			int sh = shape.getBounds().height;
			if (x >= sw) {
				x = 0;
			}
			if (y >= sh) {
				y = 0;
			}
			if (null != shape) {
				g2d.setClip(shape);
			}
			g2d.drawImage(image, x, y, bgcolor, observer);
			g2d.dispose();
		}
		return imageRoot;
	}

	public BufferedImage drawImage(BufferedImage[] imageArray, int x, int y, int width, int height) {
		return drawImage(imageArray, x, y, width, height, null, null);
	}

	public BufferedImage drawImage(BufferedImage[] imageArray, int x, int y, int width, int height, Color bgcolor,
			ImageObserver observer) {
		if (null != imageArray) {
			BufferedImage imageRoot = null;
			if (width <= 0 || height <= 0) {
				Rectangle rectangle = getRectangleMax(imageArray);
				if (null != rectangle) {
					width = (int) rectangle.getWidth();
					height = (int) rectangle.getHeight();
				} else {
					return null;
				}
			}
			imageRoot = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2d = imageRoot.createGraphics();
			BufferedImage image;
			for (int i = 0, ilength = imageArray.length; i < ilength; i++) {
				image = imageArray[i];
				g2d = drawImage(g2d, image, x, y, -1, -1, bgcolor, observer);
			}
			g2d.dispose();
			return imageRoot;
		}
		return null;
	}

	public Graphics2D drawImage(Graphics2D graphics2D, BufferedImage image, int x, int y, int width, int height) {
		return drawImage(graphics2D, image, x, y, width, height, null, null);
	}

	public Graphics2D drawImage(Graphics2D graphics2D, BufferedImage image, int x, int y, int width, int height,
			Color bgcolor, ImageObserver observer) {
		if (null != graphics2D && null != image) {
			if (width <= 0) {
				width = image.getWidth();
			}
			if (height <= 0) {
				height = image.getHeight();
			}
			graphics2D.drawImage(image, x, y, width, height, bgcolor, observer);
		}
		return graphics2D;
	}

	public double getAngle(double theta) {
		return Math.toRadians(theta % 360);
	}

	public double getAngle(int theta) {
		return Math.toRadians(theta % 360);
	}

	private Rectangle getRectangle(BufferedImage[] imageArray, boolean isMaxIndex) {
		if (null == imageArray) {
			return null;
		}
		BufferedImage image;
		int width = -1;
		int height = -1;
		int imageWidth;
		int imageHeight;
		for (int i = 0; i < imageArray.length; i++) {
			image = imageArray[i];
			if (null != image) {
				imageWidth = image.getWidth();
				imageHeight = image.getHeight();
				if (i == 0) {
					width = imageWidth;
					height = imageHeight;
				}
				if (isMaxIndex) {
					if (imageWidth > width) {
						width = imageWidth;
					}
					if (imageHeight > height) {
						height = imageHeight;
					}
				} else {
					if (imageWidth < width) {
						width = imageWidth;
					}
					if (imageHeight < height) {
						height = imageHeight;
					}
				}
			}
		}
		return new Rectangle(width, height);
	}

	public Rectangle getRectangleMax(BufferedImage[] imageArray) {
		return getRectangle(imageArray, true);
	}

	public Rectangle getRectangleMin(BufferedImage[] imageArray) {
		return getRectangle(imageArray, false);
	}

	public Rectangle getRectangleRotate(int width, int height, int theta) {
		int temp = theta % 360;
		int w;
		int h;
		double angle = 0;
		if (temp == 0) {
			return null;
		} else if (temp == 90 || temp == 270) {
			w = height;
			h = width;
		} else if (temp == 270) {
			w = width;
			h = height;
		} else {
			angle = getAngle(temp);
			double sinAngle = Math.sin(angle);
			double cosAngle = Math.cos(angle);
			w = (int) Math.abs(cosAngle * width + sinAngle * height);// w1/w=cosAngle;w2/h=sinAngle;
			h = (int) Math.abs(sinAngle * width + cosAngle * height);// h1/w=sinAngle;h2/h=cosAngle;
		}
		return new Rectangle(w, h);
	}

	public BufferedImage getScaledImage(BufferedImage image, int width, int height) {
		if (null != image) {
			BufferedImage imageRoot = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2d = imageRoot.createGraphics();
			Image imageScaled = image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
			g2d.drawImage(imageScaled, 0, 0, null, null);
			g2d.dispose();
		}
		return image;
	}

	public BufferedImage rotateAffineTransform(BufferedImage image, int theta, double anchorx, double anchory) {
		if (null != image) {
			double angle = getAngle(theta);
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.rotate(angle, anchorx, anchory);
			int interpolationType = AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
			AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, interpolationType);
			image = affineTransformOp.filter(image, null);
		}
		return image;
	}

	public BufferedImage rotateGraphics2D(BufferedImage image, int theta, Color bgcolor, ImageObserver observer) {
		BufferedImage tempImage = null;
		if (null != image) {
			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();
			Rectangle rotateRectangle = getRectangleRotate(imageWidth, imageHeight, theta);
			int w = (int) rotateRectangle.getWidth();
			int h = (int) rotateRectangle.getHeight();
			tempImage = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2d = tempImage.createGraphics();
			g2d.translate((w - imageWidth) / 2, (h - imageHeight) / 2);
			double angle = getAngle(theta);
			g2d.rotate(angle, imageWidth / 2, imageHeight / 2);
			g2d.drawImage(image, null, null);
			g2d.dispose();
		}
		return tempImage;
	}

}
