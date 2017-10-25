package com.sunkoiwish.waphotorecon.FireBase;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.sunkoiwish.waphotorecon.Home.HomeActivity;
import com.sunkoiwish.waphotorecon.R;
import com.sunkoiwish.waphotorecon.Utils.BottomNavHelper;

import static com.sunkoiwish.waphotorecon.Utils.BottomNavHelper.setupBottomNavigationView;

/**
 * Created by Xaiyeon on 10/24/2017.
 */

public class FireBaseActivity extends HomeActivity {

    private static final String TAG = "FireBaseActivity";

    // This is used for highlighting the selected activity in the menu, starts at 0
    private static final int ACTIVITY_NUMBER = 1;

    // Used in enableBotNavigation
    private Context mContext = FireBaseActivity.this;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: started.");

    }


    /**
     * BottomNavigationEX View function

    private void setupBottomNavigationExView(){

        Log.d(TAG, "setupBottomNavigationExView: Started.");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.main_bot_navBarEX);
        setupBottomNavigationView(bottomNavigationViewEx);

        BottomNavHelper.enableBotNavigation(mContext, bottomNavigationViewEx );

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);



    }
     */

}
