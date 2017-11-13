package app.paypro.payproapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String alpha2Code = extras.getString("alpha2Code");
            String callingCodes = extras.getString("callingCodes");

            TextView alpha2CodeView = this.findViewById(R.id.alpha2CodeView);
            alpha2CodeView.setText(alpha2Code);

            TextView callingCodesView = this.findViewById(R.id.callingCodesView);
            callingCodesView.setText("+"+callingCodes);
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

    public void launchListActivity(View v) {
        startActivity(new Intent(PhoneNumberActivity.this, PhoneNumberListActivity.class));
        finish();
    }

}
