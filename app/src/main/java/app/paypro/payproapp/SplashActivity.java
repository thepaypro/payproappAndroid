package app.paypro.payproapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import app.paypro.payproapp.http.ResponseListener;
import app.paypro.payproapp.user.User;
import app.paypro.payproapp.utils.PPAlertDialog;

/**
 * Created by rogerbaiget on 14/2/18.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=" + "PayPro"));
            if (intent.resolveActivity(getPackageManager()) != null) {
                User.checkVersion(getApplicationContext(), new ResponseListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject object) throws JSONException {
                        if (object.has("need_update")) {
                            if (object.getBoolean("need_update")) {
                                PPAlertDialog.getAlertDialogBuilder(SplashActivity.this, "force_update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(intent);
                                        finish();
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                }).show();
                            } else {
                                startWellcomeActivity();
                            }
                        }
                    }
                });
            }else{
                startWellcomeActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void startWellcomeActivity() {
        Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

}
