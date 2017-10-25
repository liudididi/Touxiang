package Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by asus on 2017/10/17.
 */

public class Myxqpage extends FragmentPagerAdapter {
    private List<Fragment> list;
    private List<String> meuns;
    public Myxqpage(FragmentManager fm) {
        super(fm);
    }

    public Myxqpage(FragmentManager fm, List<Fragment> list, List<String> meuns) {
        super(fm);
        this.list = list;
        this.meuns = meuns;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override public CharSequence getPageTitle(int position) {
        return meuns.get(position);
    }
}