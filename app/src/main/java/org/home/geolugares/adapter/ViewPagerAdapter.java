package org.home.geolugares.adapter;

/**
 * Created by Kelvin on 5/12/2017.
 */

import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.home.geolugares.listener.UpdateableFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    Location location;

    public void update(Location xyzData) {
        this.location = xyzData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof UpdateableFragment) {
            ((UpdateableFragment) object).update(location);
        }
        //don't return POSITION_NONE, avoid fragment recreation.
        return super.getItemPosition(object);
    }
}
