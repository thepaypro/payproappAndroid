package app.paypro.payproapp.global;

import android.app.Application;

import app.paypro.payproapp.SendMoney;

/**
 * Created by kike on 10/11/17.
 */

public class Global extends Application {
    private static String token = "";

    private static SendMoney sendMoney = new SendMoney();

    public static SendMoney resetSendMoney(){
        sendMoney = new SendMoney();
        return sendMoney;
    }

    public static SendMoney getSendMoney() {
        return sendMoney;
    }

    public static void setSendMoney(SendMoney sendMoney) {
        sendMoney = sendMoney;
    }

    public static void setToken(String tokenValue)
    {
        token = tokenValue;
    }

    public static String getToken()
    {
        return token;
    }
}
