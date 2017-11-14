package app.paypro.payproapp.http;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kike on 9/11/17.
 */

public interface ResponseListener<T>
{
    void getResult(JSONObject object) throws JSONException;
}
