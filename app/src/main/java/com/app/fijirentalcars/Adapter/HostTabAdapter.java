package com.app.fijirentalcars.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.fijirentalcars.fragments.BookedFragment;
import com.app.fijirentalcars.fragments.EarningsFragment;
import com.app.fijirentalcars.fragments.PerformanceFragment;
import com.app.fijirentalcars.fragments.ReviewFragment;
import com.app.fijirentalcars.fragments.VehiclesFragment;

public class HostTabAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public HostTabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                VehiclesFragment VehiclesFragment = new VehiclesFragment();
                return VehiclesFragment;
//            case 1:
//                BookedFragment sportFragment = new BookedFragment();
//                return sportFragment;
            case 1:
                PerformanceFragment performanceFragment = new PerformanceFragment   ();
                return performanceFragment;
            case 2:
                ReviewFragment reviewFragment = new ReviewFragment();
                return reviewFragment;
            case 3:
                EarningsFragment earningsFragment = new EarningsFragment();
                return earningsFragment;
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
