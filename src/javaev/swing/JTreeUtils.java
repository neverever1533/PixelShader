package javaev.swing;

import java.io.File;

import java.util.Iterator;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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
		for (Iterator<File> iterator = list.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode();
			if (file.isDirectory()) {
				dmtn.setAllowsChildren(true);
			} else {
				dmtn.setAllowsChildren(false);
			}
			dmtn.setUserObject(file);
//			dtm.insertNodeInto(dmtn, parent, 0);
			index = parent.getChildCount();
			dtm.insertNodeInto(dmtn, parent, index);
			dtm.nodeChanged(parent);
		}
	}

	public void treeNodeMoveTo(DefaultMutableTreeNode treeNodeDragged, DefaultMutableTreeNode treeNodeMoved) {
		DefaultMutableTreeNode treeNodeParentDragged = (DefaultMutableTreeNode) treeNodeDragged.getParent();
		DefaultMutableTreeNode treeNodeRoot = (DefaultMutableTreeNode) treeNodeDragged.getRoot();
		DefaultMutableTreeNode treeNodeParentMoved = (DefaultMutableTreeNode) treeNodeMoved.getParent();
		if (null == treeNodeParentDragged || treeNodeDragged == treeNodeRoot
				|| (treeNodeMoved.isNodeAncestor(treeNodeDragged)) || treeNodeMoved.isNodeChild(treeNodeDragged)) {
			return;
		}
		DefaultMutableTreeNode treeNodeParent;
		int index;
		if (null == treeNodeParentMoved) {
			treeNodeParent = treeNodeRoot;
			index = treeNodeParent.getChildCount();
		} else {
			if (treeNodeMoved.getAllowsChildren()) {
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
