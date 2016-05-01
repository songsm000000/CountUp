package ssm.countup;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TotalPageAdp extends FragmentPagerAdapter {

    TotalFragment inTotalFragment = null;
    TotalFragment outTotalFragment = null;

    public TotalPageAdp(FragmentManager fm, ArrayList<TotalFragment> totals) {
        super(fm);
        inTotalFragment = totals.get(0);
        outTotalFragment = totals.get(1);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = inTotalFragment;
                break;
            case 1:
                fragment = outTotalFragment;
                break;
        }

        return fragment;
    }

}
