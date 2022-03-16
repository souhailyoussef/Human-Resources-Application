package com.example.app.domain;

import com.fasterxml.jackson.annotation.*;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Data
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Node {
    @Id
    @GeneratedValue(strategy = IDENTITY)

    private Long id;

    private int depth;

    private double value;

    @Column(name="name")
    private String name;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.EAGER) //add column definitions as needed
    @JoinColumn(name="parent_id")
    private Node parent;      //each Domain with parent==null is a root domain, all others are subdomains


    @OneToMany(mappedBy = "parent", orphanRemoval = true,fetch=FetchType.EAGER) //add column definitions as needed
    private List<Node> children;



    public Node(final Node parent, String name) {
        if(parent==null) throw new IllegalArgumentException("parent required");

        this.parent = parent;
        this.name=name;
        this.children = new ArrayList<>();
        if (parent==null) {
            this.depth=0;
        }
        else {
            this.depth = (parent.getDepth() + 1);
        }
    }
    public Node() {
        this.children = new ArrayList<>();
        this.name="root";
        this.value=0;
        if (parent==null) {
            this.depth=0;
        }
        else {
            this.depth = (parent.getDepth() + 1);
        }

    }
    public Node(final Node parent, String name, double value) {
        if(parent==null) throw new IllegalArgumentException("parent required");

        this.parent = parent;
        this.name=name;
        this.value=value;
        this.children = new ArrayList<>();
        if (parent==null) {
            this.depth=0;
        }
        else {
            this.depth = (parent.getDepth() + 1);
        }
    }

    public List<Node> getChildren() {
        return this.children;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void addChild(Node child) {

        this.children.add(child);
        this.value=0;
    }
    public void setParent(Node parent) {
        this.setDepth(parent.getDepth() + 1);
        parent.addChild(this);
        this.parent = parent;
        parent.value=0;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public boolean isRootNode() {
        return (this.parent == null);
    }

    public boolean isLeafNode() {
        return (this.children.size() == 0);
    }



    @Override
    public String toString() {
        String out = "";
        out += "Node: " + this.name + " | Depth: " + this.depth + " | Value: " + this.value + " | Parent: " + (this.parent == null ? "None" : this.parent.name) + " | Children: " + (this.children.size() == 0 ? "None" : "");
        for (Node child : this.children) {
            out += "\n\t" + child.name + " | Parent: " + (child.parent == null ? "None" : child.parent.name + " | Value: " + child.value);
        }
        return out;
    }


    public void sumNode() {
        // for empty trees or leafs tree, do nothing

        if (this == null ){
            return;
        }
        //update the children subtrees
        for (Node node : this.getChildren()) {
            this.value=0;            //reset node value to 0 so previous value dont affect the resultss
            node.sumNode();

        }
        // At this point, all the elements in the left and right
        // subtrees are already summed up. Now we update the
        // sum in the root element itself.

        for (Node node : this.getChildren()) {
            if (node != null) {
                this.value += node.value;
            }


        }


    }

    public static Node createRoot() {
        return new Node();
    }


}