package javaev.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import java.io.File;
import java.io.IOException;

import java.util.Iterator;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

public class JTreeDnD extends JTree implements DropTargetListener {
	/**
	 * @author Sureness
	 * @since 0.1
	 */
	private static final long serialVersionUID = 4573533967201701426L;

	public JTreeDnD() {
		super();
		addNewDropTarget();
	}

	public JTreeDnD(TreeModel newModel) {
		super(newModel);
		addNewDropTarget();
	}

	public JTreeDnD(TreeNode root) {
		super(root);
		addNewDropTarget();
	}

	public JTreeDnD(TreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
		addNewDropTarget();
	}

	private void addNewDropTarget() {
		new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
	}

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

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		System.out.println("DragOver");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void drop(DropTargetDropEvent dtde) {
		System.out.println("Drop");
		DataFlavor dataFlavor = DataFlavor.javaFileListFlavor;
		if (dtde.isDataFlavorSupported(dataFlavor)) {
			dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
			List<File> list = null;
			Transferable transferable = dtde.getTransferable();
			try {
				list = (List<File>) transferable.getTransferData(dataFlavor);
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			addNewUserObjects((DefaultMutableTreeNode) this.getModel().getRoot(), list);
			dtde.dropComplete(true);
		}
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		System.out.println("DropActionChanged");
	}

}
