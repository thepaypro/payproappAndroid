package app.paypro.payproapp;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.android.support.BottomNavigationViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import app.paypro.payproapp.account.Account;
import app.paypro.payproapp.asynctask.db.user.GetAccountAsyncTask;
import app.paypro.payproapp.http.ResponseListener;
import app.paypro.payproapp.utils.PPSnackbar;


public class TabActivity extends AppCompatActivity {

    RelativeLayout activityMain;
    Fragment navigationAccount;

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
                    navigationAccount = selectedFragment;
                    break;
                case R.id.navigation_settings:
                    selectedFragment = SettingsFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        activityMain = findViewById(R.id.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationViewHelper.disableShiftMode(navigation);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String optionMenuLoad = extras.getString("optionMenuLoad");
            
            switch (optionMenuLoad)
            {
                case "settings":
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                    transaction.add(R.id.frame_layout, SettingsFragment.newInstance());
                    transaction.commit();
                
                    navigation.setSelectedItemId(R.id.navigation_settings);
                    break;
            }
        } else {
            navigation.setSelectedItemId(R.id.navigation_account);
        }
    }

    public void refreshAccountInfo(final SwipeRefreshLayout swipeRefreshLayout){
        try {
            Account.info(this, new ResponseListener<JSONObject>() {
                @Override
                public void getResult(JSONObject object) throws JSONException {
                    swipeRefreshLayout.setRefreshing(false);
                    try {
                        if (object.getString("status").equals("true")) {
                            ((AccountFragment) navigationAccount).onRefreshInfo(new GetAccountAsyncTask(getApplicationContext()).execute().get()[0]);
                        }else if (!object.getBoolean("status") && object.has("error_msg")){
                            PPSnackbar.getSnackbar(activityMain,object.getString("error_msg")).show();
                        } else {
                            PPSnackbar.getSnackbar(activityMain,"").show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
