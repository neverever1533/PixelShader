package cn.imaginary.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.io.File;

import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

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

	private JFileChooser jFileChooser;

	private JTreeUtils jTreeUtils;

	public TestUI() {
		super();
		setTitle("JTreeTest by Sev末夜");
		initGUI();
	}

	private void expandDirectory(int selRow, TreePath selPath) {
		DefaultMutableTreeNode treeNodeSelected = (DefaultMutableTreeNode) selPath.getLastPathComponent();
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
					JMenuItem jMenuItem = new JMenuItem();
					jMenuItem.setText("打开");
					jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
					jMenuItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							loadResources(openFiles());
						}
					});
					jMenu.add(jMenuItem);
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
//					jTreeDnD.setRootVisible(false);
					DefaultTreeModel treeModelRoot = new DefaultTreeModel(dmTreeNodeRoot);
					jTreeUtils.setModel(treeModelRoot);
					MouseMotionListener mml = new MouseMotionListener() {

						private TreePath draggedPath;
						private TreePath movedPath;

						@Override
						public void mouseDragged(MouseEvent e) {
//							int selRow = jTreeUtils.getRowForLocation(e.getX(), e.getY());
							TreePath selPath = jTreeUtils.getPathForLocation(e.getX(), e.getY());
							if (null != selPath) {
								draggedPath = selPath;
							}
						}

						@Override
						public void mouseMoved(MouseEvent e) {
							int selRow = jTreeUtils.getRowForLocation(e.getX(), e.getY());
							TreePath selPath = jTreeUtils.getPathForLocation(e.getX(), e.getY());
							if (null != selPath) {
								movedPath = selPath;
								myNodeMoveTo(selRow, selPath);
							}
						}

						private void myNodeMoveTo(int selRow, TreePath selPath) {
							if (null != selPath) {
								movedPath = selPath;
								if (null != draggedPath) {
									if (!movedPath.isDescendant(draggedPath) && draggedPath != movedPath) {
										DefaultMutableTreeNode treeNodeSelected = (DefaultMutableTreeNode) draggedPath
												.getLastPathComponent();
										DefaultMutableTreeNode treeNodeMoved = (DefaultMutableTreeNode) movedPath
												.getLastPathComponent();
										if (null != treeNodeSelected && null != treeNodeMoved) {
											DefaultMutableTreeNode parent;
											if (treeNodeMoved.getAllowsChildren()) {
												parent = treeNodeMoved;
											} else {
												parent = (DefaultMutableTreeNode) treeNodeMoved.getParent();
											}
											if (null != parent) {
												DefaultTreeModel dtm = (DefaultTreeModel) jTreeUtils.getModel();
												dtm.removeNodeFromParent(treeNodeSelected);
												dtm.insertNodeInto(treeNodeSelected, parent, 0);
												movedPath = null;
											}
										}
									}
								}
							}
						}
					};
					jTreeUtils.addMouseMotionListener(mml);
					MouseListener ml = new MouseListener() {

						@Override
						public void mouseClicked(MouseEvent e) {
							int selRow = jTreeUtils.getRowForLocation(e.getX(), e.getY());
							TreePath selPath = jTreeUtils.getPathForLocation(e.getX(), e.getY());
							if (null != selPath) {
								if (selRow != -1) {
									if (e.getButton() == MouseEvent.BUTTON3) {
										myRightClick(selRow, selPath);
									} else if (e.getClickCount() == 2) {
										myDoubleClick(selRow, selPath);
									}
								}
							}
						}

						@Override
						public void mouseEntered(MouseEvent e) {
							List<File> fileList = jPanelDnD.getDropResources();
							if (null != fileList) {
								jTreeUtils.addNewUserObjects((DefaultMutableTreeNode) jTreeUtils.getModel().getRoot(),
										fileList);
								jPanelDnD.setDropResources(null);
							}
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

						private void myRightClick(int selRow, TreePath selPath) {
							DefaultMutableTreeNode treeNodeSelected = (DefaultMutableTreeNode) selPath
									.getLastPathComponent();
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

					};
					jTreeUtils.addMouseListener(ml);
					jScrollPane.getViewport().setView(jTreeUtils);
				}
				jPanelDnD.add(jScrollPane);
			}
			getContentPane().add(jPanelDnD, BorderLayout.WEST);
		}
	}

	private void loadResources(File[] fileArray) {
		jTreeUtils.addNewUserObjects((DefaultMutableTreeNode) ((DefaultTreeModel) jTreeUtils.getModel()).getRoot(),
				fileUtils.toList(fileArray));
	}

	private void myDoubleClick(int selRow, TreePath selPath) {
		expandDirectory(selRow, selPath);
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

}
