package app.paypro.payproapp.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import app.paypro.payproapp.Contact;
import app.paypro.payproapp.ContactsAdapter;
import app.paypro.payproapp.ContactsFragment;
import app.paypro.payproapp.R;
import app.paypro.payproapp.contacts.Contacts;
import app.paypro.payproapp.http.ResponseListener;

/**
 * Created by rogerbaiget on 14/12/17.
 */

public class SaveContactsAsyncTask extends AsyncTask<Void,Void,Void> {

    private ArrayList<Contact> contacts = new ArrayList<>();
    private Context context;
    private Cursor c;
    private ContactsFragment contactsFragment;
    private ListView mContactsList;
    private ContactsAdapter contactsAdapter;

    public SaveContactsAsyncTask(Context context, Cursor cursor, ContactsFragment contactsFragment,ListView mContactsList){
        this.context = context;
        this.c = cursor;
        this.contactsFragment = contactsFragment;
        this.mContactsList = mContactsList;
    }

    @Override
    protected Void doInBackground(Void... voids) {
            final HashMap<String,String> allContactsNumbers = new HashMap<>();
            contacts.clear();
            //---display the contact id and name and phone number----
            if(c.getCount() == 0){
                contactsFragment.showEmptyListView();
                return null;
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
                            if(contactsFragment!=null && contactsFragment.getActivity()!=null) {
                                contactsFragment.hideProgressBar();
                                contactsFragment.setToolbarSubtitle(contacts.size());
                                contactsAdapter = new ContactsAdapter(context, R.layout.contacts_list_item, sortList(contacts));
                                mContactsList.setAdapter(contactsAdapter);
                                contactsFragment.setContactsAdapter(contactsAdapter);
                                contactsFragment = null;
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        return null;
    }

    public String phoneFormat(String phone){
        return phone.replace("-","").replace(" ","").replace("(","").replace(")","");
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

    @Override
    protected void onPostExecute(Void result){

    }

    @Override
    protected void onPreExecute (){
        contactsFragment.showProgressBar();
    }


}
