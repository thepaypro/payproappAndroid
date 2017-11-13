package app.paypro.payproapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;


/**
 * Created by rogerbaiget on 10/11/17.
 */

public class PhoneNumberActivity extends AppCompatActivity {

    String alpha2Code;
    String callingCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        EditText editText= findViewById(R.id.editText);
        editText.addTextChangedListener(filterTextWatcher);
        editText.requestFocus();

        android.support.v7.widget.Toolbar toolbar= findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            alpha2Code = extras.getString("alpha2Code");
            callingCodes = extras.getString("callingCodes");

            TextView alpha2CodeView = this.findViewById(R.id.alpha2CodeView);
            alpha2CodeView.setText(alpha2Code);

            TextView callingCodesView = this.findViewById(R.id.callingCodesView);
            callingCodesView.setText("+"+callingCodes);
        }


        ImageView doneLogo = (ImageView) findViewById(R.id.done_logo);
        doneLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneNumberActivity.this, SmsCodeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void launchListActivity(View v) {
        startActivity(new Intent(PhoneNumberActivity.this, PhoneNumberListActivity.class));
        finish();
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            EditText editText= findViewById(R.id.editText);
            LinearLayout toolbarLayout = findViewById(R.id.toolbar_layout);
            if(editText.getText().toString().length() > 0){
                toolbarLayout.setVisibility(LinearLayout.VISIBLE);
            }else{
                toolbarLayout.setVisibility(LinearLayout.INVISIBLE);
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    };

}
