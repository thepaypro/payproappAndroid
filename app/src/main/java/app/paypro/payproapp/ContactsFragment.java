package app.paypro.payproapp;


import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import app.paypro.payproapp.contacts.Contacts;
import app.paypro.payproapp.global.Global;
import app.paypro.payproapp.http.ResponseListener;

import static android.Manifest.permission.READ_CONTACTS;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;


public class ContactsFragment extends Fragment {

    private static final int REQUEST_READ_CONTACTS = 101;

    ArrayList<Contact> contacts = new ArrayList<Contact>();

    private ListView mContactsList;
    private LinearLayout emptyListView;
    private LinearLayout permissionDeniedView;
    private FrameLayout mainView;
    private ProgressBar progressBar;
    private FloatingActionButton fab;

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

        mContactsList = getActivity().findViewById(R.id.contacts_list);
        emptyListView = getActivity().findViewById(R.id.empty_list_view);
        permissionDeniedView = getActivity().findViewById(R.id.permission_denied_view);
        mainView = getActivity().findViewById(R.id.main_view);
        progressBar = getActivity().findViewById(R.id.progress_bar);
        fab = getActivity().findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMoney sendMoney = Global.resetSendMoney();

                Global.setSendMoney(sendMoney);

                SendMoneyAddressFragment myfragment = new SendMoneyAddressFragment();
                FragmentManager fragmentManager = ((TabActivity)getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.add(R.id.frame_layout, myfragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        disableView();
        int rc = checkSelfPermission(getContext(), READ_CONTACTS);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            loadContacts();
        } else {
            requestContactsPermission();
        }


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

    private void requestContactsPermission() {


        final String[] permissions = new String[]{READ_CONTACTS};

//        if (!shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            requestPermissions(permissions, REQUEST_READ_CONTACTS);
//            return;
//        }

        requestPermissions(permissions, REQUEST_READ_CONTACTS);

        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != REQUEST_READ_CONTACTS) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted , Access contacts here or do whatever you need.
            loadContacts();
            return;
        }

        showPermissionDeniedView();
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

    private void saveContacts(final Cursor c) {
        final HashMap<String,String> allContactsNumbers = new HashMap<>();
        //---display the contact id and name and phone number----
        if(c.getCount() == 0){
            showEmptyListView();
            return;
        }
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
                    contacts.add(new Contact(contactDisplayName, contactImageUri, contactNumbers));
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
                                                Contact contact = contacts.get(i);
                                                if(!contact.getIsUser()){
                                                    contact.setIsUser(isUser);
                                                    if(contact.getIsUser()){
                                                        contact.setUserId(responseNumberObject.getInt("userId"));
                                                        contact.setAccountId(responseNumberObject.getInt("accountId"));
                                                    }
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
        mContactsList.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
        emptyListView.setVisibility(View.GONE);
        permissionDeniedView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void enableView(){
        progressBar.setVisibility(View.GONE);
        emptyListView.setVisibility(View.GONE);
        permissionDeniedView.setVisibility(View.GONE);
        mContactsList.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

    public void showEmptyListView(){
        progressBar.setVisibility(View.GONE);
        mContactsList.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
        permissionDeniedView.setVisibility(View.GONE);
        emptyListView.setVisibility(View.VISIBLE);
    }

    public void showPermissionDeniedView(){
        progressBar.setVisibility(View.GONE);
        mContactsList.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
        emptyListView.setVisibility(View.GONE);
        permissionDeniedView.setVisibility(View.VISIBLE);

    }

    public String phoneFormat(String phone){
        return phone.replace("-","").replace(" ","").replace("(","").replace(")","");
    }



}