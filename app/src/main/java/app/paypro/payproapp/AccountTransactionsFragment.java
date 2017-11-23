package app.paypro.payproapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import app.paypro.payproapp.db.entity.Account;
import app.paypro.payproapp.db.entity.Transaction;

/**
 * Created by rogerbaiget on 15/11/17.
 */

public class AccountTransactionsFragment extends Fragment{

    private TransactionsViewModel viewModel;
    private TransactionsRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;


    public static AccountTransactionsFragment newInstance() {
        AccountTransactionsFragment fragment = new AccountTransactionsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_transactions_tab, container, false);

        recyclerView = v.findViewById(R.id.transactions_recycleview);
        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        recyclerViewAdapter = new TransactionsRecyclerViewAdapter(new ArrayList<Transaction>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(recyclerViewAdapter);

        viewModel = ViewModelProviders.of(this).get(TransactionsViewModel.class);
        viewModel.getTransactionsData().observe(getActivity(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(@Nullable List<Transaction> transactions) {
                int size = transactions.size();
                recyclerViewAdapter.addItems(transactions);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((TabActivity)getActivity()).refreshAccountInfo(swipeRefreshLayout);
            }
        });


        return v;
    }

}
