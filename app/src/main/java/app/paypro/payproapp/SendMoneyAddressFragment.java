package app.paypro.payproapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.paypro.payproapp.global.Global;

/**
 * Created by rogerbaiget on 30/11/17.
 */

public class SendMoneyAddressFragment extends Fragment {

    private EditText addrEditText;
    private Button confirmButton;

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

        addrEditText.requestFocus();
        addrEditText.setFocusable(true);

        ((TabActivity) getActivity()).showVirtualKeyboard(addrEditText);

        addrEditText.addTextChangedListener(filterTextWatcher);

    }

    @Override
    public void onResume() {
        super.onResume();

        TextView toolbarTitle = getActivity().findViewById(R.id.app_toolbar_title);
        toolbarTitle.setText(getResources().getString(R.string.bitcoin_address_title));

        TextView toolbar_back_button_text = getActivity().findViewById(R.id.app_toolbar_back_button_text);
        toolbar_back_button_text.setText(getResources().getString(R.string.contacts_title));
        toolbar_back_button_text.setVisibility(View.VISIBLE);

        ImageButton toolbar_back_button_image = getActivity().findViewById(R.id.app_toolbar_back_button_image);
        toolbar_back_button_image.setVisibility(View.VISIBLE);

        confirmButton = getActivity().findViewById(R.id.app_toolbar_confirm_button);
        confirmButton.setText(getResources().getString(R.string.next));
        if(!(addrEditText.getText().toString().length() > 0)) {
            confirmButton.setVisibility(View.GONE);
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
                String addr = addrEditText.getText().toString();
                if(addr.matches("^[13][a-km-zA-HJ-NP-Z0-9]{26,33}$")){
                    Global.getSendMoney().setAddress(addr);

                    SendMoneyAmountFragment myfragment = new SendMoneyAmountFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("origin",getResources().getString(R.string.bitcoin_address_title));
                    myfragment.setArguments(bundle);
                    FragmentManager fragmentManager = ((TabActivity)getContext()).getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    transaction.replace(R.id.frame_layout, myfragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else{
                    TextInputLayout til = (TextInputLayout) getActivity().findViewById(R.id.addr_input_layout);
                    til.setError("Invalid bitcoin address");
                }
            }
        });
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
        confirmButton.setVisibility(View.VISIBLE);
    }

    public void dissableNextButton(){
        confirmButton.setVisibility(View.GONE);
    }
}
