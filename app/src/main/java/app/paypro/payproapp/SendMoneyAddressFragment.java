package app.paypro.payproapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import app.paypro.payproapp.global.Global;

/**
 * Created by rogerbaiget on 30/11/17.
 */

public class SendMoneyAddressFragment extends Fragment {

    private EditText addrEditText;
    private Button nextButton;

    public static SendMoneyAddressFragment newInstance() {
        SendMoneyAddressFragment fragment = new SendMoneyAddressFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.sendmoney_address_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addrEditText = getActivity().findViewById(R.id.addr_edit_text);
        nextButton = getActivity().findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addr = addrEditText.getText().toString();
                if(addr.matches("\"^[13][a-km-zA-HJ-NP-Z0-9]{26,33}$\"")){
                    Global.getSendMoney().setAddress(addr);

                    SendMoneyAmountFragment myfragment = new SendMoneyAmountFragment();
                    FragmentManager fragmentManager = ((TabActivity)getContext()).getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    transaction.add(R.id.frame_layout, myfragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else{
                    TextInputLayout til = (TextInputLayout) getActivity().findViewById(R.id.addr_input_layout);
                    til.setError("Invalid bitcoin address");
                }
            }
        });

//        addrEditText.requestFocus();
//        addrEditText.setFocusable(true);

//        InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        addrEditText.addTextChangedListener(filterTextWatcher);

    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            if(addrEditText.getText().toString().length() > 0){
                enableNextButton();
            }else{
                dissableNextButton();
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    };

    public void enableNextButton(){
        nextButton.setEnabled(true);
        nextButton.setTextColor(Color.WHITE);
    }

    public void dissableNextButton(){
        nextButton.setEnabled(false);
        nextButton.setTextColor(getActivity().getResources().getColor(R.color.inactive_grey));
    }
}
