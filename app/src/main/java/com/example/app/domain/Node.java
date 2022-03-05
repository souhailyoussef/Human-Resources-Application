package com.example.app.domain;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
public class Node {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Node parent;

    @OneToMany(mappedBy = "parent", cascade =  { CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Node> children;

    //TODO : implement constructor and functions
  /*  public Rubrique ( Rubrique parent, String name) {
        if (parent == null) {
            System.out.println("error! null parent");
        } else {
            this.parent = parent;
            this.name = name;
        }
    }*/

    /*
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;



    @ManyToOne
    private Rubrique parent;

    @NotNull  children must have a parent
    @OneToMany(mappedBy = "parent")
    private Set<Rubrique> children = new HashSet<Rubrique>();

    public Rubrique (final Rubrique parent) {
        if (parent == null) throw new IllegalArgumentException("parent required");
        this.parent=parent;
        registerInParentsChildren();
    }*/
     /* Register this rubrique in the child list of its parent.
    private void registerInParentsChildren() {
        this.parent.children.add(this);
    }

    public Set<Rubrique> getChildren() {
        return this.children;
    }

    public void move(final Rubrique newParent) {
       // Check.NotNullArugment(newParent, "newParent");
        try {
            this.parent.children.remove(this);
            this.parent= newParent;
            registerInParentsChildren();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("move", e.getCause());
        }

    }
    public static Rubrique createRoot() {
        return new Rubrique();
    }
*/

}
