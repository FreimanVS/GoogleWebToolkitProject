package com.mySampleApplication.shared.model;


import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "role", schema = "company")
public class Role implements Serializable {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name = "name")
    private String name;

    @JsonbTransient
    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<Employee> empls = new HashSet<>();

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public Set<Employee> getEmpls() {
        return empls;
    }


    public void setEmpls(Set<Employee> empls) {
        this.empls = empls;
    }

    @Override
    public String toString() {
        return name;
    }
}
