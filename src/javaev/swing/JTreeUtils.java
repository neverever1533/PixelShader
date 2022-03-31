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
		for (Iterator<File> iterator = list.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode();
			if (file.isDirectory()) {
				dmtn.setAllowsChildren(true);
			} else {
				dmtn.setAllowsChildren(false);
			}
			dmtn.setUserObject(file);
			dtm.insertNodeInto(dmtn, parent, 0);
		}
	}

}
