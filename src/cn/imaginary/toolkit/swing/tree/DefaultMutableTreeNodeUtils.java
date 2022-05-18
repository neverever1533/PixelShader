package cn.imaginary.toolkit.swing.tree;

import java.util.Enumeration;
import java.util.Properties;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import javaev.lang.ObjectUtils;

public class DefaultMutableTreeNodeUtils {
	public static String tag_index = "index";

	private String tag_comma = ObjectUtils.tag_comma;
	private String tag_null = ObjectUtils.tag_null;

	public boolean equals(DefaultMutableTreeNode treeNodeAncestor, DefaultMutableTreeNode treeNodeDescendant) {
		if (null != treeNodeAncestor && null != treeNodeDescendant) {
			Object ancestor = treeNodeDescendant.getUserObject();
			if (null != ancestor) {
				ancestor = ancestor.toString();
			} else {
				ancestor = tag_null;
			}
			Object descendant = treeNodeDescendant.getUserObject();
			if (null != descendant) {
				descendant = descendant.toString();
			} else {
				descendant = tag_null;
			}
			if (descendant.equals(ancestor)) {
				return true;
			}
		}
		return false;
	}

	public DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode treeNode, Object object) {
		if (null != treeNode) {
			if (null != object) {
				object = object.toString();
			} else {
				object = tag_null;
			}
			DefaultMutableTreeNode node;
			Object obj = treeNode.getUserObject();
			if (null != obj) {
				obj = obj.toString();
			} else {
				obj = tag_null;
			}
			if (obj.equals(object)) {
				return treeNode;
			} else {
//				for (@SuppressWarnings("unchecked")
//				Enumeration<DefaultMutableTreeNode> treeNodeEnumeration = treeNode
//				.preorderEnumeration(); treeNodeEnumeration.hasMoreElements();) {
				for (@SuppressWarnings("unchecked")
				Enumeration<DefaultMutableTreeNode> treeNodeEnumeration = treeNode
						.breadthFirstEnumeration(); treeNodeEnumeration.hasMoreElements();) {
					node = treeNodeEnumeration.nextElement();
					if (null != node) {
						obj = node.getUserObject();
						if (null != obj) {
							obj = obj.toString();
						} else {
							obj = tag_null;
						}
						if (obj.equals(object)) {
							return node;
						}
					}
				}
			}
		}
		return null;
	}

	private Properties putNode(Properties properties, int index, LayerNode node) {
		if (null != properties && null != node) {
			properties.put(index, node);
			index++;
			properties.put(tag_index, index);
		}
		return properties;
	}

	public Properties storeNode(DefaultMutableTreeNode treeNode, Properties properties) {
		return storeNode(treeNode, properties, 0);
	}

	private Properties storeNode(DefaultMutableTreeNode treeNode, Properties properties, int index) {
		if (null != treeNode && index != -1) {
			if (null == properties) {
				properties = new Properties();
			}
			TreeNode nodeParent;
			DefaultMutableTreeNode node;
			Object parent = null;
			Object child = null;
			LayerNode node_;
			nodeParent = treeNode.getParent();
			if (null != nodeParent) {
				parent = ((DefaultMutableTreeNode) nodeParent).getUserObject();
			} else {
				parent = null;
			}
			if (null != parent) {
				parent = parent.toString();
			} else {
				parent = tag_null;
			}
			child = treeNode.getUserObject();
			if (null != child) {
				child = child.toString();
			} else {
				child = tag_null;
			}
			node_ = new LayerNode(index, parent, child);
			properties = putNode(properties, index, node_);
			index = (int) properties.get(tag_index);
			if (treeNode.getChildCount() != 0) {
				for (@SuppressWarnings("unchecked")
				Enumeration<DefaultMutableTreeNode> treeNodeEnumeration = treeNode.children(); treeNodeEnumeration
						.hasMoreElements();) {
					node = treeNodeEnumeration.nextElement();
					properties = storeNode(node, properties, index);
					index = (int) properties.get(tag_index);
				}
			}
		}
		return properties;
	}

	public String toString(DefaultMutableTreeNode treeNode) {
		if (null != treeNode) {
			DefaultMutableTreeNode node;
			Object object;
			StringBuffer sbuf = new StringBuffer();
			int index = 0;
//			for (@SuppressWarnings("unchecked")
//			Enumeration<DefaultMutableTreeNode> treeNodeEnumeration = treeNode
//			.preorderEnumeration(); treeNodeEnumeration.hasMoreElements();) {
			for (@SuppressWarnings("unchecked")
			Enumeration<DefaultMutableTreeNode> treeNodeEnumeration = treeNode
					.breadthFirstEnumeration(); treeNodeEnumeration.hasMoreElements();) {
				node = treeNodeEnumeration.nextElement();
				if (null != node) {
					if (index != 0) {
						sbuf.append(tag_comma);
					}
					object = node.getUserObject();
					sbuf.append(object);
					index++;
				}
			}
			return sbuf.toString();
		}
		return null;
	}
}
