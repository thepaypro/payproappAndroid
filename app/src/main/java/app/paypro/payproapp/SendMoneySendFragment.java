package app.paypro.payproapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import app.paypro.payproapp.global.Global;
import app.paypro.payproapp.http.ResponseListener;
import app.paypro.payproapp.transaction.Transaction;
import app.paypro.payproapp.ui.button.swipe.OnStateChangeListener;
import app.paypro.payproapp.ui.button.swipe.SwipeButton;
import app.paypro.payproapp.utils.PPSnackbar;


/**
 * Created by rogerbaiget on 30/11/17.
 */

public class SendMoneySendFragment extends Fragment {

    private ConstraintLayout mainView;
    private SwipeButton swipeButton;
    private TextView readyToSendText;
    private TextView toText1;
    private TextView toText2;
    private TextView toText3;
    private TextView toText4;
    private LinearLayout progressBarLayout;
    private Toolbar toolbar;


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

        mainView = getActivity().findViewById(R.id.main_view);
        swipeButton = getActivity().findViewById(R.id.swipe_btn);
        readyToSendText = getActivity().findViewById(R.id.ready_to_send_text);
        toText1 = getActivity().findViewById(R.id.to_text_1);
        toText2 = getActivity().findViewById(R.id.to_text_2);
        toText3 = getActivity().findViewById(R.id.to_text_3);
        toText4 = getActivity().findViewById(R.id.to_text_4);
        progressBarLayout = getActivity().findViewById(R.id.activity_indicator);
        toolbar = getActivity().findViewById(R.id.toolbar);

        ((TabActivity)getActivity()).setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TabActivity) getActivity()).hideVirtualKeyboard();
                getActivity().onBackPressed();
            }
        });

        // Hide the virtual keyboard
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        final SendMoney sendMoney = Global.getSendMoney();

        readyToSendText.setText("bits " + sendMoney.getFormatAmount());

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

                showActivityIndicator();

                HashMap<String,Object> transactionHashMap = new HashMap<>();

                transactionHashMap.put("subject", sendMoney.getMessage());
                transactionHashMap.put("amount", sendMoney.getAmount());

                if (sendMoney.getUserId() != null){
                    transactionHashMap.put("beneficiaryUserID",sendMoney.getUserId());
                }else{
                    transactionHashMap.put("beneficiary",sendMoney.getAddress());
                }

                JSONObject parameters = new JSONObject(transactionHashMap);

                try {
                    Transaction.create(getContext(),parameters,new ResponseListener<JSONObject>(){
                        @Override
                        public void getResult(JSONObject object) throws JSONException {
                            if(object.getBoolean("status")){
                                hideActivityIndicator();
                                Intent intent = new Intent(getActivity(), SendMoneyConfirmActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }else if (!object.getBoolean("status") && object.has("error_msg")){
                                hideActivityIndicator();
                                PPSnackbar.getSnackbar(mainView,object.getString("error_msg")).show();
                                swipeButton.restartSwipeButton();
                            }else{
                                hideActivityIndicator();
                                PPSnackbar.getSnackbar(mainView,"").show();
                                swipeButton.restartSwipeButton();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public void showActivityIndicator(){
        progressBarLayout.setVisibility(View.VISIBLE);
        swipeButton.disable();
    }

    public void hideActivityIndicator(){
        progressBarLayout.setVisibility(View.INVISIBLE);
        swipeButton.enable();
    }
}
