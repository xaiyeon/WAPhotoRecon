package com.sunkoiwish.waphotorecon.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.sunkoiwish.waphotorecon.BlueTooth.BlueToothActivity;
import com.sunkoiwish.waphotorecon.FireBase.FireBaseActivity;
import com.sunkoiwish.waphotorecon.Home.HomeActivity;
import com.sunkoiwish.waphotorecon.Images.ImagesActivity;
import com.sunkoiwish.waphotorecon.R;
import com.sunkoiwish.waphotorecon.WiFi.WiFiActivity;

/**
 * Created by Xaiyeon on 10/24/2017.
 */

public class BottomNavHelper extends HomeActivity{

    private static final String TAG = "BottomViewHelper";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: setting up nav bot view");
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);
    }


    // The fragment selecter is in HomeActivity Right now
    // Change these to get Fragments instead...
    // Dont need this anymore
    /**
    public static void enableBotNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_house:
                        Intent homeIntent = new Intent(context, HomeActivity.class);
                        context.startActivity(homeIntent);
                        break;

                    case R.id.ic_cloud:
                        Intent firebaseIntent = new Intent(context, FireBaseActivity.class);
                        context.startActivity(firebaseIntent);
                        break;

                    case R.id.ic_imagedetails:
                        Intent imagedetailsIntent = new Intent(context, ImagesActivity.class);
                        context.startActivity(imagedetailsIntent);
                        break;

                    case R.id.ic_connect:
                        Intent wifiIntent = new Intent(context, WiFiActivity.class);
                        context.startActivity(wifiIntent);
                        break;

                    case R.id.ic_account:
                        Intent BlueToothIntent = new Intent(context, BlueToothActivity.class);
                        context.startActivity(BlueToothIntent);
                        break;
                }



                return false;
            }
        });


    }
    */

}
