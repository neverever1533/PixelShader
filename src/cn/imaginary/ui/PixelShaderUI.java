package cn.imaginary.ui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import java.io.File;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

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
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import cn.imaginary.toolkit.image.ImageLayer;
import cn.imaginary.toolkit.io.PSDFileReader;

import javaev.awt.Graphics2DUtils;

import javaev.imageio.ImageIOUtils;

import javaev.io.FileUtils;

import javaev.lang.StringUtils;

import javaev.swing.DefaultTreeCellRendererUtils;
import javaev.swing.JPanelDnD;

import javaev.util.CollectionsUtils;
import javaev.util.PropertiesUtils;

public class PixelShaderUI extends JFrame {
	/**
	 * @author Sureness
	 * @since 0.1
	 */
	private static final long serialVersionUID = 4247417000065494012L;

	public static void main(String[] args) {
		PixelShaderUI tui = new PixelShaderUI();
		tui.setVisible(true);
	}

	private Color bgColor = Color.black;

	private Dimension canvas;

	private String cardEditor = "editor";
	private String cardGraphics = "graphics";

	private CardLayout cardLayout;

	private CollectionsUtils collectionsUtils = new CollectionsUtils();

	private Color fgColor = Color.white;

	private int fileFilterStyle_Image = 8;
	private int fileFilterStyle_Layer = 5;
	private int fileFilterStyle_Project = 0;
	private int fileFilterStyle_PS = 2;

	public String fileSuffixes_Image = ".png";
	public String fileSuffixes_Layer = ".pxl";
	public String fileSuffixes_Project = ".evp";

	private FileUtils fileUtils = FileUtils.getInstance();

	private Graphics2DUtils graphics2dUtils = new Graphics2DUtils();

	private ImageIOUtils imageIOUtils = new ImageIOUtils();

	private ImageLayer imageLayerSelected;

	private double indexScaled = 1;
	private double indexScaledDefault = 0.25;
	private double indexScaledMax = 2;
	private double indexScaledMin = 0.25;

	private boolean isChildAdd = false;
	private boolean isExport = false;
	private boolean isLayerRotated = false;
	private boolean isLocationCenter;

	private JCheckBoxMenuItem jCheckBoxMenuItemCenter;

	private JCheckBox jCheckBoxVisible;

	private JFileChooser jFileChooser;

	private JLabel jLabelView;

	private JPanel jPanelGraphics;;

	private JTextField jTextFieldAlpha;
	private JTextField jTextFieldAnchorX;
	private JTextField jTextFieldAnchorY;
	private JTextField jTextFieldAngle;
	private JTextField jTextFieldCanvasX;
	private JTextField jTextFieldCanvasY;
	private JTextField jTextFieldDepth;
	private JTextField jTextFieldLocationX;
	private JTextField jTextFieldLocationY;
	private JTextField jTextFieldScale;
	private JTextField jTextFieldSizeHeight;
	private JTextField jTextFieldSizeWidth;

	private JTree jTreeRoot;

	private BufferedImage layerImage;

	private List<ImageLayer> layerListRoot;

	private DefaultTreeCellRendererUtils treeCellRenderer;

	public PixelShaderUI() {
		super();
		setTitle("PixelShader ver.40.3d17.pa1a_alpha by Sev末夜");
		initGUI();
	}

	private void createNewProject() {
		double widthCanvas = 1024;
		double heightCanvas = 1024;
		jTextFieldCanvasX.setText(String.valueOf(widthCanvas));
		jTextFieldCanvasY.setText(String.valueOf(heightCanvas));
		if (null == canvas) {
			canvas = new Dimension();
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

	private void drawImageLayers(JPanel jPanel, List<ImageLayer> imageLayerList) {
		if (null != imageLayerList) {
			int widthRoot;
			int heightRoot;
			if (null != canvas) {
				widthRoot = canvas.width;
				heightRoot = canvas.height;
			} else {
				widthRoot = jPanel.getWidth();
				heightRoot = jPanel.getHeight();
				canvas = new Dimension();
				canvas.setSize(widthRoot, heightRoot);
			}
			Graphics g = jPanel.getGraphics();
			jPanel.update(g);
			Graphics2D graphics2d = (Graphics2D) g.create();
			AffineTransform at = new AffineTransform();
			at.scale(indexScaled, indexScaled);
			BufferedImage imageRoot = new BufferedImage(widthRoot, heightRoot, BufferedImage.TYPE_4BYTE_ABGR);
			ImageObserver observer = null;
			float alpha = 1.0f;
			ImageLayer layer;
			Point anchor;
			double angle;
			double theta;
			Point location;
			Dimension scale;
			BufferedImage image = null;
			double x;
			double y;
			AffineTransform xform;
			Graphics2D g2d;
			for (Iterator<ImageLayer> iterator = imageLayerList.iterator(); iterator.hasNext();) {
				layer = iterator.next();
				if (null != layer && layer.isVisible()) {
					image = layer.getImage();
					if (null != image) {
						g2d = imageRoot.createGraphics();
						xform = new AffineTransform();
						anchor = layer.getAnchor();
						angle = layer.getAngleRotated();
						location = layer.getLocation();
						scale = layer.getScale();
						if (isLocationCenter) {
							layer.setLocation((widthRoot - image.getWidth()) / 2, (heightRoot - image.getHeight()) / 2);
						}
						if (null != location) {
							x = location.getX();
							y = location.getY();
							xform.translate(x, y);
						}
						if (angle != 0) {
							theta = Math.toRadians(angle);
							if (null == anchor) {
								xform.rotate(theta);
							} else {
								xform.rotate(theta, anchor.getX(), anchor.getY());
							}
						}
						if (null != scale) {
							xform.scale(scale.getWidth(), scale.getHeight());
						}
						if (layer.isAlpha()) {
							alpha = layer.getAlpha();
							if (alpha >= 0 && alpha <= 1) {
								AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
										alpha);
								g2d.setComposite(alphaComposite);
							}
						}
						image = graphics2dUtils.filterImage(image, null, xform);
						g2d.drawImage(image, null, observer);
						g2d.dispose();
						graphics2d.drawImage(imageRoot, at, observer);
					}
				}
			}
			layerImage = imageRoot;
			graphics2d.drawRect(0, 0, (int) (widthRoot * indexScaled), (int) (heightRoot * indexScaled));
			graphics2d.drawString(String.valueOf(indexScaled), 15, 25);
			graphics2d.dispose();
		}
	}

	private void drawResources() {
		if (null != jPanelGraphics && null != layerListRoot) {
			treeCellRenderer.setResourcesList(layerListRoot);
			Comparator<ImageLayer> comparator = getComparatorByLayerDepth();
			Collections.sort(layerListRoot, comparator);
			updateResources(jTreeRoot);
			drawImageLayers(jPanelGraphics, layerListRoot);
		}
	}

	private Comparator<ImageLayer> getComparatorByLayerDepth() {
		Comparator<ImageLayer> comparator = new Comparator<ImageLayer>() {

			@Override
			public int compare(ImageLayer o1, ImageLayer o2) {
				return o2.getDepth() - o1.getDepth();
			}
		};
		return comparator;
	}

	private File getFileLayer(File file) {
		return fileUtils.getFile(file, fileSuffixes_Layer);
	}

	private File getFileProject(File file) {
		return fileUtils.getFile(file, fileSuffixes_Project);
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

	private DefaultMutableTreeNode getTreeNode(JTree jTree, Object object) {
		if (null != jTree) {
			DefaultTreeModel modelRoot = (DefaultTreeModel) jTree.getModel();
			if (null != modelRoot) {
				DefaultMutableTreeNode treeNodeRoot = (DefaultMutableTreeNode) modelRoot.getRoot();
				if (null != treeNodeRoot) {
					DefaultMutableTreeNode treeNode;
					Object objectTreeNode;
					for (@SuppressWarnings("unchecked")
					Enumeration<TreeNode> treeNodeEnumeration = treeNodeRoot.preorderEnumeration(); treeNodeEnumeration
							.hasMoreElements();) {
						treeNode = (DefaultMutableTreeNode) treeNodeEnumeration.nextElement();
						if (null != treeNode) {
							objectTreeNode = treeNode.getUserObject();
							if (null != objectTreeNode && objectTreeNode.equals(object)) {
								return treeNode;
							}
						}
					}
				}
			}
		}
		return null;
	}

	private String getVersionInfo() {
		StringBuffer description = new StringBuffer();
		String ln = StringUtils.Line_Separator;
		description.append("application:PixelShader");
		description.append(ln);
		description.append("author:Sev末夜");
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

	public void imageExport(BufferedImage image, File file) {
		imageIOUtils.write(image, ImageIOUtils.FormatName_Default, file);
	}

	public void imagesExport(List<ImageLayer> layerList, File file) {
		if (null != layerList && !layerList.isEmpty()) {
			int index = 0;
			ImageLayer layer;
			BufferedImage image;
			for (Iterator<ImageLayer> iterator = layerList.iterator(); iterator.hasNext();) {
				layer = iterator.next();
				if (null != layer) {
					if (null == file) {
						file = new File(layer.getImagePath());
					}
					file = fileUtils.getFile(file, index++, ImageIOUtils.FileSuffixes_Default);
					image = layer.getImage();
					imageExport(image, file);
				}
			}
		}
	}

	private void initGUI() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		{
			JMenuBar jMenuBar = new JMenuBar();
//			jMenuBar.setBackground(Color.white);
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
//							System.out.println(JMenuItemNew.getText());
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
						jMenuItem.setText("添加PNG到图层");
//						jMenuItem.setMnemonic(KeyEvent.VK_I);
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('i'));
//						jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
//								System.out.println(jMenuItem.getText());
								loadLayer(Arrays.asList(openFiles(fileFilterStyle_Image)));
								drawResources();
							}
						};
						jMenuItem.addActionListener(al);
						jMenuInport.add(jMenuItem);
					}
					{
						JMenuItem jMenuItem = new JMenuItem();
						jMenuItem.setText("添加Layer到图层");
//						jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('o'));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
//								System.out.println(jMenuItem.getText());
								loadLayer(Arrays.asList(openFiles(fileFilterStyle_Layer)));
								drawResources();
							}
						};
						jMenuItem.addActionListener(al);
						jMenuInport.add(jMenuItem);
					}
					{
						JMenuItem jMenuItem = new JMenuItem();
						jMenuItem.setText("添加工程到图层");
//						jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('p'));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
//								System.out.println(jMenuItem.getText());
								loadLayer(Arrays.asList(openFiles(fileFilterStyle_Project)));
								drawResources();
							}
						};
						jMenuItem.addActionListener(al);
						jMenuInport.add(jMenuItem);
					}
					{
						JMenuItem jMenuItem = new JMenuItem();
						jMenuItem.setText("添加PSD到图层");
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('u'));
//						jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
//								System.out.println(jMenuItem.getText());
								loadPsdLayer(openFiles(fileFilterStyle_PS));
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
						jMenuItem.setText("图层合并为PNG");
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('e'));
//						jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
//								System.out.println(jMenuItem.getText());
								String description;
								if (null != layerListRoot && layerListRoot.size() != 0) {
									storeResources(openFiles(fileFilterStyle_Image), 8);
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
						jMenuItem.setText("图层导出为文件");
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('w'));
//						jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
//								System.out.println(jMenuItem.getText());
								storeImages();
							}
						};
						jMenuItem.addActionListener(al);
						jMenuExport.add(jMenuItem);
					}
					{
						JMenuItem jMenuItem = new JMenuItem();
						jMenuItem.setText("图层导出为Layer");
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('q'));
//						jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
//								System.out.println(jMenuItem.getText());
								String description;
								if (null != layerListRoot && layerListRoot.size() != 0) {
									storeLayers();
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
						jMenuItem.setText("图层导出为PSD");
						jMenuItem.setAccelerator(KeyStroke.getKeyStroke('r'));
//						jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
						ActionListener al = new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
//								System.out.println(jMenuItem.getText());
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
//					jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, InputEvent.CTRL_DOWN_MASK));
					jMenuItem.setAccelerator(KeyStroke.getKeyStroke('0'));
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							String description = "打开工程将销毁现有数据！";
							int messageStyle = JOptionPane.showConfirmDialog(null, description);// yes=0;no=1;cancel=2
							if (messageStyle == 0) {
								if (null != layerListRoot && layerListRoot.size() != 0) {
									storeResources(openFiles(fileFilterStyle_Project), 0);
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
							createNewProject();
							drawResources();
							loadLayer(Arrays.asList(openFiles(fileFilterStyle_Project)));
							drawResources();
						}
					};
					jMenuItem.addActionListener(al);
					jMenu.add(jMenuItem);
				}
				{
					JMenuItem jMenuItem = new JMenuItem();
					jMenuItem.setText("保存工程");
//					jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK));
					jMenuItem.setAccelerator(KeyStroke.getKeyStroke('1'));
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
//							System.out.println(jMenuItem.getText());
							String description;
							if (null != layerListRoot && layerListRoot.size() != 0) {
								storeResources(openFiles(fileFilterStyle_Project), 0);
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
//					jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_DOWN_MASK));
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
//							System.out.println(jMenuItem.getText());
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
					jCheckBoxMenuItemCenter = new JCheckBoxMenuItem();
					jCheckBoxMenuItemCenter.setText("居中");
					jCheckBoxMenuItemCenter
							.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
//							System.out.println(jMenuItem.getText());
							if (null != layerListRoot && layerListRoot.size() != 0) {
								if (!isLocationCenter && jCheckBoxMenuItemCenter.isSelected()) {
									isLocationCenter = true;
								}
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
					jMenuItem.setText("关于");
//					jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
//							System.out.println(jMenuItem.getText());
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
//					jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
					ActionListener al = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
//							System.out.println(jMenuItem.getText());
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
//			jPanelMain.setBackground(Color.black);
			jPanelMain.setLayout(new BorderLayout());
			{
				{
					JPanel jPanel = new JPanel();
//					jPanel.setBackground(Color.red);
					jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
					{
//						Dimension dimensionJToolBar = new Dimension(256, 24);
						Dimension dimensionJTextField = new Dimension(64, 24);
//						Dimension dimensionJButton = new Dimension(24, 24);
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
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										if (null != imageLayerSelected) {
											imageLayerSelected.setSize(toDouble(jTextFieldSizeWidth),
													toDouble(jTextFieldSizeHeight));
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
										if (null != imageLayerSelected) {
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
										if (null != imageLayerSelected) {
											double rotate = toDouble(jTextFieldAngle);
											double rotateLayer = imageLayerSelected.getAngleRotated();
											layerTransform(imageLayerSelected, -1, -1, rotate);
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
										if (null != imageLayerSelected) {
											imageLayerSelected.setAnchor(toDouble(jTextFieldAnchorX),
													toDouble(jTextFieldAnchorY));
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
										if (null != imageLayerSelected) {
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
										if (null != imageLayerSelected) {
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
//					jPanel.setBackground(Color.cyan);
					jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
					{
						{
							JToolBar jToolBar = new JToolBar();
							jToolBar.setOrientation(JToolBar.VERTICAL);
							{
								JButton jButton = new JButton();
								jButton.setText("+");
								jButton.setToolTipText("画图");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
									}
								};
								jButton.addActionListener(al);
								jToolBar.add(jButton);
							}
							{
								JButton jButton = new JButton();
								jButton.setText("-");
								jButton.setToolTipText("橡皮");
								ActionListener al = new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
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
					JPanel jPanelCard = new JPanel();
					cardLayout = new CardLayout(0, 0);
					jPanelCard.setLayout(cardLayout);
					{
//					JScrollPane jScrollPane = new JScrollPane();
//					{
						jPanelGraphics = new JPanel();
						jPanelGraphics.setBackground(bgColor);
						jPanelGraphics.setForeground(fgColor);
						int width = 1024;
						int height = 1024;
						Dimension dimension = new Dimension(width, height);
						jPanelGraphics.setSize(dimension);
//					drawBackgroundStyle(jPanel);
						KeyListener kl = new KeyListener() {

							@Override
							public void keyPressed(KeyEvent e) {
								if (e.getKeyCode() == KeyEvent.VK_ALT) {
									isLayerRotated = true;
								} else {
									isLayerRotated = false;
								}
							}

							@Override
							public void keyReleased(KeyEvent e) {
								if (isLayerRotated) {
									isLayerRotated = false;
								}
							}

							@Override
							public void keyTyped(KeyEvent e) {
							}
						};
						jPanelGraphics.addKeyListener(kl);
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
								if (e.getButton() == MouseEvent.BUTTON1) {
									if (null != imageLayerSelected) {
									}
								} else if (e.getButton() == MouseEvent.BUTTON3) {
									if (null != imageLayerSelected) {
									}
								}
							}

							@Override
							public void mouseReleased(MouseEvent e) {
								xNew = e.getX();
								yNew = e.getY();
								if (null != imageLayerSelected) {
									Point location = imageLayerSelected.getLocation();
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
									imageLayerSelected.setLocation(x, y);
									setImageLayerInfo(imageLayerSelected);
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
						jPanelGraphics.addMouseListener(ml);
//						jScrollPane.getViewport().setView(jPanelGraphics);
//					}
//					jPanelMain.add(jScrollPane, BorderLayout.CENTER);
						jPanelCard.add(jPanelGraphics, cardGraphics);
					}
					{
						JPanel jPanelEditor = new JPanel();
						jPanelCard.add(jPanelEditor, cardEditor);
					}
					jPanelMain.add(jPanelCard, BorderLayout.CENTER);
				}
				{
					JPanel jPanel = new JPanel();
//					jPanel.setBackground(Color.darkGray);
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
//							int widthResources = 464;
									int heightResources = 360;
									Dimension dimensionResources = new Dimension(widthResources, heightResources);
									jScrollPane.setPreferredSize(dimensionResources);
									{
										DefaultMutableTreeNode treeNodeRoot = new DefaultMutableTreeNode();
										treeNodeRoot.setUserObject("图层");
										jTreeRoot = new JTree();
										jTreeRoot.setShowsRootHandles(true);
										jTreeRoot.setDragEnabled(true);
										jTreeRoot.setEditable(false);
//										jTreeUtils.setRootVisible(false);
										DefaultTreeModel treeModelRoot = new DefaultTreeModel(treeNodeRoot);
										jTreeRoot.setModel(treeModelRoot);
										treeCellRenderer = new DefaultTreeCellRendererUtils();
										jTreeRoot.setCellRenderer(treeCellRenderer);
										KeyListener kl = new KeyListener() {

											@Override
											public void keyPressed(KeyEvent e) {
												if (e.getKeyCode() == KeyEvent.VK_ALT) {
													isChildAdd = true;
												} else {
													isChildAdd = false;
												}
											}

											@Override
											public void keyReleased(KeyEvent e) {
												if (isChildAdd) {
													isChildAdd = false;
												}
											}

											@Override
											public void keyTyped(KeyEvent e) {
											}
										};
										jTreeRoot.addKeyListener(kl);
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
												myTreeNodeMoveTo(selRow, isChildAdd);
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
												DefaultTreeModel dtm = (DefaultTreeModel) jTreeRoot.getModel();
												dtm.removeNodeFromParent(treeNodeSelected);
												Object object;
												String pathName;
//												File file;
												if (treeNodeSelected.getChildCount() != 0) {
													DefaultMutableTreeNode treeNode;
													for (@SuppressWarnings("unchecked")
													Enumeration<TreeNode> treeNodeEnumeration = treeNodeSelected
															.children(); treeNodeEnumeration.hasMoreElements();) {
														treeNode = (DefaultMutableTreeNode) treeNodeEnumeration
																.nextElement();
														object = treeNode.getUserObject();
														if (null != object) {
															pathName = (String) object;
															layerListRoot.remove(
																	fileUtils.getImageLayer(layerListRoot, pathName));
														}
													}
												}
												object = treeNodeSelected.getUserObject();
												if (null != object && object instanceof String) {
													pathName = (String) object;
													layerListRoot
															.remove(fileUtils.getImageLayer(layerListRoot, pathName));
												}
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
													if (null != object && object instanceof String) {
														String pathName = (String) object;
														ImageLayer imageLayer = fileUtils.getImageLayer(layerListRoot,
																pathName);
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
//					jPanel.setBackground(Color.blue);
					jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
					{
//						Dimension dimensionJToolBar = new Dimension(256, 24);
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
//								jTextFieldCanvasX.setEnabled(false);
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
//								jTextFieldCanvasY.setEnabled(false);
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
												canvas = new Dimension();
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
//								jLabelView.setEnabled(false);
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
										if (null != imageLayerSelected) {
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
										if (null != imageLayerSelected) {
											double x = toDouble(jTextFieldLocationX);
											double y = toDouble(jTextFieldLocationY);
											layerTransform(imageLayerSelected, x, y, -1);
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
										if (null != imageLayerSelected) {
											Dimension size = imageLayerSelected.getSize();
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

	private boolean isTreeNodeChild(DefaultMutableTreeNode parent, DefaultMutableTreeNode child) {
		if (null != parent && null != child) {
			DefaultMutableTreeNode treeNodeChild;
			Object object;
			Object objectChild;
			for (@SuppressWarnings("unchecked")
			Enumeration<TreeNode> enumeration = parent.children(); enumeration.hasMoreElements();) {
				treeNodeChild = (DefaultMutableTreeNode) enumeration.nextElement();
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

	private void layerExport(File file, List<ImageLayer> layerList, boolean isExportProject) {
		if (null != layerList) {
			ImageLayer layer;
			Properties properties = new Properties();
			File imageFile;
			File propFile;
			File dir;
			String imagePath;
			Properties prop;
			PropertiesUtils pu = new PropertiesUtils();
			String commentImage = "Images";
			String encoding = "utf-8";
			for (Iterator<ImageLayer> iterator = layerList.iterator(); iterator.hasNext();) {
				layer = iterator.next();
				if (null != layer) {
					imagePath = layer.getImagePath();
					prop = pu.getPropertiesString(layer.getProperties());
					if (null != imagePath && null != prop) {
						imageFile = new File(imagePath);
						dir = imageFile.getParentFile();
						if (null != dir) {
							if (!imageFile.exists()) {
								imageExport(layer.getImage(), imageFile);
							}
							propFile = getFileLayer(imageFile);
							fileUtils.storeProperties(propFile, prop, commentImage, encoding);
							if (isExportProject) {
								properties.put(imageFile.getName(), propFile.getAbsolutePath());
							}
						}
					}
				}
			}
			if (null != file && isExportProject) {
				String commentLayer = "PixelShader ImageLayer";
				fileUtils.storeProperties(getFileProject(file), properties, commentLayer, encoding);
			}
		}
	}

	private void layerTransform(ImageLayer imageLayer, double x, double y, double rotate) {
		if (null != imageLayer) {
			Point location = imageLayer.getLocation();
			double xRate = 0;
			double yRate = 0;
			if (null != location) {
				double xLayer = location.getX();
				double yLayer = location.getY();
				xRate = x - xLayer;
				yRate = y - yLayer;
			}
			if (xRate != 0 || yRate != 0) {
				imageLayer.setLocation(x, y);
			}
			double rotateLayer = imageLayer.getAngleRotated();
			double rateRotate = rotate - rotateLayer;
			if (rotate != rotateLayer) {
				imageLayer.setAngleRotated(rotate);
			}
			String imagePath = imageLayer.getImagePath();
			if (null != imagePath) {
				DefaultMutableTreeNode treeNode = getTreeNode(jTreeRoot, imagePath);
				if (null != treeNode) {
					DefaultMutableTreeNode treeNodeChild;
					Object object;
					String fileLayerName;
					ImageLayer layer;
					Point locationChild;
					double xc;
					double yc;
					System.out.println(1);
					for (@SuppressWarnings("unchecked")
					Enumeration<TreeNode> treeNodeEnumeration = treeNode.children(); treeNodeEnumeration
							.hasMoreElements();) {
						treeNodeChild = (DefaultMutableTreeNode) treeNodeEnumeration.nextElement();
						System.out.println(2);
						if (null != treeNodeChild) {
							System.out.println(3);
							object = treeNode.getUserObject();
							System.out.println(object);
							if (null != object && object instanceof String) {
								System.out.println(4);
								fileLayerName = (String) object;
								layer = fileUtils.getImageLayer(layerListRoot, fileLayerName);
								if (null != layer) {
									System.out.println(5);
									locationChild = layer.getLocation();
									if (xRate != 0 || yRate != 0) {
										System.out.println(6);
										if (null != locationChild) {
											xc = locationChild.getX() + xRate;
											yc = locationChild.getY() + yRate;
										} else {
											xc = xRate;
											yc = yRate;
										}
										layer.setLocation(xc, yc);
									}
									if (rateRotate != 0) {

									}
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
			updataGraphics2D();
		}
	}

	private void loadPsdLayer(File[] array) {
		if (null != array) {
			PSDFileReader psdFileReader = new PSDFileReader();
			List<ImageLayer> layerList = new LinkedList<>();
			for (int i = 0, iLength = array.length; i < iLength; i++) {
				layerList = psdFileReader.read(array[i], layerList);
				collectionsUtils.removeRepetition(layerListRoot, layerList);
				layerListRoot = layerList;
			}
			updataGraphics2D();
		}
	}

	private File[] openFiles(int filterStyle) {
		if (null == jFileChooser) {
			jFileChooser = new JFileChooser();
		}
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		FileNameExtensionFilter filter = null;
		jFileChooser.resetChoosableFileFilters();
		if (filterStyle == 8) {
			filter = new FileNameExtensionFilter("Images", "bmp", "jpg", "gif", "png");
		} else if (filterStyle == 5) {
			filter = new FileNameExtensionFilter("PixelShader ImageLayer", "pxl");
		} else if (filterStyle == 2) {
			filter = new FileNameExtensionFilter("Photoshop Document", "psd");
		} else if (filterStyle == 0) {
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
		if (null != imageLayer) {
			Point location = imageLayer.getLocation();
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
			Dimension size = imageLayer.getSize();
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
			Dimension scale = imageLayer.getScale();
			if (null != scale) {
				s = Math.max(scale.getWidth(), scale.getHeight());
			}
			scaleMax = String.valueOf(s);
			angle = String.valueOf(imageLayer.getAngleRotated());
			Point anchor = imageLayer.getAnchor();
			if (null != anchor) {
				anchorX = String.valueOf(anchor.getX());
				anchorY = String.valueOf(anchor.getY());
			}
			if (imageLayer.isVisible()) {
				jCheckBoxVisible.setSelected(true);
			} else {
				jCheckBoxVisible.setSelected(false);
			}
			alphaFloat = String.valueOf(imageLayer.getAlpha());
			depthLayer = String.valueOf(imageLayer.getDepth());
		} else {
			locationX = "0 px";
			locationY = "0 px";
			width = "0 px";
			height = "0 px";
			scaleMax = "1倍";
			angle = "0度";
			anchorX = "0 px";
			anchorY = "0 px";
			alphaFloat = "1.0f";
			depthLayer = "0 层";
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
	}

	private void setToolkitEnabled(boolean isEnabled) {
		if (isEnabled) {
			if (!jCheckBoxVisible.isEnabled()) {
				jCheckBoxVisible.setEnabled(true);
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
			if (jTextFieldScale.isEnabled()) {
				jTextFieldScale.setEnabled(false);
			}
			if (jTextFieldSizeWidth.isEnabled()) {
				jTextFieldSizeWidth.setEnabled(false);
			}
		}
	}

	private void storeImages() {
		if (!isExport) {
			isExport = true;
		}
		drawResources();
		imagesExport(layerListRoot, null);
	}

	private void storeLayers() {
		if (null != layerListRoot) {
			layerExport(null, layerListRoot, false);
		}
	}

	private void storeResources(File[] fileArray, int style) {
		if (null == fileArray) {
			return;
		}
		File file = fileArray[0];
		File dir;
		if (!file.isDirectory()) {
			dir = file.getParentFile();
		} else {
			dir = file;
		}
		if (null == dir) {
			return;
		}
		File tempFile;
		String fileName;
		if (style == 8) {
			Calendar calendar = new GregorianCalendar();
			StringBuffer sbuf = new StringBuffer();
			sbuf.append("layerCombined-");
//			sbuf.append(calendar.get(Calendar.YEAR));
//			sbuf.append(calendar.get(Calendar.MONTH));
			sbuf.append(calendar.get(Calendar.DAY_OF_MONTH));
			sbuf.append(calendar.get(Calendar.HOUR_OF_DAY));
			sbuf.append(calendar.get(Calendar.MINUTE));
			sbuf.append(calendar.get(Calendar.SECOND));
			sbuf.append(calendar.get(Calendar.MILLISECOND));
			sbuf.append(fileSuffixes_Image);
			fileName = sbuf.toString();
			tempFile = new File(dir, fileName);
			if (null != layerImage) {
				imageExport(layerImage, tempFile);
			}
		}
//		else if (style == 2) {
////			psd
//		}
		else if (style == 0) {
			if (null != layerListRoot) {
				layerExport(dir, layerListRoot, true);
			}
		}
		if (isExport) {
			isExport = false;
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
		if (treeNodeParent.getAllowsChildren()) {
			DefaultTreeModel modelRoot = (DefaultTreeModel) jTreeRoot.getModel();
			modelRoot.removeNodeFromParent(treeNodeDragged);
			modelRoot.insertNodeInto(treeNodeDragged, treeNodeParent, index);
//			dtm.insertNodeInto(treeNodeDragged, parent, 0);
			modelRoot.nodeChanged(treeNodeParent);
		}
	}

	private void updataGraphics2D() {
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
				layerList.set(indexOld, layerOld);
			}
			if (null != layerNew) {
				layerNew.setDepth(depthOld);
				layerList.set(indexNew, layerNew);
			}
		}
	}

	private void updateJTree() {
		if (null != jTreeRoot && null != layerListRoot) {
			if (null == treeCellRenderer) {
				treeCellRenderer = new DefaultTreeCellRendererUtils();
			}
			treeCellRenderer.setResourcesList(layerListRoot);
			DefaultTreeModel modelRoot = (DefaultTreeModel) jTreeRoot.getModel();
			if (null != modelRoot) {
				Object root = modelRoot.getRoot();
				if (null != root && root instanceof DefaultMutableTreeNode) {
					updateTreeNode((DefaultMutableTreeNode) root, layerListRoot);
				}
			}
		}
	}

	private List<ImageLayer> updateListLayer(List<ImageLayer> layerList, List<File> fileList) {
		if (null != fileList) {
			if (null == layerList) {
				layerList = new LinkedList<>();
			}
			File file;
			ImageLayer imageLayer;
			ImageLayer layer;
			String fileName;
			String layerPath;
			String imagePath = "imagePath";
			Properties propLayer;
			Properties prop = null;
			Object object;
			for (Iterator<File> iterator = fileList.iterator(); iterator.hasNext();) {
				file = iterator.next();
				if (null != file) {
					fileName = file.getName().toLowerCase();
					if (fileName.endsWith(fileSuffixes_Project)) {
						fileUtils.setSuffixProperties(fileSuffixes_Project);
						propLayer = fileUtils.loadProperties(file);
						if (null != propLayer) {
							for (Iterator<Object> iteratorProperties = propLayer.values().iterator(); iteratorProperties
									.hasNext();) {
								object = iteratorProperties.next();
								if (null != object && object instanceof String) {
									layerPath = (String) object;
									file = new File(layerPath);
									fileUtils.setSuffixProperties(fileSuffixes_Layer);
									prop = fileUtils.loadProperties(file);
									if (null != prop) {
										file = fileUtils.getFile(fileList, prop.getProperty(imagePath));
										if (null == file) {
											imageLayer = new ImageLayer();
											imageLayer.setProperties(prop);
											layerList.add(imageLayer);
										}
									}
								}
							}
						}
					} else if (fileName.endsWith(fileSuffixes_Layer)) {
						fileUtils.setSuffixProperties(fileSuffixes_Layer);
						prop = fileUtils.loadProperties(file);
						if (null != prop) {
							file = fileUtils.getFile(fileList, prop.getProperty(imagePath));
							if (null == file) {
								imageLayer = new ImageLayer();
								imageLayer.setProperties(prop);
								layerList.add(imageLayer);
							}
						}
					} else {
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
		return layerList;
	}

	private void updateResources(JTree jTree) {
		if (null != jTree) {
			DefaultTreeModel treeModel = (DefaultTreeModel) jTree.getModel();
			DefaultMutableTreeNode treeNodeRoot = (DefaultMutableTreeNode) treeModel.getRoot();
			DefaultMutableTreeNode treeNode;
			ImageLayer layer;
			Object object;
			String pathName;
			int depth;
			int depthMax = fileUtils.getImageLayerDepthMax(layerListRoot) + 1;
			int index;
			for (@SuppressWarnings("unchecked")
			Enumeration<TreeNode> treeNodeEnumeration = treeNodeRoot.preorderEnumeration(); treeNodeEnumeration
					.hasMoreElements();) {
				treeNode = (DefaultMutableTreeNode) treeNodeEnumeration.nextElement();
				if (null != treeNode) {
					object = treeNode.getUserObject();
					if (null != object && object instanceof String) {
						pathName = (String) object;
						layer = fileUtils.getImageLayer(layerListRoot, pathName);
						index = layerListRoot.indexOf(layer);
						if (null != layer) {
							depth = layer.getDepth();
							if (depth == -1) {
								layer.setDepth(depthMax++);
								layerListRoot.set(index, layer);
							}
						}
					}
				}
			}
		}
	}

	private void updateTreeNode(DefaultMutableTreeNode parent, List<ImageLayer> layerList) {
		if (null == layerList || null == parent || !(parent instanceof DefaultMutableTreeNode)
				|| !parent.getAllowsChildren()) {
			return;
		}
		DefaultTreeModel modelRoot = (DefaultTreeModel) jTreeRoot.getModel();
		int index;
		String pathName;
		String imagePath;
		File file;
		DefaultMutableTreeNode treeNode;
		ImageLayer imageLayer;
		for (Iterator<ImageLayer> iterator = layerList.iterator(); iterator.hasNext();) {
			imageLayer = iterator.next();
			if (null != imageLayer) {
				treeNode = new DefaultMutableTreeNode();
				imagePath = imageLayer.getImagePath();
				if (null != imagePath) {
					file = new File(imagePath);
					pathName = file.getName();
					treeNode.setUserObject(pathName);
					if (!isTreeNodeChild(parent, treeNode)) {
						index = parent.getChildCount();
						modelRoot.insertNodeInto(treeNode, parent, index);
						modelRoot.nodeChanged(parent);
					}
				}
			}
		}
	}
}
