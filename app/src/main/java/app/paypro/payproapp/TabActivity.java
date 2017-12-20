package app.paypro.payproapp;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.android.support.BottomNavigationViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import app.paypro.payproapp.account.Account;
import app.paypro.payproapp.asynctask.db.user.GetAccountAsyncTask;
import app.paypro.payproapp.http.ResponseListener;
import app.paypro.payproapp.utils.PPSnackbar;
import io.intercom.android.sdk.Intercom;


public class TabActivity extends AppCompatActivity {

    RelativeLayout activityMain;
    Fragment navigationAccount;
    BottomNavigationView navigation;
    Boolean navigationEnabled = true;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(navigationEnabled) {
                Fragment selectedFragment = null;
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                Boolean selectedActualFragment = false;
                switch (item.getItemId()) {
                    case R.id.navigation_support:
                        Intercom.client().displayMessenger();
                        return false;
                    case R.id.navigation_scan:
                        if (f instanceof ScanFragment) {
                            selectedActualFragment = true;
                        }
                        selectedFragment = ScanFragment.newInstance();
                        break;
                    case R.id.navigation_send:
                        if (f instanceof ContactsFragment) {
                            selectedActualFragment = true;
                        }
                        selectedFragment = ContactsFragment.newInstance();
                        break;
                    case R.id.navigation_account:
                        if (f instanceof AccountFragment) {
                            selectedActualFragment = true;
                        }
                        selectedFragment = AccountFragment.newInstance();
                        navigationAccount = selectedFragment;
                        break;
                    case R.id.navigation_settings:
                        if (f instanceof SettingsFragment) {
                            selectedActualFragment = true;
                        }
                        selectedFragment = SettingsFragment.newInstance();
                        break;
                }

                if (!selectedActualFragment) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                    transaction.replace(R.id.frame_layout, selectedFragment);
                    hideVirtualKeyboard();
                    transaction.commit();
                }
                return true;
            }else{
                return false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        activityMain = findViewById(R.id.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
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
    public void hideVirtualKeyboard(){
        // Hide the virtual keyboard
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
    }

    public void dissableBottomNavigationView(){
        navigationEnabled = false;
    }

    public void enableBottomNavigationView(){
        navigationEnabled = true;
    }
}
