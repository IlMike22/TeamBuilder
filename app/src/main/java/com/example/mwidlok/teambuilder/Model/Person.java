package com.example.mwidlok.teambuilder.Model;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mike on 25.06.2017.
 */

public class Person extends RealmObject {


    @PrimaryKey
    private int id;
    private String lastName;
    private int age;
    private String firstName;
    private int skillLevel;

    // parameter constructor

    public Person()
    {
    }

    public Person(int id, String firstname, String lastname, int age, int skillLevel)
    {
        this.id = id;
        this.firstName = firstname;
        this. lastName = lastname;
        this.age = age;
        this.skillLevel = skillLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }
}
