package app.paypro.payproapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import app.paypro.payproapp.contacts.Contacts;
import app.paypro.payproapp.http.ResponseListener;

import static android.Manifest.permission.READ_CONTACTS;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;


public class ContactsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final int REQUEST_READ_CONTACTS = 101;

    ArrayList<Contact> contacts = new ArrayList<Contact>();

    private ListView mContactsList;
    private RelativeLayout mainView;
    private ProgressBar progressBar;

    private ContactsAdapter contactsAdapter;

    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_URI,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER
            };

    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.contacts_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContactsList =(ListView) getActivity().findViewById(R.id.contacts_list);
        mainView = getActivity().findViewById(R.id.main_view);
        progressBar = getActivity().findViewById(R.id.progress_bar);

        disableView();
        mayRequestContacts();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.search_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        search(searchView);

    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                contactsAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            loadContacts();
            return true;
        }
        if (checkSelfPermission(getContext(),Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            loadContacts();
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mainView, "Permission to access contacts", Snackbar.LENGTH_INDEFINITE)
                            .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted , Access contacts here or do whatever you need.
                loadContacts();
            }
        }
    }

    public void loadContacts(){

        Uri allContacts = ContactsContract.Contacts.CONTENT_URI;

        //declare our cursor
        Cursor c;

        CursorLoader cursorLoader = new CursorLoader(
                getContext(),
                allContacts,
                PROJECTION,
                null,
                null ,
                null);
        c = cursorLoader.loadInBackground();


        saveContacts(c);

    }

    private void saveContacts(Cursor c) {
        ContentResolver cr = getActivity().getContentResolver();
        final HashMap<String,String> allContactsNumbers = new HashMap<>();
        //---display the contact id and name and phone number----
        if (c.moveToFirst()) {
            do {
                //---get the contact id and name
                String contactID = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                String contactDisplayName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                //---get image number---
                String contactImageUri = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));

                //---get phone number---
                ArrayList<String> contactNumbers = new ArrayList<>();
                int hasPhone = c.getInt(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (hasPhone == 1) {
                    Cursor phoneCursor = getActivity().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " +
                                    contactID, null, null);
                    while (phoneCursor.moveToNext()) {
                        String contactPhone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactNumbers.add(contactPhone);
                        contactPhone = phoneFormat(contactPhone);
                        allContactsNumbers.put(String.valueOf(allContactsNumbers.size()+1),contactPhone);
                    }
                    phoneCursor.close();

                }

                if(contactNumbers.size() > 0){
                    contacts.add(new Contact(contactID, contactDisplayName, contactImageUri, contactNumbers));
                }


            } while (c.moveToNext());
            try {
                JSONObject parameters = new JSONObject(allContactsNumbers);
                Contacts.checkContacts(getContext(),parameters,new ResponseListener<JSONObject>(){
                    @Override
                    public void getResult(JSONObject object) throws JSONException {
                        if(object.has("contacts")){
                            JSONObject checkContactsResponse = object.getJSONObject("contacts");
                            for(int i = 0; i< contacts.size(); i++){
                                ArrayList<String> contactNumbers = contacts.get(i).getNumbers();
                                for(int j = 0; j<contactNumbers.size(); j++){
                                        Iterator iterator = checkContactsResponse.keys();
                                        while(iterator.hasNext()){
                                            String key = (String)iterator.next();
                                            if (key.equals(phoneFormat(contactNumbers.get(j)))){
                                                JSONObject responseNumberObject = checkContactsResponse.getJSONObject(key);
                                                Boolean isUser = responseNumberObject.getBoolean("isUser");
                                                contacts.get(i).setIsUser(isUser);
                                                if(isUser){
                                                    contacts.get(i).setBackFullName(responseNumberObject.getString("fullName"));
                                                }
                                            }
                                        }
                                }
                            }
                        }
                        contactsAdapter = new ContactsAdapter(
                                getContext(), R.layout.contacts_list_item, sortList(contacts));

                        mContactsList.setAdapter(contactsAdapter);
                        enableView();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    ArrayList<Contact> sortList(ArrayList<Contact> list) {
        Collections.sort(list, new Comparator<Contact>() {
            @Override
            public int compare(Contact contact1, Contact contact2) {
                return contact1.getName().compareTo(contact2.getName());
            }
        });
        return list;
    }

    public void disableView(){
        progressBar.setVisibility(LinearLayout.VISIBLE);
        mContactsList.setVisibility(View.GONE);
    }

    public void enableView(){
        progressBar.setVisibility(LinearLayout.GONE);
        mContactsList.setVisibility(View.VISIBLE);
    }

    public String phoneFormat(String phone){
        return phone.replace("-","").replace(" ","").replace("(","").replace(")","");
    }



}