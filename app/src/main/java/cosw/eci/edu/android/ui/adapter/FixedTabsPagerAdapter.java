package cosw.eci.edu.android.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cosw.eci.edu.android.R;
import cosw.eci.edu.android.ui.fragment.ListAllFragment;
import cosw.eci.edu.android.ui.fragment.ListJoinedFragment;
import cosw.eci.edu.android.ui.fragment.ListOwnedFragment;

public class FixedTabsPagerAdapter extends FragmentPagerAdapter {

    private static final int COUNT = 3;
    private Context context;

    public FixedTabsPagerAdapter(FragmentManager fragmentManager, Context context){
        super(fragmentManager);
        this.context = context;

    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new ListAllFragment();
            case 1:
                return new ListOwnedFragment();
            case 2:
                return new ListJoinedFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.all);
            case 1:
                return context.getResources().getString(R.string.owned);
            case 2:
                return context.getResources().getString(R.string.joined);
            default:
                return null;
        }
    }
}
