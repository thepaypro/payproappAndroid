package app.paypro.payproapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.support.BottomNavigationViewHelper;


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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationViewHelper.disableShiftMode(navigation);

        navigation.setSelectedItemId(R.id.navigation_account);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String optionMenuLoad = extras.getString("optionMenuLoad");
            
            switch (optionMenuLoad)
            {
                case "settings":
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, SettingsFragment.newInstance());
                    transaction.commit();

                    TabLayout tabBar = findViewById(R.id.tabs);
                    tabBar.getTabAt(R.id.navigation_settings).select();
//                    TabLayout.Tab tab = tabBar.getTabAt(R.id.navigation_settings);
//                    tab.select();
                    break;
            }
        }
    }

}
