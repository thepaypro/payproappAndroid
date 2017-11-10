package app.paypro.payproapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by rogerbaiget on 10/11/17.
 */

public class PhoneNumberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String alpha2Code = extras.getString("alpha2Code");
            String callingCodes = extras.getString("callingCodes");

            TextView alpha2CodeView = this.findViewById(R.id.alpha2CodeView);
            alpha2CodeView.setText(alpha2Code);

            TextView callingCodesView = this.findViewById(R.id.callingCodesView);
            callingCodesView.setText(callingCodes);
        }
    }

    public void launchListActivity(View v) {
        startActivity(new Intent(PhoneNumberActivity.this, PhoneNumberListActivity.class));
        finish();
    }

}
