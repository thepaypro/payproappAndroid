package app.paypro.payproapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.paypro.payproapp.global.Global;
import app.paypro.payproapp.ui.button.swipe.OnStateChangeListener;
import app.paypro.payproapp.ui.button.swipe.SwipeButton;


/**
 * Created by rogerbaiget on 30/11/17.
 */

public class SendMoneySendFragment extends Fragment {

    SwipeButton swipeButton;
    TextView readyToSendText;
    TextView toText1;
    TextView toText2;
    TextView toText3;
    TextView toText4;


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
        readyToSendText = getActivity().findViewById(R.id.ready_to_send_text);
        toText1 = getActivity().findViewById(R.id.to_text_1);
        toText2 = getActivity().findViewById(R.id.to_text_2);
        toText3 = getActivity().findViewById(R.id.to_text_3);
        toText4 = getActivity().findViewById(R.id.to_text_4);

        SendMoney sendMoney = Global.getSendMoney();

        readyToSendText.setText("bits " + sendMoney.getAmount().toString());

        if(sendMoney.getLabel() == null){
            toText1.setText(R.string.destinatary_not_available);
        }else{
            toText1.setText(sendMoney.getLabel());
        }

        if(sendMoney.getMessage() == null){
            toText2.setText("");
        }else{
            toText2.setText(sendMoney.getMessage());
        }


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
