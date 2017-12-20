package app.paypro.payproapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import app.paypro.payproapp.asynctask.db.user.GetAccountAsyncTask;
import app.paypro.payproapp.db.entity.Account;

/**
 * Created by rogerbaiget on 15/11/17.
 */

public class AccountFragment extends Fragment implements AccountFragmentsInterface{

    private TextView currencyBalance;
    private AccountInfoFragment infoFragment;
    private AccountTransactionsFragment transactionsFragment;

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_tab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView toolbarTitle = getActivity().findViewById(R.id.app_toolbar_title);
        toolbarTitle.setText("Account");

        ImageButton toolbar_back_button_image = getActivity().findViewById(R.id.app_toolbar_back_button_image);
        toolbar_back_button_image.setVisibility(View.INVISIBLE);

        TextView toolbar_back_button_text = getActivity().findViewById(R.id.app_toolbar_back_button_text);
        toolbar_back_button_text.setVisibility(View.INVISIBLE);

        Button confirmButton = getActivity().findViewById(R.id.app_toolbar_confirm_button);
        confirmButton.setVisibility(View.INVISIBLE);

        ProgressBar progressBar = getActivity().findViewById(R.id.app_toolbar_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        currencyBalance = getView().findViewById(R.id.currency_balance);
        ViewPager viewPager = (ViewPager) getView().findViewById(R.id.viewpager);

        AccountFragmentPagerAdapter adapter = new AccountFragmentPagerAdapter(getContext(), getChildFragmentManager());

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        try {
            this.refreshInfo(new GetAccountAsyncTask(getContext()).execute().get()[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefreshInfo(app.paypro.payproapp.db.entity.Account account) {

        refreshInfo(account);

        ((AccountInfoFragment) infoFragment).onRefreshInfo(account);
    }

    public void refreshInfo(app.paypro.payproapp.db.entity.Account account){

        if(account.getBalance() != null){
            NumberFormat format = NumberFormat.getInstance(Locale.UK);
            String amount = format.format(account.getBalance());
//            amount = "μ\u20BF " + amount;
            currencyBalance.setText(amount);
        }else{
            currencyBalance.setText(R.string.balance_empty);
        }

    }

    public class AccountFragmentPagerAdapter extends FragmentPagerAdapter {

        private Context mContext;

        public AccountFragmentPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new AccountInfoFragment();
            } else {
                return new AccountTransactionsFragment();
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 0:
                    infoFragment = (AccountInfoFragment) createdFragment;
                    break;
                case 1:
                    transactionsFragment = (AccountTransactionsFragment) createdFragment;
                    break;
            }
            return createdFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
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
}