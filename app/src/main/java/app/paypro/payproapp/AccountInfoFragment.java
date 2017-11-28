package app.paypro.payproapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import app.paypro.payproapp.asynctask.db.user.GetAccountAsyncTask;
import app.paypro.payproapp.db.entity.Account;

/**
 * Created by rogerbaiget on 15/11/17.
 */

public class AccountInfoFragment extends Fragment implements AccountFragmentsInterface{

    public TextView addressTextView;
//    public Account account;

    public static AccountInfoFragment newInstance() {
        AccountInfoFragment fragment = new AccountInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.account_info_tab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayout qrCodeClickable = getView().findViewById(R.id.qr_code_clickable);

        qrCodeClickable.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Bundle arguments = new Bundle();
                arguments.putString("addr",addressTextView.getText().toString());

                ShowQRCodeFragment qrCodeFragment = ShowQRCodeFragment.newInstance();
                qrCodeFragment.setArguments(arguments);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, qrCodeFragment).commit();
            }

        });

        try {
            this.onRefreshInfo(new GetAccountAsyncTask(getContext()).execute().get()[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefreshInfo(Account account) {
        addressTextView = getView().findViewById(R.id.address_text);
        addressTextView.setText(account.getAddress());
    }
}