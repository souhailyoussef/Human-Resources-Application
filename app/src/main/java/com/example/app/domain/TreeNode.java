package com.example.app.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * create a parent-->child tree while preserving their depth/history.
 */
public class TreeNode<T> {
    private final List<TreeNode<T>> children;
    private TreeNode<T> parent;
    private T data;
    private int depth;
    private double value;
    private double new_value=0;


    public TreeNode(T data, TreeNode<T> parent) {
        // new node with a given parent
        this.children = new ArrayList<>();
        this.data = data;
        this.parent = parent;
        this.depth = (parent.getDepth() + 1);
        this.value = 0;
        parent.addChild(this);
    }

    public TreeNode(T data) {
        // a fresh node, without a parent reference
        this.children = new ArrayList<>();
        this.parent = null;
        this.data = data;
        this.depth = 0; // 0 is the base level (only the root should be on there)
        this.value = 0;
    }

    public TreeNode(T data, TreeNode<T> parent, double value) {
        // new node with a given parent
        this.children = new ArrayList<>();
        this.data = data;
        this.parent = parent;
        this.depth = (parent.getDepth() + 1);
        this.value = value;
        parent.addChild(this);
    }


    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setParent(TreeNode<T> parent) {
        this.setDepth(parent.getDepth() + 1);
        parent.addChild(this);
        this.parent = parent;
        parent.value=0;
    }

    public TreeNode<T> getParent() {
        return this.parent;
    }

    public void addChild(T data) {
        TreeNode<T> child = new TreeNode<>(data);
        this.children.add(child);
        this.value=0;
    }

    public void addChild(TreeNode<T> child) {

        this.children.add(child);
        this.value=0;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isRootNode() {
        return (this.parent == null);
    }

    public boolean isLeafNode() {
        return (this.children.size() == 0);
    }

    public void removeParent() {
        this.parent = null;
    }

    @Override
    public String toString() {
        String out = "";
        out += "Node: " + this.getData().toString() + " | Depth: " + this.depth + " | Value: " + this.value + " | Parent: " + (this.getParent() == null ? "None" : this.parent.getData().toString()) + " | Children: " + (this.getChildren().size() == 0 ? "None" : "");
        for (TreeNode<T> child : this.getChildren()) {
            out += "\n\t" + child.getData().toString() + " | Parent: " + (child.getParent() == null ? "None" : child.getParent().getData() + " | Value: " + child.getValue());
        }
        return out;
    }

    public void sumNode() {
        // for empty trees or leafs tree, do nothing

        if (this == null) {
            return;
        }
        //update the children subtrees
        for (TreeNode<T> node : this.getChildren()) {
            this.value=0;            //reset node value to 0 so previous value dont affect the resultss
            node.sumNode();

        }
        // At this point, all the elements in the left and right
        // subtrees are already summed up. Now we update the
        // sum in the root element itself.

        for (TreeNode<T> node : this.getChildren()) {
            if (node != null) {
                this.value += node.value;
            }


        }



    }
}
