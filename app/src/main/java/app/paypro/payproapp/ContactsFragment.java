package app.paypro.payproapp;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import app.paypro.payproapp.asynctask.SaveContactsAsyncTask;
import app.paypro.payproapp.global.Global;
import app.paypro.payproapp.utils.PPAlertDialog;

import static android.Manifest.permission.READ_CONTACTS;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;


public class ContactsFragment extends Fragment {

    private static final int REQUEST_READ_CONTACTS = 101;

    private ListView mContactsList;
    private LinearLayout emptyListView;
    private LinearLayout permissionDeniedView;
    private LinearLayout connectionErrorView;
    private ConstraintLayout mainView;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private Boolean mIsRestoredFromBackstack;

    private ContactsAdapter contactsAdapter;

    public void setContactsAdapter(ContactsAdapter contactsAdapter) {
        this.contactsAdapter = contactsAdapter;
    }

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
        mIsRestoredFromBackstack = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contacts_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        emptyListView = getActivity().findViewById(R.id.empty_list_view);
        permissionDeniedView = getActivity().findViewById(R.id.permission_denied_view);
        connectionErrorView = getActivity().findViewById(R.id.connection_error_view);
        mainView = getActivity().findViewById(R.id.main_view);
        progressBar = getActivity().findViewById(R.id.progress_bar);
        fab = getActivity().findViewById(R.id.fab);
        toolbar = getActivity().findViewById(R.id.toolbar);

        ((TabActivity)getActivity()).setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMoneyAddressFragment myfragment = new SendMoneyAddressFragment();
                FragmentManager fragmentManager = ((TabActivity)getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.frame_layout, myfragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        mContactsList = getActivity().findViewById(R.id.contacts_list);
        mContactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact contact = (Contact) adapterView.getItemAtPosition(i);
                if(contact.getIsUser()){
                    SendMoney sendMoney = Global.resetSendMoney();

                    sendMoney.setUserId(contact.getUserId());
                    sendMoney.setAccountId(contact.getAccountId());

                    Global.setSendMoney(sendMoney);

                    SendMoneyAmountFragment myfragment = new SendMoneyAmountFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("origin",getResources().getString(R.string.contacts_title));
                    myfragment.setArguments(bundle);
                    FragmentManager fragmentManager = ((TabActivity)getContext()).getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    transaction.replace(R.id.frame_layout, myfragment);
                    transaction.addToBackStack(null);
                    ((TabActivity) getActivity()).hideVirtualKeyboard();
                    transaction.commit();
                }else{
                    DialogInterface.OnClickListener dialogInterfaceOK = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            PackageManager pm = getActivity().getPackageManager();
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.sms_msg));
                            sendIntent.setType("text/plain");
                            try {
                                PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                                sendIntent.setPackage("com.whatsapp");
                            } catch (PackageManager.NameNotFoundException e) {
                                // SHOW ALL MSG APPS
                            }
                            startActivity(sendIntent);
                        }
                    };
                    DialogInterface.OnClickListener dialogInterfaceCancell = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    };
                    PPAlertDialog.getAlertDialogBuilder(getActivity(), "invite_dialog", dialogInterfaceOK, dialogInterfaceCancell).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!mIsRestoredFromBackstack)
        {
            loadContacts();
        }else{
            contactsAdapter.getFilter().filter("");
            mContactsList.setAdapter(contactsAdapter);
        }

        TextView toolbarTitle = getActivity().findViewById(R.id.app_toolbar_title);
        toolbarTitle.setText(getResources().getString(R.string.contacts_title));

        getActivity().findViewById(R.id.app_toolbar_back_button_image).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.app_toolbar_back_button_text).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.app_toolbar_confirm_button).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.app_toolbar_progress_bar).setVisibility(View.INVISIBLE);

    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        mIsRestoredFromBackstack = true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.search_menu, menu);

        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();

        ((EditText)search.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);
        ((EditText)search.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.WHITE);
        ((EditText)search.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHighlightColor(Color.WHITE);

        ImageView searchCloseIcon = search.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchCloseIcon.setImageResource(R.drawable.ic_close_white_24dp);

        ImageView searchIcon = search.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchIcon.setImageResource(R.drawable.ic_search_white_24dp);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(contactsAdapter != null){
                    contactsAdapter.getFilter().filter(newText);
                    return true;
                }
                return false;
            }
        });

    }

    private void requestContactsPermission() {

        final String[] permissions = new String[]{READ_CONTACTS};

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

    public void loadContacts() {

        int rc = checkSelfPermission(getContext(), READ_CONTACTS);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            Uri allContacts = ContactsContract.Contacts.CONTENT_URI;
            CursorLoader cursorLoader = new CursorLoader(
                    getContext(),
                    allContacts,
                    PROJECTION,
                    null,
                    null,
                    null);
            new SaveContactsAsyncTask(getContext(), cursorLoader.loadInBackground(), this, mContactsList).execute();
        } else {
            requestContactsPermission();
        }

    }

    public void showProgressBar(){
        mContactsList.setVisibility(View.GONE);
        emptyListView.setVisibility(View.GONE);
        permissionDeniedView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
        emptyListView.setVisibility(View.GONE);
        permissionDeniedView.setVisibility(View.GONE);
        connectionErrorView.setVisibility(View.GONE);
        mContactsList.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

    public void showEmptyListView(){
        progressBar.setVisibility(View.GONE);
        mContactsList.setVisibility(View.GONE);
        permissionDeniedView.setVisibility(View.GONE);
        connectionErrorView.setVisibility(View.GONE);
        emptyListView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

    public void showPermissionDeniedView(){
        progressBar.setVisibility(View.GONE);
        mContactsList.setVisibility(View.GONE);
        emptyListView.setVisibility(View.GONE);
        connectionErrorView.setVisibility(View.GONE);
        permissionDeniedView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);

    }

    public void showConnectionErrorView(){
        progressBar.setVisibility(View.GONE);
        mContactsList.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
        emptyListView.setVisibility(View.GONE);
        permissionDeniedView.setVisibility(View.GONE);
        connectionErrorView.setVisibility(View.VISIBLE);
    }

    public void setToolbarSubtitle(int contactsSize){
        toolbar.setSubtitle(contactsSize + " contacts");

    }
}