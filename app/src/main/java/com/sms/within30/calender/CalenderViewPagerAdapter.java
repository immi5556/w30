package com.sms.within30.calender;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by NNBS PRAKASH RAO on 9/20/2015.
 */
public class CalenderViewPagerAdapter extends FragmentStatePagerAdapter {

    public CalenderViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return CalendarFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return CalendarUtils.DAYS_TO_SHOW/7;
    }

}
