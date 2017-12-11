package app.paypro.payproapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;

import app.paypro.payproapp.global.Global;

/**
 * Created by rogerbaiget on 29/11/17.
 */

public class SendMoneyAmountFragment extends Fragment {

    private Button nextButton;
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
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.sendmoney_amount_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nextButton = getActivity().findViewById(R.id.next_button);
        amountInput = getActivity().findViewById(R.id.amount_input);
        privateMsgInput = getActivity().findViewById(R.id.private_msg_input);

        nextButton.setEnabled(false);

        amountInput.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!textFormated && !charSequence.toString().equals("")) {
                    String amount = currencyInputFormatting(charSequence.toString());
                    textFormated = true;
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

        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendMoney sendMoney = Global.getSendMoney();
                sendMoney.setAmount(Float.valueOf(amountInput.getText().toString()));
                sendMoney.setMessage(privateMsgInput.getText().toString());
                SendMoneySendFragment myfragment = new SendMoneySendFragment();
                FragmentManager fragmentManager = ((TabActivity)getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.add(R.id.frame_layout, myfragment);
                transaction.addToBackStack(null);
                transaction.commit();
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
        if (amount.substring(amount.length() - 1 ).equals(",")) {
            amount = amount.replace(amount.substring(amount.length()-1),".");
        }

        amount = amount.replace( ",",  "");
        amount = amount.replace("..",  ".");

        Boolean matched = amount.matches( "^\\d+((,[0-9]{0,3})+.|\\.[0-9]{0,2})?");
        Boolean matchedFull = amount.matches("^\\d+(,[0-9]{3})+\\.([0-9]{1,2})?");

        if (!(matchedFull || (matched && !amount.equals("0"))) || amount.length() > 10){
            amount = amount.substring(0,amount.length()-1);
        }

        String amount_integer = amount;
        String amount_decimals = "";
        String amount_decimals_origin = "";
        Integer pos_dot = amount.indexOf(".");

        if (pos_dot != -1) {
            amount_integer = amount.substring(0,pos_dot);
            amount_decimals_origin = amount.substring(pos_dot);
            amount_decimals = amount_decimals_origin;

            if (amount_decimals_origin == ".") {
                amount_decimals = "";
            }
        }

        Integer amount_number = Integer.parseInt(amount_integer);


        NumberFormat format = NumberFormat.getInstance(Locale.UK);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(0);
        String amount_formatted = format.format(amount_number);

        if (!amount_decimals_origin.equals(amount_decimals)) {
            amount_formatted = amount_formatted.concat(amount_decimals_origin);
        } else {
            amount_formatted = amount_formatted.concat(amount_decimals);
        }

        return amount_formatted;
    }

    public void enableNextButton(){
        nextButton.setEnabled(true);
        nextButton.setTextColor(Color.WHITE);
    }

    public void dissableNextButton(){
        nextButton.setEnabled(false);
        nextButton.setTextColor(getActivity().getResources().getColor(R.color.inactive_grey));
    }
}
