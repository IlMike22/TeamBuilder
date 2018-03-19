package com.example.mwidlok.teambuilder;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.mwidlok.teambuilder.Model.Person;
import com.example.mwidlok.teambuilder.Model.Team;
import com.scand.realmbrowser.RealmBrowser;
import java.util.ArrayList;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmObject;

import static com.example.mwidlok.teambuilder.RealmHelper.getRealmInstance;

public class BaseActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onCreateDrawer();
    }

    protected void onCreateDrawer() {
        //super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navView = (NavigationView) findViewById(R.id.navigation);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Log.i("TeamBuilder", "Item selected");
                item.setChecked(true);
                drawerLayout.closeDrawers();
                int itemId = item.getItemId();
                switch(itemId)
                {
                    case R.id.nav_home:
                        Log.i("TeamBuilder","hell yeah!");
                        break;

                    case R.id.nav_impressum:
                        Intent intent = new Intent(getApplicationContext(), ImpressumActivity.class);
                        startActivity(intent);
                        break;

                }
                return true;
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawerOpen, R.string.drawerClose) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle("TeamBuilder");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerView.bringToFront();
                drawerView.requestLayout();
                getSupportActionBar().setTitle("TeamBuilder");
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        drawerLayout.openDrawer(GravityCompat.START);

//        if (drawerToggle.onOptionsItemSelected(item))
//            return true;

        switch (item.getItemId()) {
            case R.id.deleteDb:
                Log.i("TeamBuilder", "Delete Db Option selected");
                if (deleteDatabase())
                    Toast.makeText(getApplicationContext(), "Realm database was successfully deleted.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Realm database couldn't be deleted.", Toast.LENGTH_SHORT).show();
            case R.id.showDbBrowser:
                List<Class<? extends RealmObject>> classes = new ArrayList<>();
                classes.add(Person.class);
                classes.add(Team.class);

                Log.i("TeamBuilder", "No showing Realm Browser");
                new RealmBrowser.Builder(this)
                        .add(getRealmInstance(), classes)
                        .show();
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean deleteDatabase() {
        Realm myDb = RealmHelper.getRealmInstance();
        Log.i("TeamBuilder", "Trying to delete realm db..");
        myDb.close();

        return Realm.deleteRealm(myDb.getConfiguration());
    }


}
