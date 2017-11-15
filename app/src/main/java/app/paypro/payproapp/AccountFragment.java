package app.paypro.payproapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import app.paypro.payproapp.account.Account;
import app.paypro.payproapp.http.ResponseListener;

public class AccountFragment extends Fragment {
    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Account.info(getContext(), new ResponseListener<JSONObject>() {
                @Override
                public void getResult(JSONObject object) throws JSONException {
                    try {
                        if (object.getString("status").equals("true")) {
                            Log.i("ACCOUNT-INFO", object.getString("info"));
                        } else {
                            Log.i("ACCOUNT-INFO", "nadaaaaa");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            Log.i("aaaavbvbvbbb", "aaaaaaaa");
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_tab, container, false);
    }
}