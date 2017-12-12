package app.paypro.payproapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.paypro.payproapp.ui.button.swipe.OnStateChangeListener;
import app.paypro.payproapp.ui.button.swipe.SwipeButton;


/**
 * Created by rogerbaiget on 30/11/17.
 */

public class SendMoneySendFragment extends Fragment {

    SwipeButton swipeButton;
    Text


    public static SendMoneySendFragment newInstance() {
        SendMoneySendFragment fragment = new SendMoneySendFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sendmoney_send_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeButton = getActivity().findViewById(R.id.swipe_btn);
        swipeButton.setEventListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                Intent intent = new Intent(getActivity(), SendMoneyConfirmActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

        });

    }
}
