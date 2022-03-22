package cn.imaginary.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.File;

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

import javaev.swing.JTreeDnD;

public class TestUI extends JFrame {

	/**
	 * @author Sureness
	 * @since 0.1
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		TestUI tui = new TestUI();
		tui.setVisible(true);
	}

	private FileUtils fileUtils = FileUtils.getInstance();

	private JFileChooser jFileChooser;

	private JTreeDnD jTreeDnD;

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
				jTreeDnD.addNewUserObjects(treeNodeSelected, fileUtils.toList(file.listFiles()));
			}
		}
	}

	private void initGUI() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(480, 360);
		setLocationRelativeTo(null);
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
			JScrollPane jScrollPane = new JScrollPane();
			{
				DefaultMutableTreeNode dmTreeNodeRoot = new DefaultMutableTreeNode();
				dmTreeNodeRoot.setUserObject("图层");
				jTreeDnD = new JTreeDnD();
				jTreeDnD.setShowsRootHandles(true);
				jTreeDnD.setDragEnabled(true);
				jTreeDnD.setEditable(true);
//					jTreeDnD.setRootVisible(false);
				DefaultTreeModel treeModelRoot = new DefaultTreeModel(dmTreeNodeRoot);
				jTreeDnD.setModel(treeModelRoot);
				MouseListener ml = new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						int selRow = jTreeDnD.getRowForLocation(e.getX(), e.getY());
						TreePath selPath = jTreeDnD.getPathForLocation(e.getX(), e.getY());
						if (selRow != -1) {
							if (e.getClickCount() == 1) {
								mySingleClick(selRow, selPath);
							} else if (e.getClickCount() == 2) {
								myDoubleClick(selRow, selPath);
							}
						}
					}
				};
				jTreeDnD.addMouseListener(ml);
				jScrollPane.getViewport().setView(jTreeDnD);
			}
			getContentPane().add(jScrollPane);
		}
	}

	private void myDoubleClick(int selRow, TreePath selPath) {
		System.out.println(selRow);
		System.out.println(selPath);
		expandDirectory(selRow, selPath);
	}

	private void mySingleClick(int selRow, TreePath selPath) {
		System.out.println(selRow);
		System.out.println(selPath);
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

	private void loadResources(File[] fileArray) {
		jTreeDnD.addNewUserObjects((DefaultMutableTreeNode) ((DefaultTreeModel) jTreeDnD.getModel()).getRoot(),
				fileUtils.toList(fileArray));
	}

}
