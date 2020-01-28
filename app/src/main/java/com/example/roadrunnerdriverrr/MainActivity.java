package com.example.roadrunnerdriverrr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    MyDataManager myDataManager = new MyDataManager();
    private ConstraintLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        loadSavedData();
        myDataManager.setScreenHeight(displayMetrics.heightPixels);
        myDataManager.setScreenWidth(displayMetrics.widthPixels);
        myDataManager.setLocationManager((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        myDataManager.updateCurLoc();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        //navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }else {
            myDataManager.setWidgetPerm(true);
        }
        container = findViewById(R.id.container);
        if(myDataManager.getBlackTheme()){
            container.setBackgroundColor(Color.BLACK);
        }else {
            container.setBackgroundColor(Color.WHITE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                myDataManager.setWidgetPerm(true);
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available.",
                        Toast.LENGTH_SHORT).show();
                myDataManager.setWidgetPerm(false);

            }
        } else if(requestCode == 1014) {
            Log.v("aa","bbbbb");
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void loadSavedData(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);

        myDataManager.setStickyEdges(settings.getBoolean("stickyEdges",false));
        myDataManager.setAutoGetPostcodeFromLoc(settings.getBoolean("autoGetPostcodeFromLoc",false));
        myDataManager.setBlackTheme(settings.getBoolean("blackTheme", false));
    }

}
