package app.paypro.payproapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by rogerbaiget on 15/11/17.
 */

public class AccountFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public AccountFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AccountInfoFragment();
        } else {
            return new AccountTransactionsFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.info_tab_title);
            case 1:
                return mContext.getString(R.string.transactions_tab_title);
            default:
                return null;
        }
    }

}