package app.paypro.payproapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rogerbaiget on 15/11/17.
 */

public class AccountTransactionsFragment extends Fragment {
    public static AccountTransactionsFragment newInstance() {
        AccountTransactionsFragment fragment = new AccountTransactionsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_transactions_tab, container, false);
    }
}
