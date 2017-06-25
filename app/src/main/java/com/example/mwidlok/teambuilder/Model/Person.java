package com.example.mwidlok.teambuilder.Model;

/**
 * Created by Mike on 25.06.2017.
 */



public class Person {

    enum SkillLevel
    {
        Amateur, Average, Profi
    }

    private String lastName;
    private int age;
    private String firstName;
    private SkillLevel skillLevel;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
