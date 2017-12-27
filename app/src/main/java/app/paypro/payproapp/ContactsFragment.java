package app.paypro.payproapp;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.paypro.payproapp.anim.ResizeAnimation;
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
    private LinearLayout noResultsView;
    private TextView noResultsText;
    private RelativeLayout mainView;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private Boolean mIsRestoredFromBackstack;
    private ImageButton searchButton;
    private ConstraintLayout searchView;
    private EditText searchText;
    private ImageButton searchCloseButton;
    private ImageButton searchBackButton;
    private RelativeLayout appToolbarLayout;
    private FrameLayout frameLayout;
    private ConstraintLayout activityMain;

    private ContactsAdapter contactsAdapter;

    public void setContactsAdapter(final ContactsAdapter contactsAdapter) {
        contactsAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (contactsAdapter.getCount() == 0){
                    noResultsText.setText(String.format(getResources().getString(R.string.no_results), searchText.getText().toString()));
                    noResultsView.setVisibility(View.VISIBLE);
                }else{
                    noResultsView.setVisibility(View.GONE);
                }
            }
        });
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
        noResultsView = getActivity().findViewById(R.id.no_results_view);
        noResultsText = getActivity().findViewById(R.id.no_results_text);
        mainView = getActivity().findViewById(R.id.main_view);
        progressBar = getActivity().findViewById(R.id.progress_bar);
        fab = getActivity().findViewById(R.id.fab);
        appToolbarLayout = getActivity().findViewById(R.id.app_toolbar_layout);
        frameLayout = getActivity().findViewById(R.id.frame_layout);
        activityMain = getActivity().findViewById(R.id.activity_main);

        searchButton = getActivity().findViewById(R.id.app_toolbar_search_button);
        searchView = getActivity().findViewById(R.id.search_view);
        searchText = getActivity().findViewById(R.id.search_text);
        searchCloseButton = getActivity().findViewById(R.id.search_close_button);
        searchBackButton = getActivity().findViewById(R.id.search_back_button);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(contactsAdapter != null){
                    contactsAdapter.getFilter().filter(charSequence);
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

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showToolbarSearch();
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideToolbarSearch();
                SendMoneyAddressFragment myfragment = new SendMoneyAddressFragment();
                FragmentManager fragmentManager = ((TabActivity)getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.frame_layout, myfragment);
                transaction.addToBackStack(null);
                transaction.commit();
                appToolbarLayout.setVisibility(View.VISIBLE);
            }
        });

        mContactsList = getActivity().findViewById(R.id.contacts_list);
        mContactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact contact = (Contact) adapterView.getItemAtPosition(i);
                if(contact.getIsUser()){
                    hideToolbarSearch();
                    SendMoney sendMoney = Global.resetSendMoney();

                    sendMoney.setUserId(contact.getUserId());
                    sendMoney.setAccountId(contact.getAccountId());

                    if(!contact.getBackFullName().isEmpty()){
                        sendMoney.setLabel(contact.getBackFullName());
                    }

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
                    appToolbarLayout.setVisibility(View.VISIBLE);
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
            if(contactsAdapter.getCount() > 0){
                searchButton.setVisibility(View.VISIBLE);
            }
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
        hideToolbarSearch();
        mContactsList.setVisibility(View.GONE);
        emptyListView.setVisibility(View.GONE);
        permissionDeniedView.setVisibility(View.GONE);
        connectionErrorView.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        noResultsView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
        emptyListView.setVisibility(View.GONE);
        permissionDeniedView.setVisibility(View.GONE);
        connectionErrorView.setVisibility(View.GONE);
        noResultsView.setVisibility(View.GONE);
        mContactsList.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.VISIBLE);
    }

    public void showEmptyListView(){
        progressBar.setVisibility(View.GONE);
        mContactsList.setVisibility(View.GONE);
        permissionDeniedView.setVisibility(View.GONE);
        connectionErrorView.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        noResultsView.setVisibility(View.GONE);
        emptyListView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

    public void showPermissionDeniedView(){
        progressBar.setVisibility(View.GONE);
        mContactsList.setVisibility(View.GONE);
        emptyListView.setVisibility(View.GONE);
        connectionErrorView.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        noResultsView.setVisibility(View.GONE);
        permissionDeniedView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);

    }

    public void showConnectionErrorView(){
        progressBar.setVisibility(View.GONE);
        mContactsList.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
        emptyListView.setVisibility(View.GONE);
        permissionDeniedView.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        noResultsView.setVisibility(View.GONE);
        connectionErrorView.setVisibility(View.VISIBLE);
    }

    public void showToolbarSearch(){
        appToolbarLayout.setVisibility(View.GONE);
        searchView.setVisibility(View.VISIBLE);
        searchView.bringToFront();
        searchText.requestFocus();
        ((TabActivity)getActivity()).showVirtualKeyboard(searchText);
    }

    public void hideToolbarSearch() {
        ((TabActivity)getActivity()).hideVirtualKeyboard();
        searchText.setText("");
        searchView.setVisibility(View.GONE);
        appToolbarLayout.setVisibility(View.VISIBLE);
    }


    public Boolean onBackPressed(){
        if(searchView.getVisibility() == View.VISIBLE) {
            hideToolbarSearch();
            return true;
        }else{
            return false;
        }
    }

}