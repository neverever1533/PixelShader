package cn.imaginary.toolkit.swing.tree;

import javaev.lang.ObjectUtils;

public class LayerNode {
	public static String tag_layer_node_child = "child";
	public static String tag_layer_node_id = "id";
	public static String tag_layer_node_parent = "parent";

	private Object child_node;

	private int id_node;

	private Object parent_node;

	private String tag_bracket_close = ObjectUtils.tag_bracket_close;
	private String tag_bracket_open = ObjectUtils.tag_bracket_open;
	private String tag_comma = ObjectUtils.tag_comma;
	private String tag_equals = ObjectUtils.tag_equals;

	public LayerNode() {
	}

	public LayerNode(int id, Object parent, Object child) {
		set(id, parent, child);
	}

	public LayerNode(LayerNode layerNode) {
		set(layerNode);
	}

	public Object getChild() {
		return child_node;
	}

	public int getID() {
		return id_node;
	}

	public Object getParent() {
		return parent_node;
	}

	public void set(int id, Object parent, Object child) {
		setID(id);
		setParent(parent);
		setChild(child);
	}

	public void set(LayerNode layerNode) {
		if (null != layerNode) {
			set(layerNode.getID(), layerNode.getParent(), layerNode.getChild());
		}
	}

	public void setChild(Object child) {
		child_node = child;
	}

	public void setID(int id) {
		id_node = id;
	}

	public void setParent(Object parent) {
		parent_node = parent;
	}

	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(this.getClass().getName());
		sbuf.append(tag_bracket_open);
		sbuf.append(tag_layer_node_id);
		sbuf.append(tag_equals);
		sbuf.append(getID());
		sbuf.append(tag_comma);
		sbuf.append(tag_layer_node_parent);
		sbuf.append(tag_equals);
		sbuf.append(getParent());
		sbuf.append(tag_comma);
		sbuf.append(tag_layer_node_child);
		sbuf.append(tag_equals);
		sbuf.append(getChild());
		sbuf.append(tag_bracket_close);
		return sbuf.toString();
	}
}
