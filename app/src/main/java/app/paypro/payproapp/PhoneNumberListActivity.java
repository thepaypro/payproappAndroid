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

import android.support.v4.app.NavUtils;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.support.v7.app.AppCompatActivity;
import java.util.Collections;
import java.util.Comparator;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;
import android.support.v7.app.ActionBar;



public class PhoneNumberListActivity extends AppCompatActivity{
    private RecyclerView mRecyclerView;
    private CountriesListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_list);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(PhoneNumberListActivity.this, PhoneNumberActivity.class);
                Country country = (Country) v.getTag();
                intent.putExtra("alpha2Code", (String) country.getAlpha2Code());
                intent.putExtra("callingCodes", (String) country.getCallingCodes());
                startActivity(intent);
                finish();
            }
        });

        ArrayList<Country> myDataset = loadCountriesJSON();

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CountriesListAdapter(sortList(myDataset));
        mRecyclerView.setAdapter(mAdapter);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    ArrayList<Country> sortList(ArrayList<Country> list) {
        Collections.sort(list, new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                return country1.getName().compareTo(country2.getName());
            }
        });
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        search(searchView);

        return true;
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

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
