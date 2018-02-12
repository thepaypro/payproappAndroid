package app.paypro.payproapp.user;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import app.paypro.payproapp.PasscodeActivity;
import app.paypro.payproapp.WelcomeActivity;
import app.paypro.payproapp.asynctask.db.user.GetUserAsyncTask;
import app.paypro.payproapp.asynctask.db.user.SaveAccountAsyncTask;
import app.paypro.payproapp.asynctask.db.user.SaveUserAsyncTask;
import app.paypro.payproapp.asynctask.db.user.UpdateAccountAsyncTask;
import app.paypro.payproapp.asynctask.db.user.UpdateUserAsyncTask;
import app.paypro.payproapp.db.entity.Account;
import app.paypro.payproapp.http.PayProRequest;
import app.paypro.payproapp.http.ResponseListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class User {

    public static void register(final Context context, JSONObject parameters, final ResponseListener<JSONObject> listener) throws JSONException {
        PayProRequest.post(context, "register/", parameters, new ResponseListener<JSONObject>(){

            @Override
            public void getResult(JSONObject object) throws JSONException {
                try{
                    if (object.has("user"))
                    {
                        JSONObject userJSON = object.getJSONObject("user");

                        app.paypro.payproapp.db.entity.User userEntity = new app.paypro.payproapp.db.entity.User(userJSON.getInt("id"),userJSON.getString("username"));

                        if(!userJSON.isNull("bitcoinAccount")){
                            JSONObject accountJSON = userJSON.getJSONObject("bitcoinAccount");
                            Account accountEntity= new Account(accountJSON.getInt("id"),accountJSON.getString("address"));
                            new SaveAccountAsyncTask(context).execute(accountEntity);
                        }else{
                            JSONObject errorResponse = new JSONObject();
                            errorResponse.put("status", false);
                            listener.getResult(errorResponse);
                        }

                        new SaveUserAsyncTask(context).execute(userEntity);

                        app.paypro.payproapp.account.Account.info(context, new ResponseListener<JSONObject>() {
                            @Override
                            public void getResult(JSONObject object) throws JSONException {
                                try {
                                    if (object.getString("status").equals("true")) {
                                        JSONObject responseJSON = new JSONObject();
                                        responseJSON.put("status", true);
                                        listener.getResult(responseJSON);
                                    } else {
                                        JSONObject errorResponse = new JSONObject();
                                        if(object.has("error_msg")){
                                            errorResponse.put("error_msg", object.getString("error_msg"));
                                        }
                                        errorResponse.put("status", false);
                                        listener.getResult(errorResponse);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        JSONObject errorResponse = new JSONObject();
                        errorResponse.put("status", false);

                        if (object.has("error_msg")){
                            errorResponse.put("error_msg",object.getString("error_msg"));

                        }else if (object.has("message")) {
                            if (object.getString("message").equals("Username already exist")) {
                                errorResponse.put("error_msg","error_username_exist");

                            }if (object.getString("message").equals("Invalid verification code")) {
                                errorResponse.put("error_msg","error_invalid_verification_code");

                            }if (object.getString("message").equals("Passwords dont match")) {
                                errorResponse.put("error_msg","error_passcode_dont_match");

                            }if (object.getString("message").equals("Invalid phone number")) {
                                errorResponse.put("error_msg","error_invalid_phonenumber");
                            }
                        } else if (object.has("errorMessage")){
                            errorResponse.put("error_msg",object.getString("errorMessage"));
                        }

                        listener.getResult(errorResponse);
                    }
                } catch (JSONException e) {
                    JSONObject errorResponse = new JSONObject();
                    errorResponse.put("status", false);
                    errorResponse.put("error_msg", e.getMessage());
                    listener.getResult(errorResponse);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
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
                    responseJSON.put("isUser", object.getBoolean("isUser"));
                    listener.getResult(responseJSON);
                } else {
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

    public static void login(final Context context, JSONObject parameters, final ResponseListener<JSONObject> listener) throws JSONException
    {
        PayProRequest.post(context, "login_check", parameters, new ResponseListener<JSONObject>() {
            @Override
            public void getResult(JSONObject object) throws JSONException {
                try {
                    if (object.has("user") && object.has("version"))
                    {
                        final JSONObject responseJSON = new JSONObject();
                        JSONArray version = object.getJSONArray("version");
                        for(int i = 0; i<version.length(); i++){
                            if( ((JSONObject) version.get(i)).getString("os").equals("android")){
                                JSONObject androidVersion = ((JSONObject) version.get(i));
                                responseJSON.put("version", androidVersion);
                            }
                        }

                        JSONObject userJSON = object.getJSONObject("user");

                        Boolean firstTimeUser = false;
                        app.paypro.payproapp.db.entity.User[] user = new GetUserAsyncTask(context).execute().get();
                        if (user.length == 0 || !user[0].getUsername().equals(userJSON.getString("username"))){
                            firstTimeUser = true;
                        }

                        app.paypro.payproapp.db.entity.User userEntity = new app.paypro.payproapp.db.entity.User(userJSON.getInt("id"),userJSON.getString("username"));

                        if(!userJSON.isNull("nickname")){
                            userEntity.setNickname(userJSON.getString("nickname"));
                        } else {
                            userEntity.setNickname("");
                        }

                        if(!userJSON.isNull("bitcoinAccount")){
                            JSONObject accountJSON = userJSON.getJSONObject("bitcoinAccount");
                            Account accountEntity= new Account(accountJSON.getInt("id"),accountJSON.getString("address"));
                            if(firstTimeUser) {
                                new SaveAccountAsyncTask(context).execute(accountEntity);
                            }else{
                                new UpdateAccountAsyncTask(context).execute(accountEntity);
                            }
                        }else{
                            JSONObject errorResponse = new JSONObject();
                            errorResponse.put("status", false);
                            listener.getResult(errorResponse);
                        }

                        if(firstTimeUser){
                            new SaveUserAsyncTask(context).execute(userEntity);
                        }else{
                            new UpdateUserAsyncTask(context).execute(userEntity);
                        }

                        app.paypro.payproapp.account.Account.info(context, new ResponseListener<JSONObject>() {
                            @Override
                            public void getResult(JSONObject object) throws JSONException {
                                try {
                                    if (object.getString("status").equals("true")) {
                                        responseJSON.put("status", true);
                                        listener.getResult(responseJSON);
                                    } else {
                                        JSONObject errorResponse = new JSONObject();
                                        if(object.has("error_msg")){
                                            errorResponse.put("error_msg", object.getString("error_msg"));
                                        }
                                        errorResponse.put("status", false);
                                        listener.getResult(errorResponse);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        JSONObject errorResponse = new JSONObject();
                        if(object.has("error_msg")){
                            errorResponse.put("error_msg", object.getString("error_msg"));
                        }
                        else if (object.has("errorMessage")){
                            errorResponse.put("error_msg",object.getString("errorMessage"));
                        }
                        errorResponse.put("status", false);
                        listener.getResult(errorResponse);
                    }

                } catch (JSONException e) {
                    JSONObject errorResponse = new JSONObject();
                    errorResponse.put("status", false);
                    errorResponse.put("error_msg", e.getMessage());
                    listener.getResult(errorResponse);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void updateInfo(final Context context, JSONObject parameters, final ResponseListener<JSONObject> listener) throws JSONException
    {
        PayProRequest.put(context, "users/profile/0", parameters, new ResponseListener<JSONObject>() {
            @Override
            public void getResult(JSONObject object) throws JSONException {
                try {
                    if (object.has("user"))
                    {
                        JSONObject userJSON = object.getJSONObject("user");

                        app.paypro.payproapp.db.entity.User userEntity = new app.paypro.payproapp.db.entity.User(userJSON.getInt("id"),userJSON.getString("username"));

                        if(!userJSON.isNull("nickname")){
                            userEntity.setNickname(userJSON.getString("nickname"));
                            new SaveUserAsyncTask(context).execute(userEntity);
                        }

                        JSONObject responseJSON = new JSONObject();
                        responseJSON.put("status", true);
                        listener.getResult(responseJSON);
                    } else {
                        JSONObject errorResponse = new JSONObject();

                        if(object.has("error_msg")){
                            errorResponse.put("error_msg", object.getString("error_msg"));
                        }
                        if (object.has("errorMessage")){
                            errorResponse.put("error_msg",object.getString("errorMessage"));
                        }

                        errorResponse.put("status", false);
                        listener.getResult(errorResponse);
                    }
                } catch (JSONException e) {
                    Log.e("User.updateInfo", String.valueOf(e));

                    JSONObject errorResponse = new JSONObject();
                    errorResponse.put("status", false);
                    errorResponse.put("error_msg", e.getMessage());
                    listener.getResult(errorResponse);
                }
            }
        });
    }

    public static void changePasscode(final Context context, JSONObject parameters, final ResponseListener<JSONObject> listener) throws JSONException
    {
        PayProRequest.put(context, "users/0", parameters, new ResponseListener<JSONObject>() {
            @Override
            public void getResult(JSONObject object) throws JSONException {
                try {
                    if (object.has("user"))
                    {
                        JSONObject responseJSON = new JSONObject();
                        responseJSON.put("status", true);
                        listener.getResult(responseJSON);
                    } else {
                        JSONObject errorResponse = new JSONObject();

                        if (object.has("error_msg")){
                            errorResponse.put("error_msg",object.getString("error_msg"));
                        }
                        if(object.has("message")){
                            errorResponse.put("error_msg", object.getString("message"));
                        }

                        errorResponse.put("status", false);
                        listener.getResult(errorResponse);
                    }
                } catch (JSONException e) {
                    Log.e("User.changePasscode", String.valueOf(e));

                    JSONObject errorResponse = new JSONObject();
                    errorResponse.put("status", false);
                    errorResponse.put("error_msg", e.getMessage());
                    listener.getResult(errorResponse);
                }
            }
        });
    }
}
