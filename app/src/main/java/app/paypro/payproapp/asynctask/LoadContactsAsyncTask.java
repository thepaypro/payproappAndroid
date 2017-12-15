package app.paypro.payproapp.asynctask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import app.paypro.payproapp.Contact;
import app.paypro.payproapp.ContactsAdapter;
import app.paypro.payproapp.R;
import app.paypro.payproapp.SendMoney;
import app.paypro.payproapp.SendMoneyAmountFragment;
import app.paypro.payproapp.TabActivity;
import app.paypro.payproapp.contacts.Contacts;
import app.paypro.payproapp.global.Global;
import app.paypro.payproapp.http.ResponseListener;

/**
 * Created by rogerbaiget on 14/12/17.
 */

public class LoadContactsAsyncTask extends AsyncTask<Void,Void,ContactsAdapter> {
    private ListView mContactsList;
    private ProgressBar progressBar;
    private LinearLayout emptyListView;
    private Toolbar toolbar;
    private ContactsAdapter contactsAdapter;
    private Context context;
    private FloatingActionButton fab;

    ArrayList<Contact> contacts = new ArrayList<>();

    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_URI,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER
            };

    public LoadContactsAsyncTask(Context context, ProgressBar progressBar, ListView mContactsList, LinearLayout emptyListView, Toolbar toolbar, FloatingActionButton fab){
        this.context = context;
        this.progressBar = progressBar;
        this.mContactsList = mContactsList;
        this.emptyListView = emptyListView;
        this.toolbar = toolbar;
        this.fab = fab;
    }
    @Override
    protected ContactsAdapter doInBackground(Void... voids) {
        Uri allContacts = ContactsContract.Contacts.CONTENT_URI;

        //declare our cursor
        Cursor c;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                allContacts,
                PROJECTION,
                null,
                null ,
                null);
        c = cursorLoader.loadInBackground();


        saveContacts(c);

        return contactsAdapter;
    }

    private void saveContacts(final Cursor c) {
        final HashMap<String,String> allContactsNumbers = new HashMap<>();
        contacts.clear();
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
                    Cursor phoneCursor = context.getContentResolver().query(
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
                Contacts.checkContacts(context,parameters,new ResponseListener<JSONObject>(){
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
                        contactsAdapter = new ContactsAdapter(context, R.layout.contacts_list_item, sortList(contacts));
                        mContactsList.setAdapter(contactsAdapter);

                        enableView();
                        toolbar.setSubtitle(contacts.size() + " contacts");
//                        toolbarSubtitle.setText(contacts.size() + " contacts");
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

    public String phoneFormat(String phone){
        return phone.replace("-","").replace(" ","").replace("(","").replace(")","");
    }

    public void enableView(){
        progressBar.setVisibility(View.GONE);
        emptyListView.setVisibility(View.GONE);
        mContactsList.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

    public void showEmptyListView(){
        progressBar.setVisibility(View.GONE);
        mContactsList.setVisibility(View.GONE);
        emptyListView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.GONE);
    }
}
