package com.javarockstars.mpp.structures.test;

/**
 * Author: dedocibula
 * Created on: 6.10.2015.
 */
public class Person {
    private int id;
    private String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
