package app.paypro.payproapp.user;

import android.content.Context;
import android.util.Log;

import app.paypro.payproapp.http.PayProRequest;
import app.paypro.payproapp.http.ResponseListener;

import org.json.JSONException;
import org.json.JSONObject;


public class User {

    public static void register(Context context, JSONObject parameters, final ResponseListener<JSONObject> listener) throws JSONException {
        PayProRequest.post(context, "register/", parameters, new ResponseListener<JSONObject>(){

            @Override
            public void getResult(JSONObject object) throws JSONException {
                if (object.has("user"))
                {
                    JSONObject responseJSON = new JSONObject();
                    responseJSON.put("status", true);
                    listener.getResult(responseJSON);
                } else {
                    JSONObject errorResponse = new JSONObject();
                    errorResponse.put("status", false);

                    if (object.has("message"))
                    {
                        Log.i("message", object.getString("message"));

                        if (object.getString("message").equals("Username already exist")) {
                            errorResponse.put("errorMessage","error_username_exist");
                        }

                        if (object.getString("message").equals("Invalid verification code")) {
                            errorResponse.put("errorMessage","error_invalid_verification_code");
                        }

                        if (object.getString("message").equals("Passwords dont match")) {
                            errorResponse.put("errorMessage","error_passcode_dont_match");
                        }

                        if (object.getString("message").equals("Invalid phone number")) {
                            errorResponse.put("errorMessage","error_invalid_phonenumber");
                        }
                    }

                    listener.getResult(errorResponse);
                }
            }
        });
    }

    public static void mobileVerificationCode(Context context, String phonenumber, final ResponseListener<JSONObject> listener) throws JSONException {
        JSONObject parameters = new JSONObject();
        parameters.put("phoneNumber", phonenumber);

        PayProRequest.post(context, "mobile-verification-code", parameters, new ResponseListener<JSONObject>() {
            @Override
            public void getResult(JSONObject object) throws JSONException {
                if (object.has("isUser"))
                {
                    JSONObject responseJSON = new JSONObject();
                    responseJSON.put("status", true);
                    responseJSON.put("isUser", object.has("isUser"));
                    listener.getResult(responseJSON);
                } else {
                    JSONObject errorResponse = new JSONObject();
                    errorResponse.put("status", false);
                    listener.getResult(errorResponse);
                }
            }
        });
    }

    public static void login(Context context, JSONObject parameters, final ResponseListener<JSONObject> listener) throws JSONException
    {
        PayProRequest.post(context, "login_check", parameters, new ResponseListener<JSONObject>() {
            @Override
            public void getResult(JSONObject object) throws JSONException {
                try {
                    if (object.has("user"))
                    {
                        String user = object.getString("user");

                        JSONObject responseJSON = new JSONObject();
                        responseJSON.put("status", true);
                        listener.getResult(responseJSON);
                    } else {
                        JSONObject errorResponse = new JSONObject();
                        errorResponse.put("status", false);
                        listener.getResult(errorResponse);
                    }

                } catch (JSONException e) {
                    Log.e("User.login", String.valueOf(e));

                    JSONObject errorResponse = new JSONObject();
                    errorResponse.put("status", false);
                    errorResponse.put("message", e.getMessage());
                    listener.getResult(errorResponse);
                }
            }
        });
    }
}
