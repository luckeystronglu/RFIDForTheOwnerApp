package com.foxconn.rfid.theowner.activity.message;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lenovo on 2016/9/13.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    private String[] tabs;

    public TabsPagerAdapter(FragmentManager fm, String[] tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new FragmentSystemMsg();
        }
        else if (position == 1){
            return new FragmentSafetyGuard();
        }
//        switch (position) {
//            case 0:
//                return new Fragment01();
//            case 1:
//                return new Fragment02();
//            case 2:
//                return new Fragment03();
//            case 3:
//                return new Fragment04();
//            case 4:
//                return new Fragment05();
//        }
        return new FragmentBikeStatus();
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
