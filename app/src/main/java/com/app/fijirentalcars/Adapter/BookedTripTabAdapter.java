package com.app.fijirentalcars.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.fijirentalcars.fragments.BookedFragment;
import com.app.fijirentalcars.fragments.DetailFragment;
import com.app.fijirentalcars.fragments.HistoryFragment;

public class BookedTripTabAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public BookedTripTabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DetailFragment detailFragment = new DetailFragment();
                return detailFragment;
            case 1:
                BookedFragment sportFragment = new BookedFragment();
                return sportFragment;
            case 2:
                HistoryFragment movieFragment = new HistoryFragment();
                return movieFragment;
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
