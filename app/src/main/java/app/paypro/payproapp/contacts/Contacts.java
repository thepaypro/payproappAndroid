package app.paypro.payproapp.contacts;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.paypro.payproapp.http.PayProRequest;
import app.paypro.payproapp.http.ResponseListener;

/**
 * Created by rogerbaiget on 27/11/17.
 */

public class Contacts {

    public static void checkContacts(Context context, JSONObject parameters, final ResponseListener<JSONObject> listener) throws JSONException{

        PayProRequest.post(context,"contacts",  parameters, new ResponseListener<JSONObject>() {

            @Override
            public void getResult(JSONObject object) throws JSONException {
                if(object.has("contacts")){
                    JSONArray contacts = object.getJSONArray("contacts");

                    JSONObject responseJSON = new JSONObject();
                    responseJSON.put("status", true);
                    responseJSON.put("contacts", contacts);
                    listener.getResult(responseJSON);
                }else{
                    JSONObject errorResponse = new JSONObject();
                    if(object.has("error_msg")){
                        errorResponse.put("error_msg", object.getString("error_msg"));
                    }
                    errorResponse.put("status", false);
                    listener.getResult(errorResponse);
                }
            }
        });
    }
}
