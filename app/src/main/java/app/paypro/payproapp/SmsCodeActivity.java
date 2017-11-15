package app.paypro.payproapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Created by rogerbaiget on 13/11/17.
 */

public class SmsCodeActivity extends AppCompatActivity{

    public EditText editText;
    public TextView firstTextView;
    public TextView secondTextView;
    public TextView thirdTextView;
    public TextView fourTextView;
    public String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_code);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editText= findViewById(R.id.editText);
        firstTextView= findViewById(R.id.viewText1);
        secondTextView= findViewById(R.id.viewText2);
        thirdTextView= findViewById(R.id.viewText3);
        fourTextView= findViewById(R.id.viewText4);
        editText.requestFocus();

        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        editText.addTextChangedListener(filterTextWatcher);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

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

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            Integer editedTextLengt = editText.length();
            String editTextString = editText.getText().toString();

            switch (editedTextLengt) {
                case 0:
                    firstTextView.setText("");
                    secondTextView.setText("");
                    thirdTextView.setText("");
                    fourTextView.setText("");
                    break;
                case 1:
                    firstTextView.setText(Character.toString(editTextString.charAt(0)));
                    secondTextView.setText("");
                    thirdTextView.setText("");
                    fourTextView.setText("");
                    break;
                case 2:
                    firstTextView.setText(Character.toString(editTextString.charAt(0)));
                    secondTextView.setText(Character.toString(editTextString.charAt(1)));
                    thirdTextView.setText("");
                    fourTextView.setText("");
                    break;
                case 3:
                    firstTextView.setText(Character.toString(editTextString.charAt(0)));
                    secondTextView.setText(Character.toString(editTextString.charAt(1)));
                    thirdTextView.setText(Character.toString(editTextString.charAt(2)));
                    fourTextView.setText("");
                    break;
                case 4:
                    firstTextView.setText(Character.toString(editTextString.charAt(0)));
                    secondTextView.setText(Character.toString(editTextString.charAt(1)));
                    thirdTextView.setText(Character.toString(editTextString.charAt(2)));
                    fourTextView.setText(Character.toString(editTextString.charAt(3)));
                    Intent intent = new Intent(SmsCodeActivity.this, PasscodeActivity.class);
                    intent.putExtra("passcode_state", "create");
                    intent.putExtra("sms_code", editTextString );
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                    break;
            }

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    };

}
