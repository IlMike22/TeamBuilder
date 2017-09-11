package com.example.mwidlok.teambuilder.Model;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

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

    public Person()
    {
    }

    public Person( String firstname, String lastname, int age, SkillLevel skillLevel)
    {
        this.firstName = firstname;
        this. lastName = lastname;
        this.age = age;
        this.skillLevel = skillLevel;
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

    public void setSkillLevel(SkillLevel skillLevel) {
        this.skillLevel = skillLevel;
    }
}
