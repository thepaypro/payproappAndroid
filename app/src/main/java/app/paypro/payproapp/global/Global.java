package app.paypro.payproapp.global;

import android.app.Application;

/**
 * Created by kike on 10/11/17.
 */

public class Global extends Application {
    private static String token = "";

    public static void setToken(String tokenValue)
    {
        token = tokenValue;
    }

    public static String getToken()
    {
        return token;
    }
}
