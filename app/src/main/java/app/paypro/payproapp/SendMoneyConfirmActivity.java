package app.paypro.payproapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import app.paypro.payproapp.global.Global;

/**
 * Created by rogerbaiget on 11/12/17.
 */

public class SendMoneyConfirmActivity extends AppCompatActivity {

    TextView amount;
    TextView to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmoney_confirm);

        SendMoney sendMoney = Global.getSendMoney();

        amount = findViewById(R.id.amount);
        to = findViewById(R.id.to);

        amount.setText("bits " + sendMoney.getAmount());
        to.setText("sent to " + sendMoney.getLabel());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Global.resetSendMoney();
                Intent intent = new Intent(SendMoneyConfirmActivity.this, TabActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }


}
