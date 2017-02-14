package com.example.fearking.shopkart;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Adapter extends FragmentPagerAdapter {
    public Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:return new CustomerLoginFragment();
            case 1:return new RetailerLogin();
            default:break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
