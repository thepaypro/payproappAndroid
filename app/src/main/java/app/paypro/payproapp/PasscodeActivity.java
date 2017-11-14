package app.paypro.payproapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by rogerbaiget on 13/11/17.
 */

public class PasscodeActivity extends AppCompatActivity{

    public EditText editText;
    public ImageView firstImageView;
    public ImageView secondImageView;
    public ImageView thirdImageView;
    public ImageView fourImageView;
    public ImageView fiveImageView;
    public ImageView sixImageView;
    public String passcode_state;
    public String first_passcode = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        editText= findViewById(R.id.editText);
        firstImageView= findViewById(R.id.circle1);
        secondImageView= findViewById(R.id.circle2);
        thirdImageView= findViewById(R.id.circle3);
        fourImageView= findViewById(R.id.circle4);
        fiveImageView= findViewById(R.id.circle5);
        sixImageView= findViewById(R.id.circle6);

        editText.requestFocus();

        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            passcode_state = extras.getString("passcode_state");

            TextView titleTextView = this.findViewById(R.id.titleTextView);
            TextView descTextView = this.findViewById(R.id.descTextView);

            switch (passcode_state){
                case "create":
                    titleTextView.setText(R.string.enter_passcode_create);
                    descTextView.setText(R.string.enter_passcode_desc_create);
                    getSupportActionBar().setTitle(R.string.title_passcode_create);
                    break;
                case "login":
                    titleTextView.setText(R.string.enter_passcode_login);
                    descTextView.setText(R.string.enter_passcode_desc_login);
                    getSupportActionBar().setTitle(R.string.title_passcode_login);
                    break;
                case "old":
                    titleTextView.setText(R.string.enter_passcode_old);
                    descTextView.setText(R.string.enter_passcode_desc_old);
                    getSupportActionBar().setTitle(R.string.title_passcode_create);
                    break;
                case "new":
                    titleTextView.setText(R.string.enter_passcode_new);
                    descTextView.setText(R.string.enter_passcode_desc_new);
                    getSupportActionBar().setTitle(R.string.title_passcode_new);
                    break;
                case "confirm":
                    first_passcode = extras.getString("first_passcode");
                    titleTextView.setText(R.string.enter_passcode_confirm);
                    descTextView.setText(R.string.enter_passcode_desc_confirm);
                    getSupportActionBar().setTitle(R.string.title_passcode_confirm);
                    break;
            }
        }

        editText.addTextChangedListener(filterTextWatcher);
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
        /*shakeAnim.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });*/
        LinearLayout circlelinearLayout= findViewById(R.id.circlelinearLayout);
        circlelinearLayout.setAnimation(shakeAnim);
        circlelinearLayout.startAnimation(shakeAnim);
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
                            createIntent.putExtra("first_passcode", editTextString);
                            startActivity(createIntent);
//                            finish();
                            break;
                        case "login":
//                                Intent intent = new Intent(PasscodeActivity.this, PasscodeActivity.class);
//                                startActivity(intent);
//                                finish();
                            break;
                        case "old":

                            break;
                        case "new":

                            break;
                        case "confirm":
                            if(first_passcode.equals(editTextString)){
                                startActivity(new Intent(PasscodeActivity.this, TabActivity.class));
                                finish();
                            }else{
                                shake();
                                editText.setText("");
                                InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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
}
