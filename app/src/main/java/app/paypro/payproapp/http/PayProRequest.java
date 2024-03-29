package app.paypro.payproapp.http;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import app.paypro.payproapp.global.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kike on 10/11/17.
 */

public class PayProRequest {

    private static String absoluteURL = "http://api.payproapp.net/";
//    private static String absoluteURL = "http://192.168.1.202/";


    public static void post(Context context, String endpointURL, JSONObject parameters, final ResponseListener<JSONObject> listener)
    {
        String url = String.format("%s%s", absoluteURL, endpointURL);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, parameters,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.has("token"))
                                    {
                                        Global.setToken(response.getString("token"));
                                    }

                                    listener.getResult(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 400:
                                    try {
                                        JSONObject errorResponse = new JSONObject(new String(response.data));
                                        errorResponse.put("status", false);
                                        listener.getResult(errorResponse);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                default:
                                    try {
                                        JSONObject errorResponse = new JSONObject();
                                        errorResponse.put("status", false);
                                        listener.getResult(errorResponse);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        }else{
                            try {
                                JSONObject errorResponse = new JSONObject();
                                errorResponse.put("status", false);
                                errorResponse.put("error_msg", "connection_error");
                                listener.getResult(errorResponse);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                if (!Global.getToken().isEmpty())
                {
                    params.put("Authorization", "Bearer "+Global.getToken());
                }

                return params;
            }
        };

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static void get(Context context, String endpointURL, String parameters, final ResponseListener<JSONObject> listener)
    {
        if (!parameters.isEmpty())
        {
            endpointURL+= "?"+parameters;
        }

        String url = String.format("%s%s", absoluteURL, endpointURL);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    if (response.has("token"))
                                    {
                                        Global.setToken(response.getString("token"));
                                    }

                                    listener.getResult(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null) {

                            switch (response.statusCode) {
                                case 400:
                                    try {
                                        JSONObject errorResponse = new JSONObject(new String(response.data));
                                        errorResponse.put("status", false);
                                        listener.getResult(errorResponse);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                default:
                                    try {
                                        JSONObject errorResponse = new JSONObject();
                                        errorResponse.put("status", false);
                                        listener.getResult(errorResponse);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        }else{
                            try {
                                JSONObject errorResponse = new JSONObject();
                                errorResponse.put("status", false);
                                errorResponse.put("error_msg", "connection_error");
                                listener.getResult(errorResponse);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                if (!Global.getToken().isEmpty())
                {
                    params.put("Authorization", "Bearer "+Global.getToken());
                }

                return params;
            }
        };

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static void put(Context context, String endpointURL, JSONObject parameters, final ResponseListener<JSONObject> listener)
    {
        String url = String.format("%s%s", absoluteURL, endpointURL);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, url, parameters,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.has("token"))
                                    {
                                        Global.setToken(response.getString("token"));
                                    }

                                    listener.getResult(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 400:
                                    try {
                                        JSONObject errorResponse = new JSONObject(new String(response.data));
                                        errorResponse.put("status", false);
                                        listener.getResult(errorResponse);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                default:
                                    try {
                                        JSONObject errorResponse = new JSONObject();
                                        errorResponse.put("status", false);
                                        listener.getResult(errorResponse);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        }else{
                            try {
                                JSONObject errorResponse = new JSONObject();
                                errorResponse.put("status", false);
                                errorResponse.put("error_msg", "connection_error");
                                listener.getResult(errorResponse);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                if (!Global.getToken().isEmpty())
                {
                    params.put("Authorization", "Bearer "+Global.getToken());
                }

                return params;
            }
        };

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }
}
