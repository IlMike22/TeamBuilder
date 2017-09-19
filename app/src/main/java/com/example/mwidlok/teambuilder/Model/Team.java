package com.example.mwidlok.teambuilder.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mike on 18.09.2017.
 */

public class Team extends RealmObject {

    @PrimaryKey
    private int id;

    private String name;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
