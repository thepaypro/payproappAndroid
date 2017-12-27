package app.paypro.payproapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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

    private ConstraintLayout activityMain;
    private Fragment navigationAccount;
    private BottomNavigationView navigation;
    private Boolean navigationEnabled = true;
    private AsyncTask<Void,Void,Void> saveContactsAsyncTask;

    public void setSaveContactsAsyncTask(AsyncTask<Void, Void, Void> saveContactsAsyncTask) {
        this.saveContactsAsyncTask = saveContactsAsyncTask;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(navigationEnabled) {
                findViewById(R.id.search_view).setVisibility(View.GONE);
                findViewById(R.id.app_toolbar_layout).setVisibility(View.VISIBLE);
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
                    if(!(selectedFragment instanceof ContactsFragment) && saveContactsAsyncTask != null){
                        saveContactsAsyncTask.cancel(true);
                    }
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
    public void onAttachFragment(Fragment fragment){
        if (!(fragment instanceof ContactsFragment)){
            findViewById(R.id.search_view).setVisibility(View.GONE);
            findViewById(R.id.app_toolbar_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.app_toolbar_search_button).setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (f instanceof ContactsFragment){
            if(!((ContactsFragment) f).onBackPressed()){
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }
    }

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
                            PPSnackbar.getSnackbar(activityMain,getApplicationContext(),object.getString("error_msg")).show();
                        } else {
                            PPSnackbar.getSnackbar(activityMain,getApplicationContext(),"").show();
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
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showVirtualKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.showSoftInput(v,InputMethodManager.SHOW_IMPLICIT);
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
