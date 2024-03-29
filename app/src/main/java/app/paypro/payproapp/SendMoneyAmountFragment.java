package app.paypro.payproapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import app.paypro.payproapp.global.Global;

/**
 * Created by rogerbaiget on 29/11/17.
 */

public class SendMoneyAmountFragment extends Fragment {

    private Button confirmButton;
    private EditText amountInput;
    private EditText privateMsgInput;
    private Boolean textFormated = false;

    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.sendmoney_amount_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        amountInput = getActivity().findViewById(R.id.amount_input);
        privateMsgInput = getActivity().findViewById(R.id.private_msg_input);

        amountInput.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!textFormated && !charSequence.toString().equals("")) {
                    String amount = currencyInputFormatting(charSequence.toString());
                    textFormated = true;
                    Log.d(amount,"amount");
                    Log.d(String.valueOf(amount.length()),"amount.length()");
                    amountInput.setText(amount);
                    amountInput.setSelection(amount.length());
                    if(checkValidAmount(amount)){
                        enableNextButton();
                    }else{
                        dissableNextButton();
                    }

                }else{
                    dissableNextButton();
                    textFormated = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView toolbarTitle = getActivity().findViewById(R.id.app_toolbar_title);
        toolbarTitle.setText(getResources().getString(R.string.amount_title));

        TextView toolbar_back_button_text = getActivity().findViewById(R.id.app_toolbar_back_button_text);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            toolbar_back_button_text.setText(bundle.getString("origin"));
            toolbar_back_button_text.setVisibility(View.VISIBLE);
        }

        ImageButton toolbar_back_button_image = getActivity().findViewById(R.id.app_toolbar_back_button_image);
        toolbar_back_button_image.setVisibility(View.VISIBLE);

        confirmButton = getActivity().findViewById(R.id.app_toolbar_confirm_button);
        confirmButton.setText(getResources().getString(R.string.next));
        if(checkValidAmount(amountInput.getText().toString())){
            enableNextButton();
        }else{
            dissableNextButton();
        }

        toolbar_back_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
                ((TabActivity) getActivity()).hideVirtualKeyboard();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SendMoney sendMoney = Global.getSendMoney();
                try {
                    sendMoney.setAmount(amountInput.getText().toString());
                    if(!privateMsgInput.getText().toString().isEmpty()) {
                        sendMoney.setMessage(privateMsgInput.getText().toString());
                    }
                    SendMoneySendFragment myfragment = new SendMoneySendFragment();
                    FragmentManager fragmentManager = ((TabActivity)getContext()).getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                    transaction.replace(R.id.frame_layout, myfragment);
                    transaction.addToBackStack(null);
                    ((TabActivity) getActivity()).hideVirtualKeyboard();
                    transaction.commit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Boolean checkValidAmount(String amount){

        Boolean matchedA = amount.matches("^\\d+$");
        Boolean matchedB = amount.matches("^\\d+(,[0-9]{3})+$");
        Boolean matchedC = amount.matches("^\\d+\\.[0-9]{1,2}$");
        Boolean matchedFull = amount.matches("^\\d+(,[0-9]{3})+\\.[0-9]{1,2}$");

        return ( matchedA || matchedB || matchedC || matchedFull );
    }

    public String currencyInputFormatting(String amount){
        NumberFormat format = NumberFormat.getInstance(Locale.UK);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(0);

        if (amount.substring(amount.length() - 1 ).equals(",")) {
            amount = amount.replace(amount.substring(amount.length()-1),".");
        }

        amount = amount.replace("..",  ".");

        Integer pos_dot = amount.indexOf(".");

        if (amount.equals("0") || amount.equals(".")) {
            return "";
        }else if(amount.substring(amount.length() -1 ).equals(".")){
            String amountToFormat = amount.substring(0,amount.length()-1);
            amountToFormat = amountToFormat.replace( ",",  "");
            if(amountToFormat.length() >= 7){
                return format.format(Double.parseDouble(amountToFormat));
            }else{
                return format.format(Double.parseDouble(amountToFormat)).concat(".");
            }

        }else if (pos_dot!= -1 && ((amount.length() - pos_dot) > 3)){
            amount = amount.replace( ",",  "");
            return format.format(Double.parseDouble(amount.substring(0, amount.length() - 1)));

        }else if((format.format(Double.parseDouble(amount.replace( ",",  ""))).length() > 10)){
            int selectionStarts = amountInput.getSelectionStart();
            String amountToFormat = amount.substring(0,selectionStarts-1).concat(amount.substring(selectionStarts,amount.length()));
            amountToFormat = amountToFormat.replace( ",",  "");
            if(format.format(Double.parseDouble(amountToFormat)).length() > 10){
                return "";
            }else{
                return format.format(Double.parseDouble(amountToFormat));
            }
        }else{
            amount = amount.replace( ",",  "");
            return format.format(Double.parseDouble(amount));
        }

    }

    public void enableNextButton(){
        confirmButton.setVisibility(View.VISIBLE);
    }

    public void dissableNextButton(){
        confirmButton.setVisibility(View.GONE);
    }
}
