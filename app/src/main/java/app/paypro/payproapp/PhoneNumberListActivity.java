package app.paypro.payproapp;

/**
 * Created by rogerbaiget on 9/11/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import java.io.InputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.support.v7.app.AppCompatActivity;
import java.util.Collections;
import java.util.Comparator;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class PhoneNumberListActivity extends AppCompatActivity{
    private RecyclerView mRecyclerView;
    private CountriesListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String phone_number = "";
    private ImageButton searchButton;
    private ConstraintLayout searchView;
    private EditText searchText;
    private ImageButton searchCloseButton;
    private ImageButton searchBackButton;
    private RelativeLayout appToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_list);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        appToolbarLayout = findViewById(R.id.app_toolbar_layout);
        searchButton = findViewById(R.id.app_toolbar_search_button);
        searchView = findViewById(R.id.search_view);
        searchText = findViewById(R.id.search_text);
        searchCloseButton = findViewById(R.id.search_close_button);
        searchBackButton = findViewById(R.id.search_back_button);

        ImageButton toolbar_back_button_image = findViewById(R.id.app_toolbar_back_button_image);
        toolbar_back_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showToolbarSearch();
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mAdapter != null){
                    mAdapter.getFilter().filter(charSequence);
                }
                if(charSequence.length() > 0){
                    searchCloseButton.setVisibility(View.VISIBLE);
                }else{
                    searchCloseButton.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                hideToolbarSearch();
            }
        });

        searchCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText.setText("");
            }
        });


        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(PhoneNumberListActivity.this, PhoneNumberActivity.class);
                Country country = (Country) v.getTag();
                intent.putExtra("alpha2Code", (String) country.getAlpha2Code());
                intent.putExtra("callingCodes", (String) country.getCallingCodes());
                intent.putExtra("phone_number", phone_number);
                startActivity(intent);
                finish();
            }
        });

        ArrayList<Country> myDataset = loadCountriesJSON();

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CountriesListAdapter(sortList(myDataset));
        mRecyclerView.setAdapter(mAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phone_number = extras.getString("phone_number");
        }
    }

    @Override
    public void onBackPressed() {
        if(searchView.getVisibility() == View.VISIBLE) {
            hideToolbarSearch();
        }else{
            super.onBackPressed();
        }
    }

    ArrayList<Country> sortList(ArrayList<Country> list) {
        Collections.sort(list, new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                return country1.getName().compareTo(country2.getName());
            }
        });
        return list;
    }

    public ArrayList<Country> loadCountriesJSON() {
        ArrayList<Country> listdata = new ArrayList<>();
        try {
            InputStream is = getResources().openRawResource(R.raw.countries);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JSONArray jArray = new JSONArray(new String(buffer, "UTF-8"));
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    JSONObject jo_inside = (JSONObject) jArray.get(i);
                    Country country = new Country(jo_inside.getString("name"), jo_inside.getString("callingCodes"),jo_inside.getString("alpha2Code"));
                    listdata.add(country);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listdata;
    }

    public void showToolbarSearch(){
        appToolbarLayout.setVisibility(View.GONE);
        searchView.setVisibility(View.VISIBLE);
        searchView.bringToFront();
        searchText.requestFocus();
        showVirtualKeyboard(searchText);
    }

    public void hideToolbarSearch() {
        hideVirtualKeyboard();
        searchText.setText("");
        searchView.setVisibility(View.GONE);
        appToolbarLayout.setVisibility(View.VISIBLE);
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
}
