package app.paypro.payproapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import app.paypro.payproapp.asynctask.db.user.GetAccountAsyncTask;

/**
 * Created by rogerbaiget on 15/11/17.
 */

public class AccountFragment extends Fragment {

    private app.paypro.payproapp.db.entity.Account account;
    private TextView currencyBalance;

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

        ViewPager viewPager = (ViewPager) getView().findViewById(R.id.viewpager);

        AccountFragmentPagerAdapter adapter = new AccountFragmentPagerAdapter(getContext(), getChildFragmentManager());

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        try {
            account = new GetAccountAsyncTask(getContext()).execute().get()[0];
            currencyBalance = getView().findViewById(R.id.currency_balance);

            if(account.getBalance() != null){
                NumberFormat format = NumberFormat.getInstance(Locale.UK);
                String amount = format.format(account.getBalance());
                amount = "μ\u20BF " + amount;
                currencyBalance.setText(amount);
            }else{
                currencyBalance.setText(R.string.balance_empty);
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}