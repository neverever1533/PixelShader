package cn.imaginary.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import cn.imaginary.toolkit.image.ImageLayer;
import cn.imaginary.toolkit.io.PSDFileReader;
import cn.imaginary.toolkit.swing.tree.LayerNode;

import javaev.awt.DimensionUtils;
import javaev.awt.Graphics2DUtils;
import javaev.awt.PointUtils;
import javaev.awt.RectangleUtils;

import javaev.imageio.ImageIOUtils;

import javaev.io.FileNameMapUtils;
import javaev.io.FileUtils;

import javaev.lang.ObjectUtils;
import javaev.lang.StringUtils;

import javaev.swing.DefaultTreeCellRendererUtils;
import javaev.swing.JPanelDnD;
import javaev.swing.tree.DefaultMutableTreeNodeUtils;
import javaev.swing.tree.DefaultTreeModelUtils;

import javaev.util.PropertiesUtils;

public class PixelShaderUI extends JFrame {
	/**
	 * @author Sureness
	 * @since 0.1
	 */
	private static final long serialVersionUID = 4247417000065494012L;

	public static void main(String[] args) {
		PixelShaderUI pixelShaderUI = new PixelShaderUI();
		pixelShaderUI.setVisible(true);
	}

	private FileNameMapUtils fileNameMapUtils = new FileNameMapUtils();

	private Color bgColor = Color.black;

	private DimensionUtils canvas;

	private String encoding_default = "utf-8";

	private Color fgColor = Color.white;

	private int fileFilterStyle_Preview = 1;
	private int fileFilterStyle_Image = 2;
	private int fileFilterStyle_Images = 3;
	private int fileFilterStyle_Layer = 5;
	private int fileFilterStyle_Layers = 6;
	private int fileFilterStyle_Project = 0;
	private int fileFilterStyle_PSD = 8;

	private FileUtils fileUtils = FileUtils.getInstance();

	private Graphics2DUtils graphics2dUtils = new Graphics2DUtils();

	private ImageIOUtils imageIOUtils = new ImageIOUtils();

	private ImageLayer imageLayerSelected;

	private double indexScaled = 1;
	private double indexScaledDefault = 0.25;
	private double indexScaledMax = 2;
	private double indexScaledMin = 0.25;

	private boolean isDrawCanvas;
	private boolean isLayerAction = false;
	private boolean isLocationCenter;
	private boolean isUpdate;

	private JCheckBox jCheckBoxGravity;

	private JCheckBoxMenuItem jCheckBoxMenuItemCenter;

	private JCheckBox jCheckBoxVisible;

	private JFileChooser jFileChooser;

	private JLabel jLabelView;

	private JPanel jPanelGraphics;

	private JTextField jTextFieldAlpha;
	private JTextField jTextFieldAnchorX;
	private JTextField jTextFieldAnchorY;
	private JTextField jTextFieldAngle;;
	private JTextField jTextFieldCanvasX;
	private JTextField jTextFieldCanvasY;
	private JTextField jTextFieldDepth;
	private JTextField jTextFieldGravity;
	private JTextField jTextFieldLocationX;
	private JTextField jTextFieldLocationY;
	private JTextField jTextFieldScale;
	private JTextField jTextFieldSizeHeight;
	private JTextField jTextFieldSizeWidth;

	private JTree jTreeRoot;

	private BufferedImage layerImage;

	private List<ImageLayer> layerListRoot;

	private PointUtils locationMouse;

	private Properties modelProperties;

	private ObjectUtils objectUtils = new ObjectUtils();

	private PropertiesUtils propertiesUtils = new PropertiesUtils();

	private String tag_at = "@";
	private String tag_Canvas = "canvas";
	private String tag_slash = "/";
	private String tag_suffixes_image = ".png";
	private String tag_suffixes_layer = ".pxl";
	private String tag_suffixes_project = ".evp";

	private DefaultTreeCellRendererUtils treeCellRenderer;

	private DefaultTreeModelUtils treeModelUtils = new DefaultTreeModelUtils();

	private DefaultMutableTreeNodeUtils treeNodeUtils = new DefaultMutableTreeNodeUtils();

	private boolean isExportProject;

	private Properties propertiesProject;

	private RectangleUtils layerListRootRectangleMin;

	private JCheckBoxMenuItem jCheckBoxMenuDrawCanvas;

	public PixelShaderUI() {
		super();
		setTitle("PixelShader ver.68.9h1e.b91c_alpha by Sev末夜");
		initGUI();
	}

	private void createJTree() {
		double widthCanvas = 1024;
		double heightCanvas = 1024;
		jTextFieldCanvasX.setText(String.valueOf(widthCanvas));
		jTextFieldCanvasY.setText(String.valueOf(heightCanvas));
		if (null == canvas) {
			canvas = new DimensionUtils();
		}
		canvas.setSize(widthCanvas, heightCanvas);
		imageLayerSelected = null;
		layerImage = null;
		if (null != layerListRoot) {
			layerListRoot.clear();
		}
		DefaultTreeModel modelRoot = (DefaultTreeModel) jTreeRoot.getModel();
		DefaultMutableTreeNode treeNodeRoot = (DefaultMutableTreeNode) modelRoot.getRoot();
		treeNodeRoot.removeAllChildren();
		modelRoot.reload(treeNodeRoot);
	}

	private void createNewProject() {
		warning();
		createJTree();
	}

	private void drawImageLayers(JPanel jPanel, List<ImageLayer> imageLayerList) {
		if (null != imageLayerList) {
			int widthRoot;
			int heightRoot;
			if (null != canvas) {
				widthRoot = canvas.width;
				heightRoot = canvas.height;
			} else {
				if (null == layerListRootRectangleMin) {
					widthRoot = jPanel.getWidth();
					heightRoot = jPanel.getHeight();
				} else {
					widthRoot = layerListRootRectangleMin.width + layerListRootRectangleMin.x;
					heightRoot = layerListRootRectangleMin.height + layerListRootRectangleMin.y;
				}
				canvas = new DimensionUtils();
				canvas.setSize(widthRoot, heightRoot);
			}
			Graphics g = jPanel.getGraphics();
			jPanel.update(g);
			Graphics2D graphics2d = (Graphics2D) g.create();
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.scale(indexScaled, indexScaled);
			BufferedImage imageRoot = new BufferedImage(widthRoot, heightRoot, BufferedImage.TYPE_4BYTE_ABGR);
			ImageObserver observer = null;
			ImageLayer layer;
			BufferedImage image = null;
			for (Iterator<ImageLayer> iterator = imageLayerList.iterator(); iterator.hasNext();) {
				layer = iterator.next();
				if (null != layer && layer.isVisible()) {
					image = layer.getImage();
					if (null != image) {
						imageRoot = drawLayers(imageRoot, layer, observer);
						graphics2d.drawImage(imageRoot, affineTransform, observer);
					}
				}
			}
			layerImage = imageRoot;
			graphics2d.drawRect(0, 0, (int) (widthRoot * indexScaled), (int) (heightRoot * indexScaled));
			graphics2d.drawString(String.valueOf(indexScaled), 15, 25);
			if (!isLayerAction && null != locationMouse) {
				int x = locationMouse.x;
				int y = locationMouse.y;
				StringBuffer sbuf = new StringBuffer();
				sbuf.append(x);
				sbuf.append(tag_slash);
				sbuf.append(y);
				graphics2d.drawString(sbuf.toString(), x, y);
			}
			graphics2d.dispose();
		}
	}

	private BufferedImage drawLayers(BufferedImage imageRoot, ImageLayer imageLayer, ImageObserver observer) {
		if (null != imageRoot && null != imageLayer) {
			BufferedImage image = imageLayer.getImage();
			if (null != image) {
				AffineTransform xform = new AffineTransform();
				PointUtils anchor = imageLayer.getAnchor();
				boolean isGravity = imageLayer.isGravity();
				PointUtils location = imageLayer.getLocation();
				DimensionUtils scale = imageLayer.getScale();
				if (isLocationCenter) {
					int widthRoot = imageRoot.getWidth();
					int heightRoot = imageRoot.getHeight();
					imageLayer.setLocation((widthRoot - image.getWidth()) / 2, (heightRoot - image.getHeight()) / 2);
					location = imageLayer.getLocation();
				}
				if (null != location) {
					xform.translate(location.getX(), location.getY());
				}
				double angle;
				if (isGravity) {
					angle = imageLayer.getGravity();
				} else {
					angle = imageLayer.getRotated();
				}
				if (angle != 0) {
					double theta = Math.toRadians(angle);
					if (null == anchor) {
						xform.rotate(theta);
					} else {
						xform.rotate(theta, anchor.getX(), anchor.getY());
					}
				}
				if (null != scale) {
					xform.scale(scale.getWidth(), scale.getHeight());
				}
				image = graphics2dUtils.filterImage(image, null, xform);
				imageRoot = graphics2dUtils.drawImage(imageRoot, image, null, imageLayer.getAlpha(), observer);
			}
			return imageRoot;
		}
		return null;
	}

	private void drawResources() {
		if (null != jPanelGraphics && null != layerListRoot) {
			layerListRootRectangleMin = fileUtils.getImageLayersRectangleMin(layerListRoot);
			treeCellRenderer.setResourcesList(layerListRoot);
			updateResources(jTreeRoot);
			Comparator<ImageLayer> comparator = getComparatorByLayerDepth();
			Collections.sort(layerListRoot, comparator);
			drawImageLayers(jPanelGraphics, layerListRoot);
		}
	}

	private Comparator<ImageLayer> getComparatorByLayerDepth() {
		Comparator<ImageLayer> comparator = new Comparator<ImageLayer>() {

			@Override
			public int compare(ImageLayer o1, ImageLayer o2) {
				return o1.getDepth() - o2.getDepth();
//				return o2.getDepth() - o1.getDepth();
			}
		};
		return comparator;
	}

	@SuppressWarnings("unchecked")
	private Comparator<LayerNode> getComparatorByProperties() {
		Comparator<? super LayerNode> comparator = new Comparator<LayerNode>() {

			@Override
			public int compare(LayerNode o1, LayerNode o2) {
				return o1.getID() - o2.getID();
			}
		};
		return (Comparator<LayerNode>) comparator;
	}

	private File getFileLayer(File file) {
		return fileUtils.getFile(file, tag_suffixes_layer);
	}

	private File getFileProject(File file) {
		return fileUtils.getFile(file, tag_suffixes_project);
	}

	private String getHelpInfo() {
		StringBuffer description = new StringBuffer();
		String ln = StringUtils.Line_Separator;
		description.append("一、 图层变换：");
		description.append(ln);
		description.append("1. 开启编辑：");
		description.append(ln);
		description.append("1.1 点击画布左侧工具栏【+】按钮，进入图层编辑状态（图层可进行拖动、旋转等操作）；");
		description.append(ln);
		description.append("2. 开启预览：");
		description.append(ln);
		description.append("2.1 点击画布左侧工具栏【-】按钮，即退出编辑并进入预览状态；");
		description.append(ln);
		description.append("ps：鼠标悬停按钮之上可显示按钮功能说明。");
		description.append(ln);
		description.append("二、 图层列表编辑：");
		description.append(ln);
		description.append("1. 建立父子关系：");
		description.append(ln);
		description.append("1.1 【编辑】模式下【鼠标】点击（选中）图层节点，鼠标按住拖动子节点（child.png）；");
		description.append(ln);
		description.append("1.2 如上操作并将图层拖向父节点(parent.png)，直到子节点图层加入父节点图层；");
		description.append(ln);
		description.append("ps：若父节点显示如同文件夹结构，即可展开或关闭则表示关系建立成功。");
		description.append(ln);
		description.append("2. 解除父子关系：");
		description.append(ln);
		description.append("2.1 【编辑】模式下【鼠标】点击（选中）图层节点，鼠标按住拖动子节点（child.png）；");
		description.append(ln);
		description.append("2.2 如上操作并将图层拖向任意处，即脱离父节点(parent.png)所在行，直到父节点图层其下失去子节点；");
		description.append(ln);
		description.append("ps：按住鼠标并拖动节点可解除父子关系，同时也可以任意更换当前节点的位置。");
		description.append(ln);
		description.append("3. 删除图层：");
		description.append(ln);
		description.append("3.1 【鼠标】点击（选中）图层节点，右键点击此节点（child.png）；");
		description.append(ln);
		description.append("ps：删除图层仅移除树状列表节点内容，且所有操作不改动原始图形文件。");
		description.append(ln);
		description.append("4. PSD解除图层分组再导入：");
		description.append(ln);
		description.append("4.1 根据Adobe公司公布的PSD文件格式规范说明书，所开发的相关PSD库都存在图层分组读取冲突；");
		return description.toString();
	}

	private String getProjectInfo() {
		StringBuffer description = new StringBuffer();
		String ln = StringUtils.Line_Separator;
		description.append("application:PixelShader");
		description.append(ln);
		description.append("author:Sev末夜");
		description.append(ln);
		description.append("Github:");
		description.append(ln);
		description.append("https://github.com/neverever1533/EvolutionVector");
		return description.toString();
	}

	private String getVersionInfo() {
//		version.(1-99).(1-9)(1-12/A-L)(1-31/#16).(a.m./p.m.)(1-12/#16)(0-59/#16)
		StringBuffer description = new StringBuffer();
		String ln = StringUtils.Line_Separator;
		description.append("application:PixelShader");
		description.append(ln);
		description.append("author:Sev末夜");
		description.append(ln);
		description.append("ver.68.9h1e.b91c_alpha");
		description.append(ln);
		description.append("new:图层导出可选是否裁切边框，工程图层属性信息存储优化，图层加入哈希值精准定位防覆盖；");
		description.append(ln);
		description.append("ver.66.6h16.bf3a_alpha");
		description.append(ln);
		description.append("new:psd图层信息读取修复；");
		description.append(ln);
		description.append("ver.61.2e10.p31e_alpha");
		description.append(ln);
		description.append("new:工程文件存储方式优化；");
		description.append(ln);
		description.append("ver.52.1e0d.a820_alpha");
		description.append(ln);
		description.append("new:分组图层变换同步优化，图层编辑树状结构存储为工程；");
		description.append(ln);
		description.append("ver.50.1e08.p310_alpha");
		description.append(ln);
		description.append("new:添加物理重力属性，父子关系图层旋转同步修正；");
		description.append(ln);
		description.append("ver.40.3d17.pa1a_alpha");
		description.append(ln);
		description.append("new:PSD文件读取支持，图层导出为PNG文件；");
		description.append(ln);
		description.append("ver.36.6d0d.p61e_alpha");
		description.append(ln);
		description.append("new:图层深度修改优化，画布可调整，图层位置居中，导入导出功能实装；");
		description.append(ln);
		description.append("ver.28.9d0a.a010_alpha");
		description.append(ln);
		description.append("new:绘图界面鼠标监听便捷修改参数，图层属性加入深度参数；");
		description.append(ln);
		description.append("ver.26.2d02.p934_alpha");
		description.append(ln);
		description.append("new:可视化UI、图层属性修改界面优化，树节点显示优化；");
		description.append(ln);
		description.append("ver.25.1d02.1627_alpha");
		description.append(ln);
		description.append("new:图层实装，定义并优化图层属性参数；");
		description.append(ln);
		description.append("ver.12.3016.1403");
		description.append(ln);
		description.append("new:文件树，拖拽功能修正；");
		description.append(ln);
		description.append("ver.6.3203.1403");
		description.append(ln);
		description.append("new:图片读取，文件拖拽，JTree管理资源功能实装修正；");
		description.append(ln);
		description.append("ver.1.1a10.1203");
		description.append(ln);
		description.append("new:可视化界面UI初步设计；");
		return description.toString();
	}

	private void imageExport(BufferedImage image, PointUtils point, File file) {
		if (isDrawCanvas) {
			AffineTransform xform = null;
			if (null != point) {
				xform = new AffineTransform();
				xform.translate(point.getX(), point.getY());
			}
			image = graphics2dUtils.drawImage(canvas.width, canvas.height, image, xform, image.getTransparency(), null);
		}
		imageIOUtils.write(image, ImageIOUtils.FormatName_Default, file);
	}

	private void imagesExport(File file) {
		if (null != layerListRoot) {
			ImageLayer imageLayer;
			File temp;
			if (isDrawCanvas) {
				if (!file.isDirectory()) {
					file = file.getParentFile();
				}
				file = new File(file, tag_Canvas);
				if (!file.exists()) {
					file.mkdir();
				}
			}
			for (Iterator<ImageLayer> iterator = layerListRoot.iterator(); iterator.hasNext();) {
				imageLayer = iterator.next();
				if (null != imageLayer) {
					temp = new File(imageLayer.getImagePath());
					if (file.isFile()) {
						file = file.getParentFile();
					}
					temp = new File(file, temp.getName());
					imageExport(imageLayer.getImage(), imageLayer.getLocation(), temp);
				}
			}
		}

	}

	private void initGUI() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		{
			JMenuBar jMenuBar = new JMenuBar();
			jMenuBar.setBackground(Color.white);
			{
				JMenu jMenu = new JMenu();
				jMenu.setText("文件<File>");
				jMenu.setMnemonic(KeyEvent.VK_F);
				{
					JMenuItem JMenuItemNew = new JMenuItem();
					JMenuItemNew.setText("新建");
					JMenuItemNew.setMnemonic(KeyEvent.VK_N);
					JMenuItemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							setImageLayerInfo(null);
							createNewProject();
							drawResources();
						}
					};
					JMenuItemNew.addActionListener(al);
					jMenu.add(JMenuItemNew);
				}
				{
					JMenu jMenuInport = new JMenu();
					jMenuInport.setText("导入<Inport>");
					{
						JMenuItem jMenuItem = new JMenuItem();
						jMenuItem.setText("添加Png");
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('i'));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								File[] array = openFiles(fileFilterStyle_Image);
								if (null != array) {
									loadLayer(Arrays.asList(array));
								}
							}
						};
						jMenuItem.addActionListener(al);
						jMenuInport.add(jMenuItem);
					}
					{

						JMenuItem jMenuItem = new JMenuItem();
						jMenuItem.setText("添加Layer");
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('o'));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								File[] array = openFiles(fileFilterStyle_Layer);
								if (null != array) {
									loadLayer(Arrays.asList(array));
								}
							}
						};
						jMenuItem.addActionListener(al);
						jMenuInport.add(jMenuItem);
					}
					{
						JMenuItem jMenuItem = new JMenuItem();
						jMenuItem.setText("添加工程");
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('p'));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								File[] array = openFiles(fileFilterStyle_Project);
								if (null != array) {
									loadLayer(Arrays.asList(array));
								}
							}
						};
						jMenuItem.addActionListener(al);
						jMenuInport.add(jMenuItem);
					}
					{
						JMenuItem jMenuItem = new JMenuItem();
						jMenuItem.setText("添加PSD");
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('u'));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								loadPsdLayer(openFiles(fileFilterStyle_PSD));
							}
						};
						jMenuItem.addActionListener(al);
						jMenuInport.add(jMenuItem);
					}
					jMenu.add(jMenuInport);
				}
				{
					JMenu jMenuExport = new JMenu();
					jMenuExport.setText("导出<Export>");
					{
						JMenuItem jMenuItem = new JMenuItem();
						jMenuItem.setText("图层预览");
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('1'));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								String description;
								if (null != layerListRoot && layerListRoot.size() != 0) {
									storeResources(fileFilterStyle_Preview);
									description = "操作完成！";
								} else {
									description = "项目为空！";
								}
								JOptionPane.showMessageDialog(null, description);
							}
						};
						jMenuItem.addActionListener(al);
						jMenuExport.add(jMenuItem);
					}
					{
						JMenuItem jMenuItem = new JMenuItem();
						jMenuItem.setText("选中图层为Png");
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('2'));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								String description;
								if (null != layerListRoot && layerListRoot.size() != 0) {
									storeResources(fileFilterStyle_Image);
									description = "操作完成！";
								} else {
									description = "项目为空！";
								}
								JOptionPane.showMessageDialog(null, description);
							}
						};
						jMenuItem.addActionListener(al);
						jMenuExport.add(jMenuItem);
					}
					{
						JMenuItem jMenuItem = new JMenuItem();
						jMenuItem.setText("全部图层为Pngs");
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('3'));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								String description;
								if (null != layerListRoot && layerListRoot.size() != 0) {
									storeResources(fileFilterStyle_Images);
									description = "操作完成！";
								} else {
									description = "项目为空！";
								}
								JOptionPane.showMessageDialog(null, description);
							}
						};
						jMenuItem.addActionListener(al);
						jMenuExport.add(jMenuItem);
					}
					{
						JMenuItem jMenuItem = new JMenuItem();
						jMenuItem.setText("选中图层Layer");
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('q'));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								String description;
								if (null != layerListRoot && layerListRoot.size() != 0) {
									storeResources(fileFilterStyle_Layer);
									description = "操作完成！";
								} else {
									description = "项目为空！";
								}
								JOptionPane.showMessageDialog(null, description);
							}
						};
						jMenuItem.addActionListener(al);
						jMenuExport.add(jMenuItem);
					}
					{
						JMenuItem jMenuItem = new JMenuItem();
						jMenuItem.setText("全部图层为Layers");
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('w'));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								String description;
								if (null != layerListRoot && layerListRoot.size() != 0) {
									storeResources(fileFilterStyle_Layers);
									description = "操作完成！";
								} else {
									description = "项目为空！";
								}
								JOptionPane.showMessageDialog(null, description);
							}
						};
						jMenuItem.addActionListener(al);
						jMenuExport.add(jMenuItem);
					}
					{
						JMenuItem jMenuItem = new JMenuItem();
						jMenuItem.setText("图层为PSD");
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('e'));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								String description = "功能暂未添加";
								JOptionPane.showMessageDialog(null, description);
							}
						};
						jMenuItem.addActionListener(al);
						jMenuExport.add(jMenuItem);
					}
					jMenu.add(jMenuExport);
				}
				{
					JMenuItem jMenuItem = new JMenuItem();
					jMenuItem.setText("打开工程");
					jMenuItem.setAccelerator(KeyStroke.getKeyStroke('0'));
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							createNewProject();
							File[] array = openFiles(fileFilterStyle_Project);
							if (null != array) {
								loadLayer(Arrays.asList(array));
							}
						}
					};
					jMenuItem.addActionListener(al);
					jMenu.add(jMenuItem);
				}
				{
					JMenuItem jMenuItem = new JMenuItem();
					jMenuItem.setText("保存工程");
					jMenuItem.setAccelerator(KeyStroke.getKeyStroke('1'));
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							String description;
							if (null != layerListRoot && layerListRoot.size() != 0) {
								storeResources(fileFilterStyle_Project);
								description = "操作完成！";
							} else {
								description = "项目为空！";
							}
							JOptionPane.showMessageDialog(null, description);
						}
					};
					jMenuItem.addActionListener(al);
					jMenu.add(jMenuItem);
				}
				{
					JMenuItem jMenuItem = new JMenuItem();
					jMenuItem.setText("退出");
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							warning();
							System.exit(0);
						}
					};
					jMenuItem.addActionListener(al);
					jMenu.add(jMenuItem);
				}
				jMenuBar.add(jMenu);
			}
			{
				JMenu jMenu = new JMenu();
				jMenu.setText("编辑<Edit>");
				jMenu.setMnemonic(KeyEvent.VK_E);
				{
					JMenuItem jMenuItem = new JMenuItem();
					jMenuItem.setText("画布");
					jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
//							System.out.println(jMenuItem.getText());
						}
					};
					jMenuItem.addActionListener(al);
					jMenu.add(jMenuItem);
				}
				{
					JMenuItem jMenuItem = new JMenuItem();
					jMenuItem.setText("视图");
					jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK));
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
//							System.out.println(jMenuItem.getText());
						}
					};
					jMenuItem.addActionListener(al);
					jMenu.add(jMenuItem);
				}
				{
					jCheckBoxMenuDrawCanvas = new JCheckBoxMenuItem();
					jCheckBoxMenuDrawCanvas.setText("保留边框");
					jCheckBoxMenuDrawCanvas
							.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							if (jCheckBoxMenuDrawCanvas.isSelected()) {
								isDrawCanvas = true;
							} else {
								isDrawCanvas = false;
							}
						}
					};
					jCheckBoxMenuDrawCanvas.addActionListener(al);
					jMenu.add(jCheckBoxMenuDrawCanvas);
				}
				{
					jCheckBoxMenuItemCenter = new JCheckBoxMenuItem();
					jCheckBoxMenuItemCenter.setText("居中");
					jCheckBoxMenuItemCenter
							.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							if (jCheckBoxMenuItemCenter.isSelected()) {
								isLocationCenter = true;
							} else {
								isLocationCenter = false;
							}
						}
					};
					jCheckBoxMenuItemCenter.addActionListener(al);
					jMenu.add(jCheckBoxMenuItemCenter);
				}
				jMenuBar.add(jMenu);
			}
			{
				JMenu jMenu = new JMenu();
				jMenu.setText("帮助<Help>");
				jMenu.setMnemonic(KeyEvent.VK_H);
				{
					JMenuItem jMenuItem = new JMenuItem();
					jMenuItem.setText("指南");
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							Object description = getHelpInfo();
							String title = "操作指南：";
							JOptionPane.showMessageDialog(null, description, title, 0);
						}
					};
					jMenuItem.addActionListener(al);
					jMenu.add(jMenuItem);
				}
				{
					JMenuItem jMenuItem = new JMenuItem();
					jMenuItem.setText("关于");
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							Object description = getProjectInfo();
							String title = "项目仓库：";
							JOptionPane.showMessageDialog(null, description, title, 0);
						}
					};
					jMenuItem.addActionListener(al);
					jMenu.add(jMenuItem);
				}
				{
					JMenuItem jMenuItem = new JMenuItem();
					jMenuItem.setText("版本");
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							Object description = getVersionInfo();
							String title = "更新日志：";
							JOptionPane.showMessageDialog(null, description, title, 0);
						}
					};
					jMenuItem.addActionListener(al);
					jMenu.add(jMenuItem);
				}
				jMenuBar.add(jMenu);
			}
			getContentPane().add(jMenuBar, BorderLayout.NORTH);
		}
		{

			JPanel jPanelMain = new JPanel();
			jPanelMain.setBackground(Color.black);
			jPanelMain.setLayout(new BorderLayout());
			{
				{
					JPanel jPanel = new JPanel();
					jPanel.setBackground(Color.red);
					jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
					{
						Dimension dimensionJTextField = new Dimension(64, 24);
						{
							JToolBar jToolBar = new JToolBar();
							{
								JLabel jLabel = new JLabel();
								jLabel.setText("宽度：");
								jToolBar.add(jLabel);
							}
							{
								jTextFieldSizeWidth = new JTextField();
								jTextFieldSizeWidth.setMaximumSize(dimensionJTextField);
								jTextFieldSizeWidth.setText("像素 px");
								jTextFieldSizeWidth.setEnabled(false);
								jToolBar.add(jTextFieldSizeWidth);
							}
							{
								JLabel jLabel = new JLabel();
								jLabel.setText("高度：");
								jToolBar.add(jLabel);
							}
							{
								jTextFieldSizeHeight = new JTextField();
								jTextFieldSizeHeight.setMaximumSize(dimensionJTextField);
								jTextFieldSizeHeight.setText("像素 px");
								jTextFieldSizeHeight.setEnabled(false);
								jToolBar.add(jTextFieldSizeHeight);
							}
							{
								JButton jButton = new JButton();
								jButton.setText("确定");
								jButton.setEnabled(false);
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
//										if (null != imageLayerSelected && isLayerAction) {
//											imageLayerSelected.setSize(toDouble(jTextFieldSizeWidth),
//													toDouble(jTextFieldSizeHeight));
//											drawResources();
//										}
									}
								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							{
								JLabel jLabel = new JLabel();
								jLabel.setText("缩放：");
								jToolBar.add(jLabel);
							}
							{
								jTextFieldScale = new JTextField();
								jTextFieldScale.setMaximumSize(dimensionJTextField);
								jTextFieldScale.setText("1倍");
								jTextFieldScale.setEnabled(false);
								jToolBar.add(jTextFieldScale);
							}
							{
								JButton jButton = new JButton();
								jButton.setText("确定");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										if (null != imageLayerSelected && isLayerAction) {
											double scaled = toDouble(jTextFieldScale);
											imageLayerSelected.scale(scaled, scaled);
											drawResources();
										}
									}
								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							jPanel.add(jToolBar);
						}
						{
							JToolBar jToolBar = new JToolBar();
							{
								JLabel jLabel = new JLabel();
								jLabel.setText("变换：");
								jToolBar.add(jLabel);
							}
							{
								jTextFieldAngle = new JTextField();
								jTextFieldAngle.setMaximumSize(dimensionJTextField);
								jTextFieldAngle.setText("0度");
								jTextFieldAngle.setEnabled(false);
								jToolBar.add(jTextFieldAngle);
							}
							{
								JButton jButton = new JButton();
								jButton.setText("旋转");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										if (null != imageLayerSelected && isLayerAction) {
											double rotate = toDouble(jTextFieldAngle);
											double rotateLayer = imageLayerSelected.getRotated();
											PointUtils point = imageLayerSelected.getLocation();
											int x = 0;
											int y = 0;
											if (null != point) {
												x = point.x;
												y = point.y;
											}
											layerTransform(imageLayerSelected, x, y, rotate);
											if (rotate != rotateLayer) {
												if (isLocationCenter) {
													isLocationCenter = false;
												}
												if (jCheckBoxMenuItemCenter.isSelected()) {
													jCheckBoxMenuItemCenter.setSelected(false);
												}
												drawResources();
											}
										}
									}

								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							jPanel.add(jToolBar);
						}
						{
							JToolBar jToolBar = new JToolBar();
							{
								JLabel jLabel = new JLabel();
								jLabel.setText("锚点X：");
								jToolBar.add(jLabel);
							}
							{
								jTextFieldAnchorX = new JTextField();
								jTextFieldAnchorX.setMaximumSize(dimensionJTextField);
								jTextFieldAnchorX.setText("像素 px");
								jTextFieldAnchorX.setEnabled(false);
								jToolBar.add(jTextFieldAnchorX);
							}
							{
								JLabel jLabel = new JLabel();
								jLabel.setText("锚点Y：");
								jToolBar.add(jLabel);
							}
							{
								jTextFieldAnchorY = new JTextField();
								jTextFieldAnchorY.setMaximumSize(dimensionJTextField);
								jTextFieldAnchorY.setText("像素 px");
								jTextFieldAnchorY.setEnabled(false);
								jToolBar.add(jTextFieldAnchorY);
							}
							{
								JButton jButton = new JButton();
								jButton.setText("确定");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										if (null != imageLayerSelected && isLayerAction) {
											PointUtils location = imageLayerSelected.getLocation();
											if (null != location) {
												imageLayerSelected.setAnchor(
														toDouble(jTextFieldAnchorX) - location.getX(),
														toDouble(jTextFieldAnchorY) - location.getY());
											}
											if (isLocationCenter) {
												isLocationCenter = false;
											}
											if (jCheckBoxMenuItemCenter.isSelected()) {
												jCheckBoxMenuItemCenter.setSelected(false);
											}
											drawResources();
										}
									}
								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							jPanel.add(jToolBar);
						}
						{
							JToolBar jToolBar = new JToolBar();
							{
								JLabel jLabel = new JLabel();
								jLabel.setText("透明：");
								jToolBar.add(jLabel);
							}
							{
								jTextFieldAlpha = new JTextField();
								jTextFieldAlpha.setMaximumSize(dimensionJTextField);
								jTextFieldAlpha.setText("1.0f");
								jTextFieldAlpha.setEnabled(false);
								jToolBar.add(jTextFieldAlpha);
							}
							{
								JButton jButton = new JButton();
								jButton.setText("确定");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										if (null != imageLayerSelected && isLayerAction) {
											String string = toStringDigital(jTextFieldAlpha);
											if (null != string) {
												imageLayerSelected.setAlpha(Float.valueOf(string));
											}
											drawResources();
										}
									}
								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							jPanel.add(jToolBar);
						}
						{
							JToolBar jToolBar = new JToolBar();
							{
								JLabel jLabel = new JLabel();
								jLabel.setText("深度：");
								jToolBar.add(jLabel);
							}
							{
								jTextFieldDepth = new JTextField();
								jTextFieldDepth.setMaximumSize(dimensionJTextField);
								jTextFieldDepth.setText("0层");
								jTextFieldDepth.setEnabled(false);
								jToolBar.add(jTextFieldDepth);
							}
							{
								JButton jButton = new JButton();
								jButton.setText("确定");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										if (null != imageLayerSelected && isLayerAction) {
											String string = toStringDigital(jTextFieldDepth);
											if (null != string) {
												int depthNew = Integer.valueOf(string);
												int depthOld = imageLayerSelected.getDepth();
												updateImageLayerDepth(layerListRoot, depthOld, depthNew);
											}
											drawResources();
										}
									}
								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							jPanel.add(jToolBar);
						}
					}
					jPanelMain.add(jPanel, BorderLayout.NORTH);
				}
				{
					JPanel jPanel = new JPanel();
					jPanel.setBackground(Color.cyan);
					jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
					{
						{
							JToolBar jToolBar = new JToolBar();
							jToolBar.setOrientation(JToolBar.VERTICAL);
							{
								JButton jButton = new JButton();
								jButton.setText("+");
								jButton.setToolTipText("编辑");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										if (!isLayerAction) {
											isLayerAction = true;
										}
									}
								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							{
								JButton jButton = new JButton();
								jButton.setText("-");
								jButton.setToolTipText("预览");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										if (isLayerAction) {
											isLayerAction = false;
										}
									}
								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							jPanel.add(jToolBar);
						}
					}
					jPanelMain.add(jPanel, BorderLayout.WEST);
				}
				{
					JScrollPane jScrollPane = new JScrollPane();
					{
						jPanelGraphics = new JPanel();
						jPanelGraphics.setBackground(bgColor);
						jPanelGraphics.setForeground(fgColor);
						int width = 1024;
						int height = 1024;
						Dimension dimension = new Dimension(width, height);
						jPanelGraphics.setPreferredSize(dimension);
						MouseWheelListener mwl = new MouseWheelListener() {

							@Override
							public void mouseWheelMoved(MouseWheelEvent e) {
								int wr = e.getWheelRotation();
								if (wr != 0) {
									if (wr == -1) {
										if (indexScaled > indexScaledMin) {
											indexScaled -= indexScaledDefault;
										}
									} else if (wr == 1) {
										if (indexScaled < indexScaledMax) {
											indexScaled += indexScaledDefault;
										}
									}
									if (null != layerListRoot && layerListRoot.size() != 0) {
										jLabelView.setText(String.valueOf(indexScaled));
										drawResources();
									}
								}
							}
						};
						jPanelGraphics.addMouseWheelListener(mwl);
						MouseMotionListener mml = new MouseMotionListener() {

							@Override
							public void mouseDragged(MouseEvent e) {
							}

							@Override
							public void mouseMoved(MouseEvent e) {
							}
						};
						jPanelGraphics.addMouseMotionListener(mml);
						MouseListener ml = new MouseListener() {

							private double xNew;
							private double xOld;
							private double yNew;
							private double yOld;

							@Override
							public void mouseClicked(MouseEvent e) {
							}

							@Override
							public void mouseEntered(MouseEvent e) {
							}

							@Override
							public void mouseExited(MouseEvent e) {
							}

							@Override
							public void mousePressed(MouseEvent e) {
								xOld = e.getX();
								yOld = e.getY();
							}

							@Override
							public void mouseReleased(MouseEvent e) {
								xNew = e.getX();
								yNew = e.getY();
								if (e.getButton() == MouseEvent.BUTTON1) {
									if (isLayerAction) {
										if (null != imageLayerSelected && isLayerAction) {
											PointUtils location = imageLayerSelected.getLocation();
											double x;
											double y;
											double tx = xNew - xOld;
											double ty = yNew - yOld;
											if (null != location) {
												x = location.getX() + tx;
												y = location.getY() + ty;
											} else {
												x = tx;
												y = ty;
											}
											layerTransform(imageLayerSelected, x, y, imageLayerSelected.getRotated());
											setImageLayerInfo(imageLayerSelected);
											if (isLocationCenter) {
												isLocationCenter = false;
											}
											if (jCheckBoxMenuItemCenter.isSelected()) {
												jCheckBoxMenuItemCenter.setSelected(false);
											}
											drawResources();
										}
										if (null != locationMouse) {
											locationMouse = null;
										}
									} else {
										if (null == locationMouse) {
											locationMouse = new PointUtils();
										}
										locationMouse.setLocation(e.getX(), e.getY());
										drawResources();
									}
								} else if (e.getButton() == MouseEvent.BUTTON3) {
//									if (null != imageLayerSelected) {
//										PointUtils location = imageLayerSelected.getLocation();
//										if(null!= location) {
//											double ty = yNew - yOld;
//											double x = location.getX();
//											double y = location.getY();
//											layerTransform(imageLayerSelected, x, y, imageLayerSelected.getRotated() + ty * 10);
//											setImageLayerInfo(imageLayerSelected);
//											drawResources();
//										}
//									}
								}
							}
						};
						jPanelGraphics.addMouseListener(ml);
						jScrollPane.getViewport().setView(jPanelGraphics);
					}
					jPanelMain.add(jScrollPane, BorderLayout.CENTER);
				}
				{
					JPanel jPanel = new JPanel();
					jPanel.setBackground(Color.darkGray);
					jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
					{
						JToolBar jToolBar = new JToolBar();
						jToolBar.setOrientation(JToolBar.VERTICAL);
						{
							{
								JPanelDnD jPanelDnD = new JPanelDnD();
								jPanelDnD.setLayout(new BorderLayout());
								{
									JScrollPane jScrollPane = new JScrollPane();
									int widthResources = 256;
									int heightResources = 360;
									Dimension dimensionResources = new Dimension(widthResources, heightResources);
									jScrollPane.setPreferredSize(dimensionResources);
									{
										DefaultMutableTreeNode treeNodeRoot = new DefaultMutableTreeNode();
										treeNodeRoot.setUserObject("图层@000000000");
										jTreeRoot = new JTree();
										jTreeRoot.setShowsRootHandles(true);
										jTreeRoot.setDragEnabled(true);
										jTreeRoot.setEditable(false);
										DefaultTreeModel treeModelRoot = new DefaultTreeModel(treeNodeRoot);
										jTreeRoot.setModel(treeModelRoot);
										treeCellRenderer = new DefaultTreeCellRendererUtils();
										jTreeRoot.setCellRenderer(treeCellRenderer);
										MouseMotionListener mml = new MouseMotionListener() {

											private int rowDragged;
											private int rowMoved;

											@Override
											public void mouseDragged(MouseEvent e) {
												int selRow = jTreeRoot.getRowForLocation(e.getX(), e.getY());
												rowDragged = selRow;
											}

											@Override
											public void mouseMoved(MouseEvent e) {
												int selRow = jTreeRoot.getRowForLocation(e.getX(), e.getY());
												rowMoved = selRow;
												myTreeNodeMoveTo(selRow, isLayerAction);
											}

											private void myTreeNodeMoveTo(int selRow, boolean todo) {
												if (rowMoved == -1 || rowDragged == -1 || rowDragged == 0
														|| rowMoved == rowDragged) {
													return;
												}
												TreePath pathDragged = jTreeRoot.getPathForRow(rowDragged);
												if (null != pathDragged) {
													DefaultMutableTreeNode treeNodeDragged = (DefaultMutableTreeNode) pathDragged
															.getLastPathComponent();
													TreePath pathMoved = jTreeRoot.getPathForRow(rowMoved);
													if (null != pathMoved) {
														DefaultMutableTreeNode treeNodeMoved = (DefaultMutableTreeNode) pathMoved
																.getLastPathComponent();
														treeNodeMoveTo(treeNodeDragged, treeNodeMoved, todo);
														rowDragged = -1;
													}
												}
											}
										};
										jTreeRoot.addMouseMotionListener(mml);
										MouseListener ml = new MouseListener() {

											@Override
											public void mouseClicked(MouseEvent e) {
												int selRow = jTreeRoot.getRowForLocation(e.getX(), e.getY());
												if (selRow != -1) {
													if (e.getClickCount() == 1) {
														if (e.getButton() == MouseEvent.BUTTON3) {
															myRightClick(selRow);
														} else {
															mySingleClick(selRow);
														}
													} else if (e.getClickCount() == 2) {
														myDoubleClick(selRow);
													}
												}
											}

											@Override
											public void mouseEntered(MouseEvent e) {
												List<File> fileList = jPanelDnD.getDropResources();
												if (null != fileList) {
													loadLayer(fileList);
												}
												jPanelDnD.setDropResources(null);
											}

											@Override
											public void mouseExited(MouseEvent e) {
											}

											@Override
											public void mousePressed(MouseEvent e) {
											}

											@Override
											public void mouseReleased(MouseEvent e) {
											}

											private void myDoubleClick(int selRow) {
											}

											private void myRightClick(int selRow) {
												if (selRow == -1) {
													return;
												}
												DefaultMutableTreeNode treeNodeSelected = (DefaultMutableTreeNode) jTreeRoot
														.getPathForRow(selRow).getLastPathComponent();
												if (null == treeNodeSelected) {
													return;
												}
												DefaultMutableTreeNode parent = (DefaultMutableTreeNode) treeNodeSelected
														.getParent();
												if (null == parent) {
													return;
												}
												removeNode(treeNodeSelected);
												if (layerListRoot.isEmpty()) {
													imageLayerSelected = null;
													setImageLayerInfo(null);
													jPanelGraphics.updateUI();
													setToolkitEnabled(false);
												} else {
													drawResources();
												}
											}

											private void mySingleClick(int selRow) {
												if (selRow == -1) {
													return;
												}
												DefaultMutableTreeNode treeNodeSelected = (DefaultMutableTreeNode) jTreeRoot
														.getPathForRow(selRow).getLastPathComponent();
												if (null != treeNodeSelected) {
													Object object = treeNodeSelected.getUserObject();
													if (null != object) {
														ImageLayer imageLayer = fileUtils.getImageLayer(layerListRoot,
																object);
														if (null != imageLayer) {
															imageLayerSelected = imageLayer;
															setImageLayerInfo(imageLayerSelected);
															setToolkitEnabled(true);
														}
													}
												}
											}
										};
										jTreeRoot.addMouseListener(ml);
										jScrollPane.getViewport().setView(jTreeRoot);
									}
									jPanelDnD.add(jScrollPane);
								}
								jToolBar.add(jPanelDnD);
							}
							jPanel.add(jToolBar);
						}
					}
					jPanelMain.add(jPanel, BorderLayout.EAST);
				}
				{
					JPanel jPanel = new JPanel();
					jPanel.setBackground(Color.blue);
					jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
					{
						Dimension dimensionJTextField = new Dimension(64, 24);
						{
							JToolBar jToolBar = new JToolBar();
							{
								JLabel jLabel = new JLabel();
								jLabel.setText("画布w：");
								jToolBar.add(jLabel);
							}
							{
								jTextFieldCanvasX = new JTextField();
								jTextFieldCanvasX.setMaximumSize(dimensionJTextField);
								jTextFieldCanvasX.setText(String.valueOf(jPanelGraphics.getWidth()));
								jTextFieldCanvasX.setText("默认");
								jToolBar.add(jTextFieldCanvasX);
							}
							{
								JLabel jLabel = new JLabel();
								jLabel.setText("画布h：");
								jToolBar.add(jLabel);
							}
							{
								jTextFieldCanvasY = new JTextField();
								jTextFieldCanvasY.setMaximumSize(dimensionJTextField);
								jTextFieldCanvasY.setText(String.valueOf(jPanelGraphics.getHeight()));
								jTextFieldCanvasY.setText("默认");
								jToolBar.add(jTextFieldCanvasY);
							}
							{
								JButton jButton = new JButton();
								jButton.setText("确定");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										if (null != layerListRoot && layerListRoot.size() != 0) {
											double widthCanvas = toDouble(jTextFieldCanvasX);
											double heightCanvas = toDouble(jTextFieldCanvasY);
											if (widthCanvas > 0 && heightCanvas > 0) {
												canvas = new DimensionUtils();
												canvas.setSize(widthCanvas, heightCanvas);
											}
											drawResources();
										}
									}
								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							jPanel.add(jToolBar);
						}
						{
							JToolBar jToolBar = new JToolBar();
							{
								JLabel jLabel = new JLabel();
								jLabel.setText("视图：");
								jToolBar.add(jLabel);
							}
							{
								JButton jButton = new JButton();
								jButton.setText("缩小");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										if (null != layerListRoot && layerListRoot.size() != 0) {
											if (indexScaled > indexScaledMin) {
												indexScaled -= indexScaledDefault;
											}
											jLabelView.setText(String.valueOf(indexScaled));
											drawResources();
										}
									}
								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							{
								jLabelView = new JLabel();
								jLabelView.setMaximumSize(dimensionJTextField);
								jLabelView.setText("1倍");
								jToolBar.add(jLabelView);
							}
							{
								JButton jButton = new JButton();
								jButton.setText("放大");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										if (null != layerListRoot && layerListRoot.size() != 0) {
											if (indexScaled < indexScaledMax) {
												indexScaled += indexScaledDefault;
											}
											jLabelView.setText(String.valueOf(indexScaled));
											drawResources();
										}
									}
								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							jPanel.add(jToolBar);
						}
						{
							JToolBar jToolBar = new JToolBar();
							{
								JLabel jLabel = new JLabel();
								jLabel.setText("显示：");
								jToolBar.add(jLabel);
							}
							{
								jCheckBoxVisible = new JCheckBox();
								jCheckBoxVisible.setEnabled(false);
								jToolBar.add(jCheckBoxVisible);
							}
							{
								JButton jButton = new JButton();
								jButton.setText("确定");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										if (null != imageLayerSelected && isLayerAction) {
											if (jCheckBoxVisible.isSelected()) {
												imageLayerSelected.isVisible(true);
											} else {
												imageLayerSelected.isVisible(false);
											}
											drawResources();
										}
									}
								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							jPanel.add(jToolBar);
						}
						{
							JToolBar jToolBar = new JToolBar();
							{
								JLabel jLabel = new JLabel();
								jLabel.setText("重力：");
								jToolBar.add(jLabel);
							}
							{
								jCheckBoxGravity = new JCheckBox();
								jCheckBoxGravity.setEnabled(false);
								jToolBar.add(jCheckBoxGravity);
							}
							{
								jTextFieldGravity = new JTextField();
								jTextFieldGravity.setMaximumSize(dimensionJTextField);
								jTextFieldGravity.setText("0度");
								jTextFieldGravity.setEnabled(false);
								jToolBar.add(jTextFieldGravity);
							}
							{
								JButton jButton = new JButton();
								jButton.setText("确定");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										if (null != imageLayerSelected && isLayerAction) {
											if (jCheckBoxGravity.isSelected()) {
												imageLayerSelected.isGravity(true);
											} else {
												imageLayerSelected.isGravity(false);
											}
											double gravity = toDouble(jTextFieldGravity);
											double gravityLayer = imageLayerSelected.getGravity();
											if (gravityLayer != gravity) {
												imageLayerSelected.setGravity(gravity);
											}
											drawResources();
										}
									}
								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							jPanel.add(jToolBar);
						}
						{
							JToolBar jToolBar = new JToolBar();
							{
								JLabel jLabel = new JLabel();
								jLabel.setText("位置X：");
								jToolBar.add(jLabel);
							}
							{
								jTextFieldLocationX = new JTextField();
								jTextFieldLocationX.setMaximumSize(dimensionJTextField);
								jTextFieldLocationX.setText("像素 px");
								jTextFieldLocationX.setEnabled(false);
								jToolBar.add(jTextFieldLocationX);
							}
							{
								JLabel jLabel = new JLabel();
								jLabel.setText("位置Y：");
								jToolBar.add(jLabel);
							}
							{
								jTextFieldLocationY = new JTextField();
								jTextFieldLocationY.setMaximumSize(dimensionJTextField);
								jTextFieldLocationY.setText("像素 px");
								jTextFieldLocationY.setEnabled(false);
								jToolBar.add(jTextFieldLocationY);
							}
							{
								JButton jButton = new JButton();
								jButton.setText("确定");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										if (null != imageLayerSelected && isLayerAction) {
											double x = toDouble(jTextFieldLocationX);
											double y = toDouble(jTextFieldLocationY);
											layerTransform(imageLayerSelected, x, y, imageLayerSelected.getRotated());
											if (isLocationCenter) {
												isLocationCenter = false;
											}
											if (jCheckBoxMenuItemCenter.isSelected()) {
												jCheckBoxMenuItemCenter.setSelected(false);
											}
											drawResources();
										}
									}
								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							{
								JButton jButton = new JButton();
								jButton.setText("居中");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										if (null != imageLayerSelected && isLayerAction) {
											DimensionUtils size = imageLayerSelected.getSize();
											if (null != size && null != canvas) {
												double x = (canvas.getWidth() - size.getWidth()) / 2;
												double y = (canvas.getHeight() - size.getHeight()) / 2;
												imageLayerSelected.setLocation(x, y);
												jTextFieldLocationX.setText(String.valueOf(x));
												jTextFieldLocationY.setText(String.valueOf(y));
												drawResources();
											}
										}
									}
								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							jPanel.add(jToolBar);
						}
					}
					jPanelMain.add(jPanel, BorderLayout.SOUTH);
				}
			}
			getContentPane().add(jPanelMain, BorderLayout.CENTER);
		}
		int width = 1080;
		int height = 720;
		Dimension size = new Dimension(width, height);

		setSize(size);
		setResizable(false);
		setLocationRelativeTo(null);
	}

	private boolean isImageFile(String filePath) {
		if (null != filePath) {
			return fileNameMapUtils.isFileImage(new File(filePath));
		}
		return false;
	}

	private boolean isTreeNodeChild(DefaultMutableTreeNode parent, DefaultMutableTreeNode child) {
		if (null != parent && null != child) {
			DefaultMutableTreeNode treeNodeChild;
			Object object;
			Object objectChild;
			for (@SuppressWarnings("unchecked")
			Enumeration<DefaultMutableTreeNode> enumeration = parent.children(); enumeration.hasMoreElements();) {
				treeNodeChild = enumeration.nextElement();
				if (null != treeNodeChild) {
					if (child.equals(treeNodeChild)) {
						return true;
					} else {
						object = treeNodeChild.getUserObject();
						objectChild = child.getUserObject();
						if (null != object && null != objectChild && object.equals(objectChild)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private void layerExport(File file, ImageLayer layer) {
		if (null != layer) {
			if (null == propertiesProject) {
				propertiesProject = new Properties();
			}
			File imageFile;
			File propFile = null;
			String imagePath;
			Properties prop;
			PropertiesUtils pu = new PropertiesUtils();
			String commentLayer = "ImageLayer";
			Object obj;
			prop = pu.getPropertiesString(layer.getProperties());
			imagePath = layer.getImagePath();
			if (null != imagePath) {
				imageFile = new File(imagePath);
				if (null == file) {
					file = imageFile;
				}
				if (null != file && null != prop) {
					File temp;
					if (file.isDirectory()) {
						temp = file;
					} else {
						temp = file.getParentFile();
					}
					file = new File(temp, imageFile.getName());
					propFile = getFileLayer(file);
					propertiesUtils.storePropertiesXml(prop, propFile, tag_suffixes_layer, commentLayer,
							encoding_default);
					if (isExportProject) {
						obj = layer.getObject();
						if (null != obj) {
							propertiesProject.put(layer.getObject(), prop);
						}
					}
				}
			}
		}
	}

	private void layerExport(File file, List<ImageLayer> layerList) {
		if (null != layerList) {
			ImageLayer layer;
			if (null == propertiesProject) {
				propertiesProject = new Properties();
			}
			Properties prop;
			for (Iterator<ImageLayer> iterator = layerList.iterator(); iterator.hasNext();) {
				layer = iterator.next();
				if (null != layer) {
					layerExport(file, layer);
				}
			}
			if (null != file && isExportProject) {
				String commentProject = "PixelShader Project";
				prop = treeModelUtils.storeNode((DefaultTreeModel) jTreeRoot.getModel());
				propertiesProject = updateProperties(prop, propertiesProject);
				propertiesUtils.storePropertiesXml(propertiesProject, getFileProject(file), tag_suffixes_project,
						commentProject, encoding_default);
			}
		}
	}

	private void layerTransform(ImageLayer imageLayer, double x, double y, double rotate) {
		if (null != imageLayer) {
			int index = layerListRoot.indexOf(imageLayer);
			PointUtils location = imageLayer.getLocation();
			double rateX = 0;
			double rateY = 0;
			if (null != location) {
				double xLayer = location.getX();
				double yLayer = location.getY();
				rateX = x - xLayer;
				rateY = y - yLayer;
			}
			if (rateX != 0 || rateY != 0) {
				imageLayer.setLocation(x, y);
			}
			location = imageLayer.getLocation();
			PointUtils root = imageLayer.getRoot();
			PointUtils anchor = imageLayer.getAnchor();
			if (null != root && null != anchor && !anchor.equals(root)) {
				imageLayer.setAnchor(root);
			}
			anchor = imageLayer.getAnchor();
			double xAnchor = 0;
			double yAnchor = 0;
			if (null != location && null != anchor) {
				xAnchor = location.getX() + anchor.getX();
				yAnchor = location.getY() + anchor.getY();
			}
			double rotateLayer = imageLayer.getRotated();
			double rateRotate = rotate - rotateLayer;
			if (rotate != rotateLayer) {
				imageLayer.setRotated(rotate);
			}
			if (index != -1) {
				layerListRoot.set(index, imageLayer);
			}
			layerTransformChilds(imageLayer, rateX, rateY, rateRotate, xAnchor, yAnchor);
		}
	}

	private void layerTransformChild(ImageLayer layer, double rateX, double rateY, double rateRotate, double xAnchor,
			double yAnchor) {
		if (null != layer) {
			int index;
			PointUtils locationChild = layer.getLocation();
			if (rateX != 0 || rateY != 0) {
				double xc;
				double yc;
				if (null != locationChild) {
					xc = locationChild.getX() + rateX;
					yc = locationChild.getY() + rateY;
				} else {
					xc = rateX;
					yc = rateY;
				}
				layer.setLocation(xc, yc);
			}
			locationChild = layer.getLocation();
			if (null != locationChild && rateRotate != 0) {
				PointUtils anchorChild = layer.getAnchor();
				if (null != anchorChild) {
					layer.setAnchor(xAnchor - locationChild.getX(), yAnchor - locationChild.getY());
				}
				layer.setRotated(layer.getRotated() + rateRotate);
			}
			index = layerListRoot.indexOf(layer);
			if (index != -1) {
				layerListRoot.set(index, layer);
			}
		}
	}

	private void layerTransformChilds(ImageLayer imageLayer, double rateX, double rateY, double rateRotate,
			double xAnchor, double yAnchor) {
		if (null != imageLayer) {
			Object object = imageLayer.getObject();
			if (null != object) {
				DefaultTreeModel modelRoot = (DefaultTreeModel) jTreeRoot.getModel();
				if (null != modelRoot) {
					DefaultMutableTreeNode treeNodeRoot = (DefaultMutableTreeNode) modelRoot.getRoot();
					DefaultMutableTreeNode treeNode = treeNodeUtils.getTreeNode(treeNodeRoot, object);
					if (null != treeNode) {
						int count = treeNode.getChildCount();
						if (count <= 0) {
							return;
						}
						DefaultMutableTreeNode treeNodeChild;
						ImageLayer layer;
						for (@SuppressWarnings("unchecked")
						Enumeration<DefaultMutableTreeNode> treeNodeEnumeration = treeNode
								.children(); treeNodeEnumeration.hasMoreElements();) {
							treeNodeChild = treeNodeEnumeration.nextElement();
							if (null != treeNodeChild) {
								object = treeNodeChild.getUserObject();
								if (null != object) {
									layer = fileUtils.getImageLayer(layerListRoot, object);
									if (treeNodeChild.getChildCount() != 0) {
										layerTransformChilds(layer, rateX, rateY, rateRotate, xAnchor, yAnchor);
									}
									layerTransformChild(layer, rateX, rateY, rateRotate, xAnchor, yAnchor);
								}
							}
						}
					}
				}
			}
		}
	}

	private void loadLayer(List<File> fileList) {
		if (null != fileList) {
			layerListRoot = updateListLayer(layerListRoot, fileList);
			updateGraphics();
		}
	}

	private void loadPsdLayer(File[] array) {
		if (null != array) {
			PSDFileReader psdFileReader = new PSDFileReader();
			List<ImageLayer> layerList = new ArrayList<>();
			for (int i = 0, iLength = array.length; i < iLength; i++) {
				layerList = psdFileReader.read(array[i], layerList);
			}
			removeRepetition(layerListRoot, layerList);
			layerListRoot = layerList;
			updateGraphics();
		}
	}

	private File[] openFiles(int filterStyle) {
		if (null == jFileChooser) {
			jFileChooser = new JFileChooser();
		}
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		FileNameExtensionFilter filter = null;
		jFileChooser.resetChoosableFileFilters();
		if (filterStyle == fileFilterStyle_Image || filterStyle == fileFilterStyle_Images
				|| filterStyle == fileFilterStyle_Preview) {
			filter = new FileNameExtensionFilter("Images", "bmp", "jpg", "gif", "png");
		} else if (filterStyle == fileFilterStyle_Layer || filterStyle == fileFilterStyle_Layers) {
			filter = new FileNameExtensionFilter("PixelShader ImageLayer", "pxl");
		} else if (filterStyle == fileFilterStyle_PSD) {
			filter = new FileNameExtensionFilter("Photoshop Document", "psd");
		} else if (filterStyle == fileFilterStyle_Project) {
			filter = new FileNameExtensionFilter("EvolutionVector Project", "evp");
		}
		jFileChooser.setFileFilter(filter);
		jFileChooser.setMultiSelectionEnabled(true);
		int returnVal = jFileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.CANCEL_OPTION) {
			return null;
		}
		File[] fileArray = jFileChooser.getSelectedFiles();
		return fileArray;
	}

	private void removeNode(DefaultMutableTreeNode treeNode) {
		if (null != treeNode && null != layerListRoot) {
			DefaultTreeModel modelRoot = (DefaultTreeModel) jTreeRoot.getModel();
			modelRoot.removeNodeFromParent(treeNode);
			Object object = treeNode.getUserObject();
			if (null != object) {
				layerListRoot.remove(fileUtils.getImageLayer(layerListRoot, object));
			}
			if (treeNode.getChildCount() != 0) {
				DefaultMutableTreeNode node;
				for (@SuppressWarnings("unchecked")
				Enumeration<DefaultMutableTreeNode> treeNodeEnumeration = treeNode
						.preorderEnumeration(); treeNodeEnumeration.hasMoreElements();) {
					node = treeNodeEnumeration.nextElement();
					object = node.getUserObject();
					if (null != object) {
						layerListRoot.remove(fileUtils.getImageLayer(layerListRoot, object));
					}
				}
			}
		}
	}

	private void removeRepetition(List<ImageLayer> src, List<ImageLayer> dest) {
		if (null != src && null != dest) {
			ImageLayer layer;
			Object path;
			for (Iterator<ImageLayer> iterator = src.iterator(); iterator.hasNext();) {
				layer = iterator.next();
				if (null != layer) {
					path = layer.getImagePath();
					if (dest.indexOf(path) == -1) {
						dest.add(layer);
					}
				}
			}
		}
	}

	private void setImageLayerInfo(ImageLayer imageLayer) {
		String locationX;
		String locationY;
		String width = null;
		String height = null;
		String scaleMax;
		String angle;
		String anchorX = null;
		String anchorY = null;
		String alphaFloat;
		String depthLayer;
		String canvasX;
		String canvasY;
		String gravity;
		if (null != imageLayer) {
			PointUtils location = imageLayer.getLocation();
			double x;
			double y;
			if (null != location) {
				x = location.getX();
				y = location.getY();
			} else {
				x = 0;
				y = 0;
			}
			locationX = String.valueOf(x);
			locationY = String.valueOf(y);
			DimensionUtils size = imageLayer.getSize();
			double w;
			double h;
			if (null != size) {
				w = size.getWidth();
				h = size.getHeight();
			} else {
				w = 0;
				h = 0;
			}
			width = String.valueOf(w);
			height = String.valueOf(h);
			double s = 1;
			DimensionUtils scale = imageLayer.getScale();
			if (null != scale) {
				s = Math.max(scale.getWidth(), scale.getHeight());
			}
			scaleMax = String.valueOf(s);
			angle = String.valueOf(imageLayer.getRotated());
			PointUtils anchor = imageLayer.getAnchor();
			if (null != anchor) {
				anchorX = String.valueOf(anchor.getX() + x);
				anchorY = String.valueOf(anchor.getY() + y);
			}
			if (imageLayer.isVisible()) {
				jCheckBoxVisible.setSelected(true);
			} else {
				jCheckBoxVisible.setSelected(false);
			}
			alphaFloat = String.valueOf(imageLayer.getAlpha());
			depthLayer = String.valueOf(imageLayer.getDepth());
			gravity = String.valueOf(imageLayer.getGravity());
		} else {
			String px = "0 px";
			locationX = px;
			locationY = px;
			width = px;
			height = px;
			scaleMax = "1倍";
			angle = "0度";
			anchorX = px;
			anchorY = px;
			alphaFloat = "1.0f";
			depthLayer = "0 层";
			gravity = "0度";
		}
		if (null != canvas) {
			canvasX = String.valueOf(canvas.getWidth());
			canvasY = String.valueOf(canvas.getHeight());
		} else {
			canvasX = String.valueOf(jPanelGraphics.getWidth());
			canvasY = String.valueOf(jPanelGraphics.getHeight());
		}
		jTextFieldAlpha.setText(alphaFloat);
		jTextFieldAnchorX.setText(anchorX);
		jTextFieldAnchorY.setText(anchorY);
		jTextFieldCanvasX.setText(canvasX);
		jTextFieldCanvasY.setText(canvasY);
		jTextFieldDepth.setText(depthLayer);
		jTextFieldSizeHeight.setText(height);
		jTextFieldAngle.setText(angle);
		jTextFieldScale.setText(scaleMax);
		jTextFieldLocationX.setText(locationX);
		jTextFieldLocationY.setText(locationY);
		jTextFieldSizeWidth.setText(width);
		jTextFieldGravity.setText(gravity);
	}

	private void setToolkitEnabled(boolean isEnabled) {
		if (isEnabled) {
			if (!jCheckBoxVisible.isEnabled()) {
				jCheckBoxVisible.setEnabled(true);
			}
			if (!jCheckBoxGravity.isEnabled()) {
				jCheckBoxGravity.setEnabled(true);
			}
			if (!jTextFieldAlpha.isEnabled()) {
				jTextFieldAlpha.setEnabled(true);
			}
			if (!jTextFieldAnchorX.isEnabled()) {
				jTextFieldAnchorX.setEnabled(true);
			}
			if (!jTextFieldAnchorY.isEnabled()) {
				jTextFieldAnchorY.setEnabled(true);
			}
			if (!jTextFieldDepth.isEnabled()) {
				jTextFieldDepth.setEnabled(true);
			}
			if (!jTextFieldSizeHeight.isEnabled()) {
				jTextFieldSizeHeight.setEnabled(true);
			}
			if (!jTextFieldLocationX.isEnabled()) {
				jTextFieldLocationX.setEnabled(true);
			}
			if (!jTextFieldLocationY.isEnabled()) {
				jTextFieldLocationY.setEnabled(true);
			}
			if (!jTextFieldAngle.isEnabled()) {
				jTextFieldAngle.setEnabled(true);
			}
			if (!jTextFieldGravity.isEnabled()) {
				jTextFieldGravity.setEnabled(true);
			}
			if (!jTextFieldScale.isEnabled()) {
				jTextFieldScale.setEnabled(true);
			}
			if (!jTextFieldSizeWidth.isEnabled()) {
				jTextFieldSizeWidth.setEnabled(true);
			}
		} else {
			if (jCheckBoxVisible.isEnabled()) {
				jCheckBoxVisible.setEnabled(false);
			}
			if (jCheckBoxGravity.isEnabled()) {
				jCheckBoxGravity.setEnabled(false);
			}
			if (jTextFieldAlpha.isEnabled()) {
				jTextFieldAlpha.setEnabled(false);
			}
			if (jTextFieldAnchorX.isEnabled()) {
				jTextFieldAnchorX.setEnabled(false);
			}
			if (jTextFieldAnchorY.isEnabled()) {
				jTextFieldAnchorY.setEnabled(false);
			}
			if (jTextFieldDepth.isEnabled()) {
				jTextFieldDepth.setEnabled(false);
			}
			if (jTextFieldSizeHeight.isEnabled()) {
				jTextFieldSizeHeight.setEnabled(false);
			}
			if (jTextFieldLocationX.isEnabled()) {
				jTextFieldLocationX.setEnabled(false);
			}
			if (jTextFieldLocationY.isEnabled()) {
				jTextFieldLocationY.setEnabled(false);
			}
			if (jTextFieldAngle.isEnabled()) {
				jTextFieldAngle.setEnabled(false);
			}
			if (jTextFieldGravity.isEnabled()) {
				jTextFieldGravity.setEnabled(false);
			}
			if (jTextFieldScale.isEnabled()) {
				jTextFieldScale.setEnabled(false);
			}
			if (jTextFieldSizeWidth.isEnabled()) {
				jTextFieldSizeWidth.setEnabled(false);
			}
		}
	}

	private void storeResources(int filterStyle) {
		File[] fileArray = openFiles(filterStyle);
		if (null == fileArray) {
			return;
		}
		File file = fileArray[0];
		File dir = file.getParentFile();
		if (null == dir) {
			return;
		}
		if (filterStyle == fileFilterStyle_Images) {
			imagesExport(dir);
		} else if (filterStyle == fileFilterStyle_Layers) {
			isExportProject = false;
			layerExport(file, layerListRoot);
		} else if (filterStyle == fileFilterStyle_Preview) {
			Calendar calendar = new GregorianCalendar();
			StringBuffer sbuf = new StringBuffer();
			sbuf.append("layerCombined-");
//				sbuf.append(calendar.get(Calendar.YEAR));
//				sbuf.append(calendar.get(Calendar.MONTH));
			sbuf.append(calendar.get(Calendar.DAY_OF_MONTH));
			sbuf.append(calendar.get(Calendar.HOUR_OF_DAY));
			sbuf.append(calendar.get(Calendar.MINUTE));
			sbuf.append(calendar.get(Calendar.SECOND));
			sbuf.append(calendar.get(Calendar.MILLISECOND));
			sbuf.append(tag_suffixes_image);
			if (null != layerImage) {
				BufferedImage image = layerImage;
				if (!isDrawCanvas) {
					image = layerImage.getSubimage(layerListRootRectangleMin.x, layerListRootRectangleMin.y,
							layerListRootRectangleMin.width, layerListRootRectangleMin.height);
				}
				imageExport(image, null, new File(dir, sbuf.toString()));
			}
		} else if (filterStyle == fileFilterStyle_Image) {
			File temp = new File(imageLayerSelected.getImagePath());
			imageExport(imageLayerSelected.getImage(), null, new File(dir, temp.getName()));
		} else if (filterStyle == fileFilterStyle_Layer) {
			layerExport(file, imageLayerSelected);
		} else if (filterStyle == fileFilterStyle_Project) {
			isExportProject = true;
			imagesExport(dir);
			layerExport(file, layerListRoot);
		} else if (filterStyle == fileFilterStyle_PSD) {

		}
	}

	private double toDouble(JTextField jTextField) {
		String string = toStringDigital(jTextField);
		if (null != string) {
			return Double.valueOf(toStringDigital(jTextField));
		}
		return 0;
	}

	private String toStringDigital(JTextField jTextField) {
		String string = null;
		if (null != jTextField) {
			string = jTextField.getText();
			String regex = "\\D*(\\d+\\.*\\d*)\\D*";
			if (string.matches(regex)) {
				String replacement = "$1";
				return string.replaceAll(regex, replacement);
			}
		}
		return string;
	}

	private void treeNodeMoveTo(DefaultMutableTreeNode treeNodeDragged, DefaultMutableTreeNode treeNodeMoved,
			boolean isChildAdd) {
		DefaultMutableTreeNode treeNodeParentDragged = null;
		DefaultMutableTreeNode treeNodeRoot = null;
		if (null != treeNodeDragged) {
			treeNodeParentDragged = (DefaultMutableTreeNode) treeNodeDragged.getParent();
			treeNodeRoot = (DefaultMutableTreeNode) treeNodeDragged.getRoot();
		}
		DefaultMutableTreeNode treeNodeParentMoved = null;
		if (null != treeNodeMoved) {
			treeNodeParentMoved = (DefaultMutableTreeNode) treeNodeMoved.getParent();
		}
		if (null == treeNodeParentDragged || null == treeNodeRoot || treeNodeDragged == treeNodeRoot
				|| (treeNodeMoved.isNodeAncestor(treeNodeDragged)) || treeNodeMoved.isNodeChild(treeNodeDragged)) {
			return;
		}
		DefaultMutableTreeNode treeNodeParent;
		int index;
		if (isChildAdd) {
			if (null == treeNodeParentMoved) {
				treeNodeParent = treeNodeRoot;
				index = treeNodeParent.getChildCount();
			} else {
				if (treeNodeMoved.getAllowsChildren() && isChildAdd) {
					treeNodeParent = treeNodeMoved;
					index = treeNodeParent.getChildCount();
				} else {
					treeNodeParent = treeNodeParentMoved;
					index = treeNodeParent.getIndex(treeNodeMoved);
				}
			}
		} else {
			if (treeNodeParentMoved == treeNodeParentDragged) {
				treeNodeParent = treeNodeParentMoved;
				index = treeNodeParent.getIndex(treeNodeMoved);
			} else {
				return;
			}
		}
		if (treeNodeParent.getAllowsChildren()) {
			DefaultTreeModel modelRoot = (DefaultTreeModel) jTreeRoot.getModel();
			modelRoot.removeNodeFromParent(treeNodeDragged);
			modelRoot.insertNodeInto(treeNodeDragged, treeNodeParent, index);
			modelRoot.nodeChanged(treeNodeParent);
			Object object = treeNodeDragged.getUserObject();
			if (null != object) {
				ImageLayer layer = fileUtils.getImageLayer(layerListRoot, object);
				if (null != layer) {
					index = layerListRoot.indexOf(layer);
					if (index != -1) {
						layerListRoot.set(index, layer);
					}
				}
			}
		}
	}

	private void updateGraphics() {
		updateJTree();
		drawResources();
	}

	private void updateImageLayerDepth(List<ImageLayer> layerList, int depthOld, int depthNew) {
		if (null != layerList && depthOld != depthNew) {
			ImageLayer layerOld = fileUtils.getImageLayer(layerList, depthOld);
			int indexOld = layerList.indexOf(layerOld);
			ImageLayer layerNew = fileUtils.getImageLayer(layerList, depthNew);
			int indexNew = layerList.indexOf(layerNew);
			if (null != layerOld) {
				layerOld.setDepth(depthNew);
				if (indexOld != -1) {
					layerList.set(indexOld, layerOld);
				}
			}
			if (null != layerNew) {
				layerNew.setDepth(depthOld);
				if (indexNew != -1) {
					layerList.set(indexNew, layerNew);
				}
			}
		}
	}

	private void updateJTree() {
		if (null != jTreeRoot && null != layerListRoot) {
			DefaultTreeModel modelRoot = (DefaultTreeModel) jTreeRoot.getModel();
			updateTreeNode(modelRoot, layerListRoot);
			if (isUpdate) {
				if (null != modelProperties) {
					updateTreeNode(modelRoot, modelProperties);
				}
			}
		}
	}

	private List<ImageLayer> updateListLayer(List<ImageLayer> layerList, List<File> fileList) {
		if (null != fileList) {
			if (null == layerList) {
				layerList = new ArrayList<>();
			}
			File file;
			ImageLayer imageLayer;
			ImageLayer layer;
			String name;
			String fileName;
			String imagePath = "imagePath";
			Properties propLayer;
			Properties prop = null;
			LayerNode node;
			for (Iterator<File> iterator = fileList.iterator(); iterator.hasNext();) {
				file = iterator.next();
				if (null != file) {
					fileName = file.getName().toLowerCase();
					if (fileName.endsWith(tag_suffixes_project)) {
						propLayer = propertiesUtils.loadPropertiesXml(file, tag_suffixes_project);
						if (null != propLayer) {
							isUpdate = true;
							modelProperties = new Properties();
							Set<Object> keySet = propLayer.keySet();
							Object key;
							Object value;
							TreeSet<LayerNode> treeSet = new TreeSet<>(getComparatorByProperties());
							for (Iterator<Object> iteratorKey = keySet.iterator(); iteratorKey.hasNext();) {
								key = iteratorKey.next();
								if (null != key && key instanceof String) {
									key = objectUtils.getObject((String) key);
									if (null != key && key instanceof LayerNode) {
										treeSet.add((LayerNode) key);
									}
								}
							}
//							System.out.println(Arrays.toString(treeSet.toArray()));
							for (Iterator<LayerNode> iteratorTree = treeSet.iterator(); iteratorTree.hasNext();) {
								key = iteratorTree.next();
								if (null != key) {
									key = key.toString();
									value = propLayer.get(key);
									if (key instanceof LayerNode) {
										node = (LayerNode) key;
										modelProperties.put(node.getID(), node);
									}
									if (null != value) {
										if (value instanceof String) {
											value = objectUtils.getProperties((String) value);
										}
										if (value instanceof Properties) {
											prop = (Properties) value;
											file = fileUtils.getFile(fileList, prop.getProperty(imagePath));
											if (null == file) {
												imageLayer = new ImageLayer();
												imageLayer.setProperties(prop);
												name = imageLayer.getName() + tag_at + imageLayer.hashCode();
												imageLayer.setObject(name);
												layerList.add(imageLayer);
											}
										}
									}
								}
							}
						} else {
							isUpdate = false;
						}
					} else if (fileName.endsWith(tag_suffixes_layer)) {
						prop = propertiesUtils.loadPropertiesXml(file, tag_suffixes_layer);
						if (null != prop) {
							file = fileUtils.getFile(fileList, prop.getProperty(imagePath));
							if (null == file) {
								imageLayer = new ImageLayer();
								imageLayer.setProperties(prop);
								name = imageLayer.getName() + tag_at + imageLayer.hashCode();
								imageLayer.setObject(name);
								layerList.add(imageLayer);
							}
						}
					} else {
						if (isImageFile(fileName)) {
							layer = fileUtils.getImageLayer(layerList, file);
							if (null == layer) {
								imageLayer = new ImageLayer();
								imageLayer.read(file);
								layerList.add(imageLayer);
							}
						}
					}
				}
			}
		}
		return layerList;
	}

	private Properties updateProperties(Properties src, Properties dest) {
		if (null != src && null != dest) {
			Set<Object> kset = src.keySet();
			Object key;
			Object value;
			LayerNode node;
			Object object;
			Properties properties = new Properties();
			for (Iterator<Object> iterator = kset.iterator(); iterator.hasNext();) {
				key = iterator.next();
				if (null != key) {
					value = src.get(key);
					if (null != value) {
						if (value instanceof String) {
							value = objectUtils.getObject(value.toString());
						}
						if (null != value && value instanceof LayerNode) {
							node = (LayerNode) value;
							object = node.getObject();
							if (null != object) {
								value = dest.get(object);
								if (null != value) {
									key = node;
									properties.put(key, value);
								}
							}
						}
					}
				}
			}
			return properties;
		}
		return null;
	}

	private void updateResources(JTree jTree) {
		if (null != jTree) {
			DefaultTreeModel treeModel = (DefaultTreeModel) jTree.getModel();
			DefaultMutableTreeNode treeNodeRoot = (DefaultMutableTreeNode) treeModel.getRoot();
			DefaultMutableTreeNode treeNode;
			DefaultMutableTreeNode treeNodeParent;
			ImageLayer layer;
			ImageLayer layerSuper;
			Object object;
			int depth;
			int depthMax = fileUtils.getImageLayerDepthMax(layerListRoot) + 1;
			int index;
//			for (@SuppressWarnings("unchecked")
//			Enumeration<DefaultMutableTreeNode> treeNodeEnumeration = treeNodeRoot
//			.preorderEnumeration(); treeNodeEnumeration.hasMoreElements();) {
			for (@SuppressWarnings("unchecked")
			Enumeration<DefaultMutableTreeNode> treeNodeEnumeration = treeNodeRoot
					.breadthFirstEnumeration(); treeNodeEnumeration.hasMoreElements();) {
				treeNode = treeNodeEnumeration.nextElement();
				if (null != treeNode) {
					object = treeNode.getUserObject();
					if (null != object) {
						layer = fileUtils.getImageLayer(layerListRoot, object);
						index = layerListRoot.indexOf(layer);
						treeNodeParent = (DefaultMutableTreeNode) treeNode.getParent();
						if (null != treeNodeParent) {
							object = treeNodeParent.getUserObject();
							if (null != object) {
								layerSuper = fileUtils.getImageLayer(layerListRoot, object);
								if (!treeNodeParent.isRoot() && null != layerSuper) {
									object = layerSuper.getObject();
								}
								layer.setObjectSuper(object);
							}
						}
						if (null != layer) {
							depth = layer.getDepth();
							if (depth == -1) {
								layer.setDepth(depthMax++);
								if (index != -1) {
									layerListRoot.set(index, layer);
								}
							}
						}
					}
				}
			}
		}
	}

	private void updateTreeNode(DefaultTreeModel treeModel, List<ImageLayer> layerList) {
		if (null != layerList) {
			DefaultMutableTreeNode nodeRoot;
			if (null == treeModel) {
				nodeRoot = new DefaultMutableTreeNode();
				treeModel = new DefaultTreeModel(nodeRoot);
			} else {
				nodeRoot = (DefaultMutableTreeNode) treeModel.getRoot();
			}
			DefaultMutableTreeNode node;
			ImageLayer imageLayer;
			for (Iterator<ImageLayer> iterator = layerList.iterator(); iterator.hasNext();) {
				imageLayer = iterator.next();
				if (null != imageLayer) {
					node = new DefaultMutableTreeNode();
					node.setUserObject(imageLayer.getObject());
					if (!isTreeNodeChild(nodeRoot, node)) {
						treeModel.insertNodeInto(node, nodeRoot, nodeRoot.getChildCount());
						treeModel.nodeChanged(nodeRoot);
					}
				}
			}
		}
	}

	private void updateTreeNode(DefaultTreeModel modelRoot, Properties properties) {
		treeModelUtils.loadNode(modelRoot, properties);
	}

	private void warning() {
		String description = "当前操作将销毁现有数据，请选择【是】保存工程！";
		int messageStyle = JOptionPane.showConfirmDialog(null, description);// yes=0;no=1;cancel=2
		if (messageStyle == 0) {
			if (null != layerListRoot && layerListRoot.size() != 0) {
				storeResources(fileFilterStyle_Project);
				description = "操作完成！";
			} else {
				description = "项目为空！";
			}
			JOptionPane.showMessageDialog(null, description);
		} else if (messageStyle == 1) {
			description = "原有项目未保存！";
			JOptionPane.showMessageDialog(null, description);
		} else if (messageStyle == 2) {
			return;
		}
	}
}
