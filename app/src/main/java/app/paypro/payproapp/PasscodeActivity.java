package app.paypro.payproapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.paypro.payproapp.http.ResponseListener;
import app.paypro.payproapp.user.User;
import app.paypro.payproapp.utils.PPAlertDialog;
import app.paypro.payproapp.utils.PPSnackbar;
import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

/**
 * Created by rogerbaiget on 13/11/17.
 */

public class PasscodeActivity extends AppCompatActivity{

    private EditText editText;
    private ImageView firstImageView;
    private ImageView secondImageView;
    private ImageView thirdImageView;
    private ImageView fourImageView;
    private ImageView fiveImageView;
    private ImageView sixImageView;
    private ProgressBar progressBar;
    private String passcode_state;
    private String username;
    private String sms_code;
    private String first_passcode = "";
    private String old_passcode = "";
    private LinearLayout mainView;

    DialogInterface.OnClickListener dialogInterfaceDefaultCancel = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Intent intent = new Intent(PasscodeActivity.this, PhoneNumberActivity.class);
            startActivity(intent);
            finish();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        progressBar = findViewById(R.id.progress_bar);
        editText= findViewById(R.id.editText);
        firstImageView= findViewById(R.id.circle1);
        secondImageView= findViewById(R.id.circle2);
        thirdImageView= findViewById(R.id.circle3);
        fourImageView= findViewById(R.id.circle4);
        fiveImageView= findViewById(R.id.circle5);
        sixImageView= findViewById(R.id.circle6);
        mainView = findViewById(R.id.main_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            passcode_state = extras.getString("passcode_state");
            username = extras.getString("username");
            editText.requestFocus();

            TextView titleTextView = this.findViewById(R.id.titleTextView);
            TextView descTextView = this.findViewById(R.id.descTextView);
            Toolbar toolbar = this.findViewById(R.id.toolbar);

            switch (passcode_state){
                case "create":
                    sms_code = extras.getString("sms_code");
                    titleTextView.setText(R.string.enter_passcode_create);
                    descTextView.setText(R.string.enter_passcode_desc_create);
                    toolbar.setTitle(R.string.title_passcode_create);
                    break;
                case "login":
                    titleTextView.setText(R.string.enter_passcode_login);
                    descTextView.setText(R.string.enter_passcode_desc_login);
                    toolbar.setTitle(R.string.title_passcode_login);
                    break;
                case "old":
                    titleTextView.setText(R.string.enter_passcode_old);
                    descTextView.setText(R.string.enter_passcode_desc_old);
                    toolbar.setTitle(R.string.title_passcode_create);
                    break;
                case "confirm":
                    first_passcode = extras.getString("first_passcode");
                    sms_code = extras.getString("sms_code");
                    titleTextView.setText(R.string.enter_passcode_confirm);
                    descTextView.setText(R.string.enter_passcode_desc_confirm);
                    toolbar.setTitle(R.string.title_passcode_confirm);
                    break;
                case "changePasscodeNew":
                    old_passcode = extras.getString("old_passcode");
                    titleTextView.setText(R.string.enter_passcode_new);
                    descTextView.setText(R.string.enter_passcode_desc_new);
                    toolbar.setTitle(R.string.title_passcode_new);
                    break;
                case "changePasscodeConfirm":
                    old_passcode = extras.getString("old_passcode");
                    first_passcode = extras.getString("first_passcode");
                    titleTextView.setText(R.string.enter_passcode_confirm);
                    descTextView.setText(R.string.enter_passcode_desc_confirm);
                    toolbar.setTitle(R.string.title_passcode_confirm);
                    break;
            }
        }

        editText.addTextChangedListener(filterTextWatcher);

        editText.requestFocus();
        editText.setFocusableInTouchMode(true);

    }

    public void shake(){
        firstImageView.setImageResource(R.drawable.empty_circle);
        secondImageView.setImageResource(R.drawable.empty_circle);
        thirdImageView.setImageResource(R.drawable.empty_circle);
        fourImageView.setImageResource(R.drawable.empty_circle);
        fiveImageView.setImageResource(R.drawable.empty_circle);
        sixImageView.setImageResource(R.drawable.empty_circle);
        AnimationSet shakeAnim = new AnimationSet(true);
        shakeAnim.addAnimation(AnimationUtils.loadAnimation(getApplicationContext(),  R.anim.solid_circle_translation));
        LinearLayout circlelinearLayout= findViewById(R.id.circlelinearLayout);
        circlelinearLayout.setAnimation(shakeAnim);
        circlelinearLayout.startAnimation(shakeAnim);

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            Integer editedTextLengt = editText.length();
            String editTextString = editText.getText().toString();

            switch (editedTextLengt) {
                case 0:
                    firstImageView.setImageResource(R.drawable.empty_circle);
                    secondImageView.setImageResource(R.drawable.empty_circle);
                    thirdImageView.setImageResource(R.drawable.empty_circle);
                    fourImageView.setImageResource(R.drawable.empty_circle);
                    fiveImageView.setImageResource(R.drawable.empty_circle);
                    sixImageView.setImageResource(R.drawable.empty_circle);
                    break;
                case 1:
                    firstImageView.setImageResource(R.drawable.solid_circle);
                    secondImageView.setImageResource(R.drawable.empty_circle);
                    thirdImageView.setImageResource(R.drawable.empty_circle);
                    fourImageView.setImageResource(R.drawable.empty_circle);
                    fiveImageView.setImageResource(R.drawable.empty_circle);
                    sixImageView.setImageResource(R.drawable.empty_circle);
                    break;
                case 2:
                    firstImageView.setImageResource(R.drawable.solid_circle);
                    secondImageView.setImageResource(R.drawable.solid_circle);
                    thirdImageView.setImageResource(R.drawable.empty_circle);
                    fourImageView.setImageResource(R.drawable.empty_circle);
                    fiveImageView.setImageResource(R.drawable.empty_circle);
                    sixImageView.setImageResource(R.drawable.empty_circle);
                    break;
                case 3:
                    firstImageView.setImageResource(R.drawable.solid_circle);
                    secondImageView.setImageResource(R.drawable.solid_circle);
                    thirdImageView.setImageResource(R.drawable.solid_circle);
                    fourImageView.setImageResource(R.drawable.empty_circle);
                    fiveImageView.setImageResource(R.drawable.empty_circle);
                    sixImageView.setImageResource(R.drawable.empty_circle);
                    break;
                case 4:
                    firstImageView.setImageResource(R.drawable.solid_circle);
                    secondImageView.setImageResource(R.drawable.solid_circle);
                    thirdImageView.setImageResource(R.drawable.solid_circle);
                    fourImageView.setImageResource(R.drawable.solid_circle);
                    fiveImageView.setImageResource(R.drawable.empty_circle);
                    sixImageView.setImageResource(R.drawable.empty_circle);
                    break;
                case 5:
                    firstImageView.setImageResource(R.drawable.solid_circle);
                    secondImageView.setImageResource(R.drawable.solid_circle);
                    thirdImageView.setImageResource(R.drawable.solid_circle);
                    fourImageView.setImageResource(R.drawable.solid_circle);
                    fiveImageView.setImageResource(R.drawable.solid_circle);
                    sixImageView.setImageResource(R.drawable.empty_circle);
                    break;
                case 6:
                    firstImageView.setImageResource(R.drawable.solid_circle);
                    secondImageView.setImageResource(R.drawable.solid_circle);
                    thirdImageView.setImageResource(R.drawable.solid_circle);
                    fourImageView.setImageResource(R.drawable.solid_circle);
                    fiveImageView.setImageResource(R.drawable.solid_circle);
                    sixImageView.setImageResource(R.drawable.solid_circle);

                    switch (passcode_state){
                        case "create":
                            Intent createIntent = new Intent(PasscodeActivity.this, PasscodeActivity.class);
                            createIntent.putExtra("passcode_state", "confirm");
                            createIntent.putExtra("username", username);
                            createIntent.putExtra("sms_code", sms_code);
                            createIntent.putExtra("first_passcode", editTextString);
                            startActivity(createIntent);
                            finish();
                            break;
                        case "login":
                            JSONObject parameters = new JSONObject();
                            try {
                                parameters.put("_username",username);
                                parameters.put("_password",editTextString);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            disableView();
                            try {
                                User.login(getApplicationContext(), parameters, new ResponseListener<JSONObject>(){
                                    @Override
                                    public void getResult(JSONObject object) {
                                        try {
                                            if(object.getBoolean("status")){
                                                JSONObject version = object.getJSONObject("version");
                                                int lastVersion = version.getInt("lastVersion");
                                                int oldSuppVersion = version.getInt("oldSuppVersion");

                                                if(BuildConfig.VERSION_CODE < oldSuppVersion){
                                                    PPAlertDialog.getAlertDialogBuilder(getBaseContext(), "force_update", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                                            Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse("market://search?q=" + APP_NAME));
//                                                            startActivity(intent);
                                                        }
                                                    }, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            finish();
                                                        }
                                                    }).show();
                                                }else if (BuildConfig.VERSION_CODE < lastVersion){
                                                    PPAlertDialog.getAlertDialogBuilder(getBaseContext(), "optional_update", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                                            Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse("market://search?q=" + APP_NAME));
//                                                            startActivity(intent);
                                                        }
                                                    }, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {}
                                                    }).show();

                                                }else{
                                                    enableView();
                                                    Intercom.initialize(getApplication(), "android_sdk-33727861a67ba62364b150d95d3d8812f5e3584a", "u32hbwiy");
                                                    Registration registration = Registration.create().withUserId(username);
                                                    Intercom.client().registerIdentifiedUser(registration);
                                                    Intent intentLogin = new Intent(PasscodeActivity.this, TabActivity.class);
                                                    startActivity(intentLogin);
                                                    finish();
                                                }
                                            }else if (!object.getBoolean("status") && object.has("error_msg")){
                                                enableView();
                                                PPSnackbar.getSnackbar(mainView,getApplicationContext(),object.getString("error_msg")).show();
                                                shake();
                                                editText.setText("");
                                            }else{
                                                enableView();
                                                shake();
                                                editText.setText("");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "old":
                            enableView();
                            Intent intentOld = new Intent(PasscodeActivity.this, PasscodeActivity.class);
                            intentOld.putExtra("passcode_state", "changePasscodeNew");
                            intentOld.putExtra("old_passcode", editTextString);
                            startActivity(intentOld);
                            finish();
                            break;
                        case "confirm":
                            JSONObject registerParameters = new JSONObject();
                            disableView();
                            try {
                                JSONObject userInfo = new JSONObject();
                                userInfo.put("username", username);

                                JSONObject password = new JSONObject();
                                password.put("first", first_passcode);
                                password.put("second", editTextString);

                                userInfo.put("plainPassword", password);
                                userInfo.put("mobileVerificationCode", sms_code);

                                registerParameters.put("app_user_registration", userInfo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                User.register(getApplicationContext(), registerParameters, new ResponseListener<JSONObject>() {
                                    @Override
                                    public void getResult(JSONObject object) throws JSONException {
                                        try {
                                            if (object.getBoolean("status")) {
                                                Intent intentComfirm = new Intent(PasscodeActivity.this, TabActivity.class);
                                                startActivity(intentComfirm);
                                                finish();
                                            } else if(!object.getBoolean("status") && object.has("error_msg")) {
                                                enableView();
                                                shake();
                                                editText.setText("");
                                                final String errorMsg = object.getString("error_msg");

                                                if(errorMsg.equals("error_invalid_verification_code")) {
                                                    DialogInterface.OnClickListener dialogInterface = new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            Intent intent = new Intent(PasscodeActivity.this, SmsCodeActivity.class);
                                                            intent.putExtra("username", username);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    };
                                                    PPAlertDialog.getAlertDialogBuilder(PasscodeActivity.this,errorMsg,dialogInterface,dialogInterfaceDefaultCancel).show();
                                                }else if(errorMsg.equals("error_passcode_dont_match")) {
                                                    DialogInterface.OnClickListener dialogInterface = new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            Intent intent = new Intent(PasscodeActivity.this, PasscodeActivity.class);
                                                            intent.putExtra("username", username);
                                                            intent.putExtra("sms_code", sms_code);
                                                            intent.putExtra("passcode_state", "create");
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    };
                                                    PPAlertDialog.getAlertDialogBuilder(PasscodeActivity.this, errorMsg, dialogInterface, dialogInterfaceDefaultCancel).show();
                                                }else if(errorMsg.equals("error_username_exist") || errorMsg.equals("error_invalid_phonenumber")) {
                                                    PPAlertDialog.getAlertDialogBuilder(PasscodeActivity.this,errorMsg,dialogInterfaceDefaultCancel,dialogInterfaceDefaultCancel).show();
                                                }else {
                                                    PPAlertDialog.getAlertDialogBuilder(PasscodeActivity.this,errorMsg,dialogInterfaceDefaultCancel,dialogInterfaceDefaultCancel).show();
                                                }
                                            }else{
                                                enableView();
                                                shake();
                                                editText.setText("");
                                                PPAlertDialog.getAlertDialogBuilder(PasscodeActivity.this,"",dialogInterfaceDefaultCancel,dialogInterfaceDefaultCancel).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "changePasscodeNew":
                            enableView();
                            Intent intentNew = new Intent(PasscodeActivity.this, PasscodeActivity.class);
                            intentNew.putExtra("passcode_state", "changePasscodeConfirm");
                            intentNew.putExtra("old_passcode", old_passcode);
                            intentNew.putExtra("first_passcode", editTextString);
                            startActivity(intentNew);
                            finish();
                            break;
                        case "changePasscodeConfirm":
                            disableView();
                            try {
                                JSONObject changePasscodeParameters = new JSONObject();
                                changePasscodeParameters.put("old_password", old_passcode.toString());
                                changePasscodeParameters.put("new_password", first_passcode.toString());
                                changePasscodeParameters.put("confirm_password", editTextString);
                                User.changePasscode(getApplicationContext(), changePasscodeParameters, new ResponseListener<JSONObject>() {
                                    @Override
                                    public void getResult(JSONObject object) throws JSONException {
                                        try {
                                            if (object.getBoolean("status")){
                                                Intent intentComfirm = new Intent(PasscodeActivity.this, TabActivity.class);
                                                intentComfirm.putExtra("optionMenuLoad", "settings");
                                                startActivity(intentComfirm);
                                                finish();
                                            } else {
                                                enableView();
                                                shake();
                                                editText.setText("");
                                                DialogInterface.OnClickListener dialogInterfaceOK = new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent(PasscodeActivity.this, PasscodeActivity.class);
                                                        intent.putExtra("passcode_state", "old");
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                };
                                                PPAlertDialog.getAlertDialogBuilder(PasscodeActivity.this,"error_change_passcode",dialogInterfaceOK,dialogInterfaceOK).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    break;
            }

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    };

    public void disableView(){
        progressBar.setVisibility(LinearLayout.VISIBLE);
        editText.setEnabled(false);
    }

    public void enableView(){
        progressBar.setVisibility(LinearLayout.GONE);
        editText.setEnabled(true);
        editText.requestFocus();
    }
}
