package app.paypro.payproapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import app.paypro.payproapp.http.ResponseListener;
import app.paypro.payproapp.user.User;


/**
 * Created by rogerbaiget on 10/11/17.
 */

public class PhoneNumberActivity extends AppCompatActivity {

    private String alpha2Code;
    private String callingCodes;
    private String phone_number = "";
    private TextInputEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        editText= findViewById(R.id.editText);
        editText.requestFocus();
        editText.setFocusable(true);

        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        editText.addTextChangedListener(filterTextWatcher);

        android.support.v7.widget.Toolbar toolbar= findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayShowTitleEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            alpha2Code = extras.getString("alpha2Code");
            callingCodes = extras.getString("callingCodes");
            phone_number = extras.getString("phone_number");

            TextView alpha2CodeView = this.findViewById(R.id.alpha2CodeView);
            alpha2CodeView.setText(alpha2Code);

            TextView callingCodesView = this.findViewById(R.id.callingCodesView);
            callingCodesView.setText("+"+callingCodes);

            editText.setText(phone_number);
        }


        LinearLayout toolbarLayout =  findViewById(R.id.toolbar_layout);

        if(editText.getText().toString().length() > 0){
            toolbarLayout.setVisibility(LinearLayout.VISIBLE);
        }else{
            toolbarLayout.setVisibility(LinearLayout.INVISIBLE);
        }

    }

    public void launchSmsCodeActivity(View v){
        final String phoneNumber = "+"+callingCodes+editText.getText().toString();
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
                        }else{
                            TextInputLayout til = (TextInputLayout) findViewById(R.id.text_input_layout);
                            til.setError("Invalid phone number");
                            /*AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                            builder.setMessage(R.string.dialog_message_invalid_phone_number)
                                    .setTitle(R.string.dialog_title_invalid_phone_number)
                                    .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // FIRE ZE MISSILES!
                                        }
                                    })
                                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                        }
                                    });

                            // 3. Get the AlertDialog from create()
                            AlertDialog dialog = builder.create();*/
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

    public void launchListActivity(View v) {
        Intent intentList = new Intent(PhoneNumberActivity.this, PhoneNumberListActivity.class);
        intentList.putExtra("phone_number", editText.getText().toString());
        startActivity(intentList);
//        finish();
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            EditText editText= findViewById(R.id.editText);
            LinearLayout toolbarLayout = findViewById(R.id.toolbar_layout);
            if(editText.getText().toString().length() > 0){
//                getSupportActionBar().setDisplayShowTitleEnabled(false);
                toolbarLayout.setVisibility(LinearLayout.VISIBLE);
            }else{
                toolbarLayout.setVisibility(LinearLayout.INVISIBLE);
//                getSupportActionBar().setDisplayShowTitleEnabled(true);
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    };

}
