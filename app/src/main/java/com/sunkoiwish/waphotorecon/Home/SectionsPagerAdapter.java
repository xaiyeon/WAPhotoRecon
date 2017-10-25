package com.sunkoiwish.waphotorecon.Home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xaiyeon on 10/24/2017.
 */

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "SectionsPagerAdapter";
    private final List<Fragment> mFragmentList = new ArrayList<>();


    public SectionsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        return mFragmentList.get(position);

    }

    @Override
    public int getCount(){
        return mFragmentList.size();

    }

    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);

    }


}
