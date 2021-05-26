package com.garifullin_timur.testing;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String prefString = sharedPreferences.getString("theme", getString(R.string.purple));
        Log.d("theme", prefString);
        if (prefString.equals(getString(R.string.purple))){
           setTheme(R.style.Theme_Testing);
            Log.d("theme", "сработало");
        }
        else if (prefString.equals(getString(R.string.blue))){
            setTheme(R.style.Theme_Blue);
            Log.d("theme", "сработало тема");

        }
        else if (prefString.equals(getString(R.string.orange))){
            setTheme(R.style.Theme_Orange);
            Log.d("theme", "сработало тема");

        }
        else {
            setTheme(R.style.Theme_Testing);
            Log.d("theme", "сработало");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                if (sharedPreferences.getBoolean("send_messages", true)){
//                    Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);
//                    PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this,
//                            0, notificationIntent,
//                            PendingIntent.FLAG_CANCEL_CURRENT);
//                    NotificationCompat.Builder builder =
//                            new NotificationCompat.Builder(MainActivity.this, "Murkat")
//                                    .setContentTitle("Напоминание")
//                                    .setContentText("Пора выполнить домашнее задание")
//                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
//                                            R.mipmap.ic_cat_launcher))
//                                    .setSmallIcon(R.mipmap.ic_cat_launcher)
//                                    .setContentIntent(contentIntent)
//                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//
//                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
//                    NotificationChannel channel = new NotificationChannel("Murkat", "Murkat", NotificationManager.IMPORTANCE_HIGH);
//                    notificationManager.createNotificationChannel(channel);
//                    notificationManager.notify(101, builder.build());
//                    Log.e("work", "wokr");
//                }
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_rasp,   R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.settingsFragment2)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}