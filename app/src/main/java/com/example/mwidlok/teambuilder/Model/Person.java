package com.example.mwidlok.teambuilder.Model;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mike on 25.06.2017.
 */


public class Person extends RealmObject implements Serializable{

    @PrimaryKey
    private int id;
    private String lastName;
    private int age;
    private String firstName;
    private int skillLevel;    // due to realm does not support enums we have to set skill level with integers. 0 = amateur, 1 = average, 2 = profi
    private int teamId;
    private String skillLevelDescription;
    private Date createDate;
    private Date updateDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getSkillLevelDescription() {
        return skillLevelDescription;
    }

    public void setSkillLevelDescription(String skillLevelDescription) {
        this.skillLevelDescription = skillLevelDescription;
    }

    public Person()
    {
    }

    public Person( String firstname, String lastname, int age, int skillLevel, int teamId)
    {
        this.firstName = firstname;
        this. lastName = lastname;
        this.age = age;
        this.skillLevel = skillLevel;
        this.teamId = teamId;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
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


    public int getSkillLevel() {return skillLevel;}

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

}
