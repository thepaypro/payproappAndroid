package app.paypro.payproapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import app.paypro.payproapp.http.ResponseListener;
import app.paypro.payproapp.user.User;
import app.paypro.payproapp.utils.PPSnackbar;


/**
 * Created by rogerbaiget on 10/11/17.
 */

public class PhoneNumberActivity extends AppCompatActivity {

    private String alpha2Code;
    private String callingCodes;
    private String phone_number = "";
    private TextInputEditText editText;
    private LinearLayout countryPrefixView;

    private ConstraintLayout mainView;
    private ProgressBar progressBar;
    private Button nextButton;
    private ImageButton toolbar_back_button_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        mainView = findViewById(R.id.main_view);
        nextButton = findViewById(R.id.app_toolbar_confirm_button);
        progressBar = findViewById(R.id.app_toolbar_progress_bar);
        countryPrefixView = findViewById(R.id.countryPrefixView);

        editText= findViewById(R.id.editText);

        editText.addTextChangedListener(filterTextWatcher);

        toolbar_back_button_image = findViewById(R.id.app_toolbar_back_button_image);
        toolbar_back_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.app_toolbar_confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNextActivity();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            alpha2Code = extras.getString("alpha2Code");
            callingCodes = extras.getString("callingCodes");
            phone_number = extras.getString("phone_number");
            editText.requestFocus();

            TextView alpha2CodeView = this.findViewById(R.id.alpha2CodeView);
            alpha2CodeView.setText(alpha2Code);

            TextView callingCodesView = this.findViewById(R.id.callingCodesView);
            callingCodesView.setText("+"+callingCodes);

            editText.setText(phone_number);

        }else{
            callingCodes = "44";
            TextView callingCodesView = this.findViewById(R.id.callingCodesView);
            callingCodesView.setText("+"+callingCodes);

            alpha2Code = "GB";
            TextView alpha2CodeView = this.findViewById(R.id.alpha2CodeView);
            alpha2CodeView.setText(alpha2Code);
        }

        if(editText.getText().toString().length() > 0){
            nextButton.setVisibility(LinearLayout.VISIBLE);
        }else{
            nextButton.setVisibility(LinearLayout.INVISIBLE);
        }

    }

    public void launchNextActivity(){
        final String phoneNumber = "+"+callingCodes+editText.getText().toString();
        disableView();
        try {
            User.mobileVerificationCode(this, phoneNumber, new ResponseListener<JSONObject>() {
                @Override
                public void getResult(JSONObject object) throws JSONException {
                    try {
                        if (object.getString("status").equals("true") && object.getString("isUser").equals("true")) {
                            Intent intentLogin = new Intent(PhoneNumberActivity.this, PasscodeActivity.class);
                            intentLogin.putExtra("username", phoneNumber);
                            intentLogin.putExtra("passcode_state", "login");
                            startActivity(intentLogin);
                            finish();
                        } else if (object.getString("status").equals("true") && object.getString("isUser").equals("false")){
                            Intent intentSms = new Intent(PhoneNumberActivity.this, SmsCodeActivity.class);
                            intentSms.putExtra("username", phoneNumber);
                            startActivity(intentSms);
                            finish();
                        }else if (!object.getBoolean("status") && object.has("error_msg")){
                            enableView();
                            PPSnackbar.getSnackbar(mainView,getApplicationContext(),object.getString("error_msg")).show();
                        }else {
                            TextInputLayout til = (TextInputLayout) findViewById(R.id.text_input_layout);
                            til.setError("Invalid phone number");
                            enableView();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void disableView(){
        nextButton.setVisibility(LinearLayout.GONE);
        progressBar.setVisibility(LinearLayout.VISIBLE);
        countryPrefixView.setEnabled(false);
        editText.setEnabled(false);
        toolbar_back_button_image.setEnabled(false);
    }

    public void enableView(){
        nextButton.setVisibility(LinearLayout.VISIBLE);
        progressBar.setVisibility(LinearLayout.GONE);
        countryPrefixView.setEnabled(true);
        editText.setEnabled(true);
        toolbar_back_button_image.setEnabled(true);
    }

    public void launchListActivity(View v) {
        Intent intentList = new Intent(PhoneNumberActivity.this, PhoneNumberListActivity.class);
        intentList.putExtra("phone_number", editText.getText().toString());
        startActivity(intentList);
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            EditText editText= findViewById(R.id.editText);
            if(editText.getText().toString().length() > 0){
                nextButton.setVisibility(LinearLayout.VISIBLE);
            }else{
                nextButton.setVisibility(LinearLayout.INVISIBLE);
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    };

    public void hideVirtualKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
