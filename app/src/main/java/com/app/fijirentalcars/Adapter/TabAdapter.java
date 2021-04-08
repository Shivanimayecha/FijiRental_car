package com.app.fijirentalcars.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.fijirentalcars.fragments.ActivityFragment;
import com.app.fijirentalcars.fragments.BookedFragment;
import com.app.fijirentalcars.fragments.HistoryFragment;

public class TabAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public TabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ActivityFragment homeFragment = new ActivityFragment();
                return homeFragment;
            case 1:
                BookedFragment sportFragment = new BookedFragment();
                return sportFragment;
            case 2:
                HistoryFragment movieFragment = new HistoryFragment();
                return movieFragment;
            case 3:
                HistoryFragment fragment3 = new HistoryFragment();
                return fragment3;
            case 4:
                HistoryFragment fragment4 = new HistoryFragment();
                return fragment4;
            default:
                return null;
        }
    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
