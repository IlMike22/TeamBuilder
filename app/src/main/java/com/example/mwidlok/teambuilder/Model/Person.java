package com.example.mwidlok.teambuilder.Model;

/**
 * Created by Mike on 25.06.2017.
 */

public class Person {

    public enum SkillLevel
    {
        Amateur, Average, Profi
    }

    private String lastName;
    private int age;
    private String firstName;
    private SkillLevel skillLevel;

    // parameter constructor

    public Person(String firstname, String lastname, int age, SkillLevel skilllevel)
    {
        this.firstName = firstname;
        this. lastName = lastname;
        this.age = age;
        this.skillLevel = skilllevel;
    }

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

    public SkillLevel getSkillLevel() {return skillLevel; }
    public void SetSkillLevel(SkillLevel skillLevel) {this.skillLevel = skillLevel; }
}
