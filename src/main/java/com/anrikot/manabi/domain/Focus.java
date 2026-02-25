package com.anrikot.manabi.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Table(
    name = "focus",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "name"})
)
@Entity
public class Focus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Focus parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Focus> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Focus() {}

    public Focus(Long id, String name, Focus parent, List<Focus> children, User user) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.children = children;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Focus getParent() {
        return parent;
    }

    public void setParent(Focus parent) {
        this.parent = parent;
    }

    public List<Focus> getChildren() {
        return children;
    }

    public void setChildren(List<Focus> children) {
        this.children = children;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}