package cn.imaginary.toolkit.swing.tree;

import javaev.lang.ObjectUtils;

public class LayerNode {
	public static String tag_layer_node_id = "id";
	public static String tag_layer_node_super = "super";
	public static String tag_layer_node_this = "this";

	private int id_layer;

	private Object object_super;
	private Object object_this;

	private String tag_bracket_close = ObjectUtils.tag_bracket_close;
	private String tag_bracket_open = ObjectUtils.tag_bracket_open;
	private String tag_comma = ObjectUtils.tag_comma;
	private String tag_equals = ObjectUtils.tag_equals;

	public LayerNode() {
	}

	public LayerNode(int id, Object super_object, Object this_object) {
		set(id, super_object, this_object);
	}

	public LayerNode(LayerNode layerNode) {
		set(layerNode);
	}

	public int getID() {
		return id_layer;
	}

	public Object getObject() {
		return object_this;
	}

	public Object getObjectSuper() {
		return object_super;
	}

	public void set(int id, Object super_object, Object this_object) {
		setID(id);
		setObjectSuper(super_object);
		setObject(this_object);
	}

	public void set(LayerNode layerNode) {
		if (null != layerNode) {
			set(layerNode.getID(), layerNode.getObjectSuper(), layerNode.getObject());
		}
	}

	public void setID(int id) {
		id_layer = id;
	}

	public void setObject(Object object) {
		object_this = object;
	}

	public void setObjectSuper(Object object) {
		object_super = object;
	}

	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(this.getClass().getName());
		sbuf.append(tag_bracket_open);
		sbuf.append(tag_layer_node_id);
		sbuf.append(tag_equals);
		sbuf.append(getID());
		sbuf.append(tag_comma);
		sbuf.append(tag_layer_node_super);
		sbuf.append(tag_equals);
		sbuf.append(getObjectSuper());
		sbuf.append(tag_comma);
		sbuf.append(tag_layer_node_this);
		sbuf.append(tag_equals);
		sbuf.append(getObject());
		sbuf.append(tag_bracket_close);
		return sbuf.toString();
	}
}
