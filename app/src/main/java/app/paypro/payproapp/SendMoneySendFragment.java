package app.paypro.payproapp;

import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ImageButton;
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
import app.paypro.payproapp.utils.PPAlertDialog;
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
    private Boolean activityBlocked = false;

    DialogInterface.OnClickListener defaultDialogInterface = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    };


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

        final SendMoney sendMoney = Global.getSendMoney();

        readyToSendText.setText("bits " + sendMoney.getFormatAmount());

        if(sendMoney.getLabel() == null){
            toText1.setText(R.string.destinatary_not_available);
        }else{
            toText1.setText(sendMoney.getLabel());
        }

        if(sendMoney.getMessage() == null){
            toText2.setText(R.string.default_transaction_msg);
            sendMoney.setMessage(getResources().getString(R.string.default_transaction_msg));
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
                                swipeButton.restartSwipeButton();
                                switch (object.getString("error_msg")){
                                    case "insufficient_funds":
                                        DialogInterface.OnClickListener dialogInterfaceIF = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                getFragmentManager().popBackStack();
                                            }
                                        };
                                        PPAlertDialog.getAlertDialogBuilder(getContext(),"insufficient_funds",dialogInterfaceIF,dialogInterfaceIF).show();
                                        break;
                                    case "connection_error":
                                        PPAlertDialog.getAlertDialogBuilder(getContext(),"connection_error",defaultDialogInterface,defaultDialogInterface).show();
                                        break;
                                    default:
                                        PPAlertDialog.getAlertDialogBuilder(getContext(),"",defaultDialogInterface,defaultDialogInterface).show();

                                }
                            }else{
                                hideActivityIndicator();
                                swipeButton.restartSwipeButton();
                                PPAlertDialog.getAlertDialogBuilder(getContext(),"",defaultDialogInterface,defaultDialogInterface).show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView toolbarTitle = getActivity().findViewById(R.id.app_toolbar_title);
        toolbarTitle.setText(getResources().getString(R.string.title_send));

        TextView toolbar_back_button_text = getActivity().findViewById(R.id.app_toolbar_back_button_text);
        toolbar_back_button_text.setText(getResources().getString(R.string.title_amount));
        toolbar_back_button_text.setVisibility(View.VISIBLE);

        ImageButton toolbar_back_button_image = getActivity().findViewById(R.id.app_toolbar_back_button_image);
        toolbar_back_button_image.setVisibility(View.VISIBLE);

        Button confirmButton = getActivity().findViewById(R.id.app_toolbar_confirm_button);
        confirmButton.setVisibility(View.GONE);

        toolbar_back_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!activityBlocked){
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    public void showActivityIndicator(){
        progressBarLayout.setVisibility(View.VISIBLE);
        swipeButton.disable();
        activityBlocked = true;
        ((TabActivity)getActivity()).dissableBottomNavigationView();
    }

    public void hideActivityIndicator(){
        progressBarLayout.setVisibility(View.INVISIBLE);
        swipeButton.enable();
        activityBlocked = false;
        ((TabActivity)getActivity()).enableBottomNavigationView();
    }
}
