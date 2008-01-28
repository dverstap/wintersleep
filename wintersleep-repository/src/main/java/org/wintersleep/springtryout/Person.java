package org.wintersleep.springtryout;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Person implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Basic(optional = false)
    @Column(nullable = false, unique = true, updatable = false)
    private String name;

    protected Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "Person: " + id + ": " + name;
    }
}
