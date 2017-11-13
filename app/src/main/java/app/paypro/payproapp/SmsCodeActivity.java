package app.paypro.payproapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.text.TextWatcher;

/**
 * Created by rogerbaiget on 13/11/17.
 */

public class SmsCodeActivity extends AppCompatActivity{

    public EditText firstText;
    public EditText secondText;
    public EditText thirdText;
    public EditText fourText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_code);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        EditText firstText= findViewById(R.id.editText1);
        EditText secondText= findViewById(R.id.editText2);
        EditText thirdText= findViewById(R.id.editText3);
        EditText fourText= findViewById(R.id.editText4);
        firstText.requestFocus();

        secondText.addTextChangedListener(filterTextWatcher);
        thirdText.addTextChangedListener(filterTextWatcher);
        fourText.addTextChangedListener(filterTextWatcher);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchPasscodeActivity(View v) {
//        startActivity(new Intent(PhoneNumberActivity.this, PhoneNumberListActivity.class));
//        finish();
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            Boolean isfirstTextNull = firstText.getText().toString() == null;
            Boolean issecondTextNull =secondText.getText().toString() == null;
            Boolean isthirdTextNull =thirdText.getText().toString() == null;
            Boolean isfourTextNull =fourText.getText().toString() == null;

            if(!isfirstTextNull){
                if(!issecondTextNull){
                    if(!isthirdTextNull){
                        if(isfourTextNull){
                            fourText.requestFocus();
                        }else{
                            //show nextButton
                        }
                    }else{
                        thirdText.requestFocus();
                    }
                }else{
                    secondText.requestFocus();
                }
            }else{
                firstText.requestFocus();
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    };

}
