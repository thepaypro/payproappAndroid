package app.paypro.payproapp.transaction;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import app.paypro.payproapp.http.PayProRequest;
import app.paypro.payproapp.http.ResponseListener;

/**
 * Created by rogerbaiget on 12/12/17.
 */

public class Transaction {

    public static void create(final Context context, JSONObject parameters, final ResponseListener<JSONObject> listener) throws JSONException {

        PayProRequest.post(context,"bitcoin-transactions",parameters,new ResponseListener<JSONObject>() {
            @Override
            public void getResult(JSONObject object) throws JSONException {
                if(object.has("transaction")){
                    JSONObject responseJSON = new JSONObject();
                    responseJSON.put("status", true);
                    listener.getResult(responseJSON);
                }else{
                    JSONObject errorResponse = new JSONObject();
                    errorResponse.put("status", false);

                    if (object.has("error_msg")){
                        errorResponse.put("error_msg",object.getString("error_msg"));
                    }
                    if (object.has("errorMessage")){
                        errorResponse.put("error_msg",object.getString("errorMessage"));
                    }
                    listener.getResult(errorResponse);
                }
            }
        });

    }


}
