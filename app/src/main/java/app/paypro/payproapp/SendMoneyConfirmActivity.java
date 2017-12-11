package app.paypro.payproapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by rogerbaiget on 11/12/17.
 */

public class SendMoneyConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmoney_confirm);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SendMoneyConfirmActivity.this, TabActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }


}
