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
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import static android.Manifest.permission.READ_CONTACTS;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;


public class ContactsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final int REQUEST_READ_CONTACTS = 101;

    ArrayList<Contact> contacts = new ArrayList<Contact>();

    ListView mContactsList;
    RelativeLayout mainView;

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
            Snackbar.make(mainView, "", Snackbar.LENGTH_INDEFINITE)
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

        contactsAdapter = new ContactsAdapter(
                getContext(), R.layout.contacts_list_item, contacts);

        mContactsList.setAdapter(contactsAdapter);

    }

    private void saveContacts(Cursor c) {
        ContentResolver cr = getActivity().getContentResolver();
        //---display the contact id and name and phone number----
        if (c.moveToFirst()) {
            do {
                //---get the contact id and name
                String contactID = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                String contactDisplayName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                String contactDisplayPhone = "";

                //---get phone number---
                int hasPhone = c.getInt(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (hasPhone == 1) {
                    Cursor phoneCursor = getActivity().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " +
                                    contactID, null, null);
                    while (phoneCursor.moveToNext()) {
                        contactDisplayPhone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phoneCursor.close();

                }

                //---get image number---
                String contactImageUri = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));


                contacts.add(new Contact(contactID, contactDisplayName, contactImageUri, contactDisplayPhone));

            } while (c.moveToNext());
        }
    }



}