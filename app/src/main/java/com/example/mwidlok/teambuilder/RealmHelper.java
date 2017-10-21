package com.example.mwidlok.teambuilder;

import android.support.v7.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Mike on 21.10.2017.
 */

public class RealmHelper {

    public static Realm getRealmInstance()
    {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("myRealmDatabase.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        return Realm.getInstance(realmConfig);
    }
}
