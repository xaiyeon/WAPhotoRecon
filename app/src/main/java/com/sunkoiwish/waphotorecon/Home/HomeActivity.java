package com.sunkoiwish.waphotorecon.Home;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.sunkoiwish.waphotorecon.Account.AccountFragment;
import com.sunkoiwish.waphotorecon.BlueTooth.BlueToothFragment;
import com.sunkoiwish.waphotorecon.Connect.ConnectFragment;
import com.sunkoiwish.waphotorecon.FireBase.FireBaseFragment;
import com.sunkoiwish.waphotorecon.Images.ImagesFragment;
import com.sunkoiwish.waphotorecon.R;
import com.sunkoiwish.waphotorecon.Utils.BottomNavHelper;
import com.sunkoiwish.waphotorecon.WiFi.WiFiFragment;

/**
 * All other activities aren't used anymore and are replaced with fragments
 * Only three activities are used with is Login, Register, and Home.
 * Home is the main activity to do most of the actions regards to user.
 *
 *
 *
 *
 */


public class HomeActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";

    // This is used for highlighting the selected activity in the menu, starts at 0
    // This is NOT USED, since switched to fragments.
    private static final int ACTIVITY_NUMBER = 0;

    // used for getting location
    private LocationManager locationManager;
    private LocationListener locationListener;

    // Was used enableBotNavigation
    private Context mContext = HomeActivity.this;

    // firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG,"onCreate: HomeActivity");

        // init firebase
        setupFireBaseAuth();

        // Calling our setnavigationEX
        BottomNavigationViewEx navigation = (BottomNavigationViewEx) findViewById(R.id.main_bot_navBarEX);
        BottomNavHelper.setupBottomNavigationView(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Sets-up 3 tabs for main, camera, log
        setupViewPager();
    }


    /**
     * SectionsPagerAdapter things
     */
    private void setupViewPager(){

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(this.getSupportFragmentManager());
        adapter.addFragment(new CameraFragment());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new BBLogFragment());
        ViewPager viewPager = (ViewPager) findViewById(R.id.center_main_container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_midnone);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_log);

    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.ic_house:
                    fragmentTransaction.replace(R.id.center_main_container, new HomeFragment()).commit();
                    return true;

                case R.id.ic_cloud:
                    fragmentTransaction.replace(R.id.main_center_content, new FireBaseFragment()).commit();
                    return true;

                case R.id.ic_imagedetails:
                    fragmentTransaction.replace(R.id.main_center_content, new ImagesFragment()).commit();
                    return true;

                case R.id.ic_connect:
                    fragmentTransaction.replace(R.id.main_center_content, new ConnectFragment()).commit();
                    return true;

                case R.id.ic_account:
                    fragmentTransaction.replace(R.id.main_center_content, new AccountFragment()).commit();
                    return true;

            }
            return false;
        }

    };


    /**
     * BottomNavigationEX View function

    private void setupBottomNavigationExView(){

        Log.d(TAG, "setupBottomNavigationExView: Started.");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.main_bot_navBarEX);
        BottomNavHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavHelper.enableBotNavigation(mContext, bottomNavigationViewEx );
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);

    }
     */

    // START OF FIREBASE STUFF
    // onStart and onStop is NEEDED for FIREBASE!

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // LOADING OUR FIREBASE AUTHENTICATION
    private void setupFireBaseAuth(){
        Log.d(TAG, "setupFireBaseAuth: setting up firebase auth.");
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }


// END OF FIREBASE STUFF ---------------





}
