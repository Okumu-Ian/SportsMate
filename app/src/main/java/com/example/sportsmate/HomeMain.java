package com.example.sportsmate;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class HomeMain extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(this,registerActivity.class));
            finish();
        }
        checkData();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(item.getItemId() == R.id.action_settings){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,registerActivity.class));
        finish();
       }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void checkData(){
        SQLiteDatabase database = openOrCreateDatabase("GAMES",MODE_PRIVATE,null);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        boolean firstData = getSharedPreferences("FIRST_DATA",MODE_PRIVATE).getBoolean("FIRST_DATA",true);
        String insertData = "INSERT into games(game_title,game_date,game_creator,game_joined,game_time,game_price) VALUES (?,?,?,?,?,?)";
        String [] titles = {"Table Tennis A", "Badminton B","Soda Ball","Beach Volleyball"};
        String [] dates = {"01/02/2019","01/09/2020","09/09/2020","15/12/2019"};
        String [] creators = {"AxAx",uid,"BxO0LP1",uid};
        String joined = "No";

        if(firstData) {

            String createTable = "CREATE TABLE IF NOT EXISTS games(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "game_title VARCHAR(20) NOT NULL, " +
                    "game_date VARCHAR(10) NOT NULL," +
                    "game_creator VARCHAR(30) NOT NULL," +
                    "game_joined VARCHAR(10) NOT NULL," +
                    "game_time VARCHAR(10) NOT NULL," +
                    "game_price VARCHAR(10) NOT NULL)";
            database.execSQL(createTable);

            for (int i = 0; i < titles.length; i++) {
                Object[] data = {titles[i], dates[i], creators[i],joined,"12:05","2000"};
                database.execSQL(insertData, data);
            }
        }
        getSharedPreferences("FIRST_DATA",MODE_PRIVATE).edit().putBoolean("FIRST_DATA",false).apply();

    }

}
