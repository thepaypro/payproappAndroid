package app.paypro.payproapp;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.android.support.BottomNavigationViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

import app.paypro.payproapp.account.Account;
import app.paypro.payproapp.asynctask.db.user.GetAccountAsyncTask;
import app.paypro.payproapp.http.ResponseListener;


public class TabActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_support:
                    selectedFragment = SupportFragment.newInstance();
                    break;
                case R.id.navigation_scan:
                    selectedFragment = ScanFragment.newInstance();
                    break;
                case R.id.navigation_send:
                    selectedFragment = SendFragment.newInstance();
                    break;
                case R.id.navigation_account:
                    selectedFragment = AccountFragment.newInstance();
                    break;
                case R.id.navigation_settings:
                    selectedFragment = SettingsFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        try {
            Account.info(this, new ResponseListener<JSONObject>() {
                @Override
                public void getResult(JSONObject object) throws JSONException {
                    try {
                        if (object.getString("status").equals("true")) {
                            Log.i("ACCOUNT-INFO", object.getString("info"));
                        } else {
                            Log.i("ACCOUNT-INFO", "nadaaaaa");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            Log.i("aaaavbvbvbbb", "aaaaaaaa");
            e.printStackTrace();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationViewHelper.disableShiftMode(navigation);

        navigation.setSelectedItemId(R.id.navigation_account);
    }

}
