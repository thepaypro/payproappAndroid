package app.paypro.payproapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import app.paypro.payproapp.utils.LocaleHelper;

/**
 * Created by rogerbaiget on 24/1/18.
 */

public class BaseActivity extends AppCompatActivity {

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
