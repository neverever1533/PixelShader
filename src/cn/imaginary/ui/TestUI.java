package cn.imaginary.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import java.io.File;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import javaev.awt.Graphics2DUtils;

import javaev.imageio.ImageIOUtils;

import javaev.io.FileUtils;

import javaev.swing.JPanelDnD;
import javaev.swing.JTreeUtils;

public class TestUI extends JFrame {

	/**
	 * @author Sureness
	 * @since 0.1
	 */
	private static final long serialVersionUID = 4247417000065494012L;

	public static void main(String[] args) {
		TestUI tui = new TestUI();
		tui.setVisible(true);
	}

	private FileUtils fileUtils = FileUtils.getInstance();

	private Graphics2DUtils g2dUtils = new Graphics2DUtils();

	private ImageIOUtils imageIOUtils = new ImageIOUtils();

	private JFileChooser jFileChooser;

	private JTreeUtils jTreeUtils;

	public TestUI() {
		super();
		setTitle("JTreeTest by Sev末夜");
		initGUI();
	}

	private void expandDirectory(int selRow) {
		expandTreeNodeDirectory((DefaultMutableTreeNode) jTreeUtils.getPathForRow(selRow).getLastPathComponent());
	}

	private void expandTreeNodeDirectory(DefaultMutableTreeNode treeNodeSelected) {
		if (null == treeNodeSelected) {
			return;
		}
		Object object = treeNodeSelected.getUserObject();
		if (object instanceof File) {
			File file = (File) object;
			if (file.isDirectory()) {
				jTreeUtils.addNewUserObjects(treeNodeSelected, fileUtils.toList(file.listFiles()));
			}
		}
	}

	public BufferedImage getBufferedImage(BufferedImage[] imageArray) {
		if (null == imageArray) {
			return null;
		}
		Rectangle rectangle = g2dUtils.getRectangleMax(imageArray);
		BufferedImage image = g2dUtils.drawImage(imageArray, 0, 0, rectangle.width, rectangle.height);
		return image;
	}

	public BufferedImage getBufferedImage(File[] fileArray) {
		BufferedImage[] imageArray = imageIOUtils.read(fileArray);
		return getBufferedImage(imageArray);
	}

	public void imageExport(File file, BufferedImage image) {
		imageIOUtils.write(file, image);
	}

	private void initGUI() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(480, 360);
		setLocationRelativeTo(null);
//		getContentPane().setBackground(Color.RED);
//			getContentPane().setLayout(null);
		{
			JMenuBar jMenuBar = new JMenuBar();
			{
				JMenu jMenu = new JMenu();
				jMenu.setText("文件");
				{
					JMenuItem jMenuItemOpen = new JMenuItem();
					jMenuItemOpen.setText("打开");
					jMenuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
					jMenuItemOpen.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							loadResources(openFiles());
						}
					});
					jMenu.add(jMenuItemOpen);
				}
				{
					JMenuItem jMenuItemExport = new JMenuItem();
					jMenuItemExport.setText("合并导出");
					jMenuItemExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
					jMenuItemExport.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							storeResources(openFiles());
						}
					});
					jMenu.add(jMenuItemExport);
				}
				jMenuBar.add(jMenu);
			}
			getContentPane().add(jMenuBar, BorderLayout.NORTH);
		}
		{
			JPanelDnD jPanelDnD = new JPanelDnD();
			jPanelDnD.setLayout(new BorderLayout());
			{
				JScrollPane jScrollPane = new JScrollPane();
//				int widthResources = 128;
				int widthResources = 464;
				int heightResources = 360;
				Dimension dimensionResources = new Dimension(widthResources, heightResources);
				jScrollPane.setPreferredSize(dimensionResources);
				{
					DefaultMutableTreeNode dmTreeNodeRoot = new DefaultMutableTreeNode();
					dmTreeNodeRoot.setUserObject("图层");
					jTreeUtils = new JTreeUtils();
					jTreeUtils.setShowsRootHandles(true);
					jTreeUtils.setDragEnabled(true);
					jTreeUtils.setEditable(true);
//					jTreeUtils.setRootVisible(false);
					DefaultTreeModel treeModelRoot = new DefaultTreeModel(dmTreeNodeRoot);
					jTreeUtils.setModel(treeModelRoot);
					MouseMotionListener mml = new MouseMotionListener() {

						private int rowDragged;
						private int rowMoved;

						@Override
						public void mouseDragged(MouseEvent e) {
							int selRow = jTreeUtils.getRowForLocation(e.getX(), e.getY());
							rowDragged = selRow;
						}

						@Override
						public void mouseMoved(MouseEvent e) {
							int selRow = jTreeUtils.getRowForLocation(e.getX(), e.getY());
							myTreeNodeMoveTo(selRow);
						}

						private void myTreeNodeMoveTo(int selRow) {
							rowMoved = selRow;
							if (rowMoved == -1 || rowDragged == -1 || rowDragged == 0 || rowMoved == rowDragged) {
								return;
							}
							DefaultMutableTreeNode treeNodeDragged = (DefaultMutableTreeNode) jTreeUtils
									.getPathForRow(rowDragged).getLastPathComponent();
							DefaultMutableTreeNode treeNodeMoved = (DefaultMutableTreeNode) jTreeUtils
									.getPathForRow(rowMoved).getLastPathComponent();
							jTreeUtils.treeNodeMoveTo(treeNodeDragged, treeNodeMoved);
							rowDragged = -1;
						}
					};
					jTreeUtils.addMouseMotionListener(mml);
					MouseListener ml = new MouseListener() {

						@Override
						public void mouseClicked(MouseEvent e) {
							int selRow = jTreeUtils.getRowForLocation(e.getX(), e.getY());
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
							loadResources(jPanelDnD.getDropResources());
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

						private void myRightClick(int selRow) {
							if (selRow == -1) {
								return;
							}
							DefaultMutableTreeNode treeNodeSelected = (DefaultMutableTreeNode) jTreeUtils
									.getPathForRow(selRow).getLastPathComponent();
							if (null == treeNodeSelected) {
								return;
							}
							DefaultMutableTreeNode parent = (DefaultMutableTreeNode) treeNodeSelected.getParent();
							if (null == parent) {
								return;
							}
							DefaultTreeModel dtm = (DefaultTreeModel) jTreeUtils.getModel();
							dtm.removeNodeFromParent(treeNodeSelected);
						}

						private void mySingleClick(int selRow) {
							if (selRow == -1) {
								return;
							}
						}

					};
					jTreeUtils.addMouseListener(ml);
					jScrollPane.getViewport().setView(jTreeUtils);
				}
				jPanelDnD.add(jScrollPane);
			}
			getContentPane().add(jPanelDnD, BorderLayout.WEST);
		}
		{
			JPanel jPanelGraphics = new JPanel();
			jPanelGraphics.setBackground(Color.black);
			getContentPane().add(jPanelGraphics, BorderLayout.CENTER);
		}
	}

	private void loadResources(File[] fileArray) {
		jTreeUtils.addNewUserObjects((DefaultMutableTreeNode) ((DefaultTreeModel) jTreeUtils.getModel()).getRoot(),
				fileUtils.toList(fileArray));
	}

	private void loadResources(List<File> fileList) {
		if (null != fileList) {
			jTreeUtils.addNewUserObjects((DefaultMutableTreeNode) jTreeUtils.getModel().getRoot(), fileList);
		}
	}

	private void myDoubleClick(int selRow) {
		expandDirectory(selRow);
	}

	private File[] openFiles() {
		if (null == jFileChooser) {
			jFileChooser = new JFileChooser();
		}
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//		FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "bmp", "jpg", "gif", "png");
//		jFileChooser.setFileFilter(filter);
		jFileChooser.setMultiSelectionEnabled(true);
		int returnVal = jFileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.CANCEL_OPTION) {
			return null;
		}
		File[] fileArray = jFileChooser.getSelectedFiles();
		return fileArray;
	}

	private void storeResources(File[] fileArray) {
		File file = fileArray[0];
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
		DefaultTreeModel treeModel = (DefaultTreeModel) jTreeUtils.getModel();
		DefaultMutableTreeNode treeNodeRoot = (DefaultMutableTreeNode) treeModel.getRoot();
		DefaultMutableTreeNode treeNode;
		Object object;
		File fileNode;
		ArrayList<File> list = new ArrayList<>();
		Enumeration<TreeNode> treeNodeEnumeration = treeNodeRoot.preorderEnumeration();
		for (Iterator<TreeNode> iterator = treeNodeEnumeration.asIterator(); iterator.hasNext();) {
			treeNode = (DefaultMutableTreeNode) iterator.next();
			object = treeNode.getUserObject();
			if (null != object && object instanceof File) {
				fileNode = (File) object;
				if (fileNode.isFile()) {
					list.add(fileNode);
				}
			}
		}
		int len = list.size();
		File[] fileArr = new File[len];
		list.toArray(fileArr);
		Calendar calendar = new GregorianCalendar();
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("layerCombined-");
		sbuf.append(calendar.get(Calendar.DAY_OF_MONTH));
		sbuf.append(calendar.get(Calendar.HOUR_OF_DAY));
		sbuf.append(calendar.get(Calendar.MINUTE));
		sbuf.append(calendar.get(Calendar.SECOND));
		sbuf.append(calendar.get(Calendar.MILLISECOND));
		sbuf.append(".png");
		String fileName = sbuf.toString();
		File temp = new File(dir, fileName);
		imageIOUtils.write(temp, getBufferedImage(fileArr));
	}

}
