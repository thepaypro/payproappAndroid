package app.paypro.payproapp.account;

import android.content.Context;
import android.util.Log;

import app.paypro.payproapp.global.Global;
import app.paypro.payproapp.http.PayProRequest;
import app.paypro.payproapp.http.ResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kike on 10/11/17.
 */

public class Account {

    public static void info(Context context, final ResponseListener<JSONObject> listener) throws JSONException {

        PayProRequest.get(context, "accounts_info", "", new ResponseListener<JSONObject>() {
            @Override
            public void getResult(JSONObject object) throws JSONException {
                Log.i("accountinfo", object.toString());
                if (object.has("info"))
                {
//                    Global.setBitcoinBalance(object.getJSONObject("info").getString("bitcoinBalance"));
                    JSONObject responseJSON = new JSONObject();
                    responseJSON.put("status", true);
                    responseJSON.put("info", object.getString("info"));
                    listener.getResult(responseJSON);
                } else {
                    JSONObject errorResponse = new JSONObject();
                    errorResponse.put("status", false);
                    listener.getResult(errorResponse);
                }
            }
        });
    }
}
