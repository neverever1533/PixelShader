package javaev.swing;

import java.io.File;

import java.util.Iterator;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import cn.imaginary.toolkit.image.ImageLayer;

public class JTreeUtils extends JTree {
	/**
	 * @author Sureness
	 * @since 0.1
	 */
	private static final long serialVersionUID = 4573533967201701426L;

	public void addNewUserObjects(DefaultMutableTreeNode parent, List<File> list) {
		if (null == list || null == parent || !(parent instanceof DefaultMutableTreeNode)
				|| !parent.getAllowsChildren()) {
			return;
		}
		DefaultTreeModel dtm = (DefaultTreeModel) this.getModel();
		int index;
		String pathName;
		for (Iterator<File> iterator = list.iterator(); iterator.hasNext();) {
			File file = iterator.next();
			if (null != file) {
				DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode();
				if (file.isFile()) {
					pathName = file.getName();
					dmtn.setUserObject(pathName);
//					dtm.insertNodeInto(dmtn, parent, 0);
					if (dtm.getIndexOfChild(parent, dmtn) == -1) {
						index = parent.getChildCount();
						dtm.insertNodeInto(dmtn, parent, index);
						dtm.nodeChanged(parent);
					}
				}
			}
		}
	}

	public void addNewUserObjectsFile(DefaultMutableTreeNode parent, List<File> list) {
		if (null == list || null == parent || !(parent instanceof DefaultMutableTreeNode)
				|| !parent.getAllowsChildren()) {
			return;
		}
		DefaultTreeModel dtm = (DefaultTreeModel) this.getModel();
		int index;
		for (Iterator<File> iterator = list.iterator(); iterator.hasNext();) {
			File file = iterator.next();
			if (null != file) {
				DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode();
				if (file.isDirectory()) {
					dmtn.setAllowsChildren(true);
				} else {
					dmtn.setAllowsChildren(false);
				}
				dmtn.setUserObject(file);
//			dtm.insertNodeInto(dmtn, parent, 0);
				if (dtm.getIndexOfChild(parent, dmtn) == -1) {
					index = parent.getChildCount();
					dtm.insertNodeInto(dmtn, parent, index);
					dtm.nodeChanged(parent);
				}
			}
		}
	}

	public void addNewUserObjectsImageLayer(DefaultMutableTreeNode parent, List<ImageLayer> list) {
		if (null == list || null == parent || !(parent instanceof DefaultMutableTreeNode)
				|| !parent.getAllowsChildren()) {
			return;
		}
		DefaultTreeModel dtm = (DefaultTreeModel) this.getModel();
		int index;
		for (Iterator<ImageLayer> iterator = list.iterator(); iterator.hasNext();) {
			ImageLayer imageLayer = iterator.next();
			if (null != imageLayer) {
				DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode();
				dmtn.setUserObject(imageLayer);
				if (dtm.getIndexOfChild(parent, dmtn) == -1) {
					index = parent.getChildCount();
					dtm.insertNodeInto(dmtn, parent, index);
					dtm.nodeChanged(parent);
				}
			}
		}
	}

	public void addNewUserObjectsPathName(DefaultMutableTreeNode parent, List<ImageLayer> list) {
		if (null == list || null == parent || !parent.getAllowsChildren()) {
			return;
		}
		DefaultTreeModel dtm = (DefaultTreeModel) this.getModel();
		int index;
		ImageLayer imageLayer;
		String pathName;
		for (Iterator<ImageLayer> iterator = list.iterator(); iterator.hasNext();) {
			imageLayer = iterator.next();
			if (null != imageLayer) {
				pathName = imageLayer.getImagePath();
				if ((null != pathName)) {
					DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode();
					dmtn.setUserObject(pathName);
//					dtm.insertNodeInto(dmtn, parent, 0);
					if (dtm.getIndexOfChild(parent, dmtn) == -1) {
						index = parent.getChildCount();
						dtm.insertNodeInto(dmtn, parent, index);
						dtm.nodeChanged(parent);
					}
				}
			}
		}
	}

//	public void treeNodeMoveTo(DefaultMutableTreeNode treeNodeDragged, DefaultMutableTreeNode treeNodeMoved,
//			boolean isChildAdd) {
	public void treeNodeMoveTo(DefaultMutableTreeNode treeNodeDragged, DefaultMutableTreeNode treeNodeMoved,
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
			DefaultTreeModel dtm = (DefaultTreeModel) this.getModel();
			dtm.removeNodeFromParent(treeNodeDragged);
			dtm.insertNodeInto(treeNodeDragged, treeNodeParent, index);
//			dtm.insertNodeInto(treeNodeDragged, parent, 0);
			dtm.nodeChanged(treeNodeParent);
		}
	}

}
