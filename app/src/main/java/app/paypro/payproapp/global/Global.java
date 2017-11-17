package app.paypro.payproapp.global;

import android.app.Application;

/**
 * Created by kike on 10/11/17.
 */

public class Global extends Application {
    private static String token = "";
//    private static String username = "";
//    private static String nickname = "";
//    private static String bitcoinAddress = "";
//    private static String bitcoinBalance = "";

    public static void setToken(String tokenValue)
    {
        token = tokenValue;
    }

    public static String getToken()
    {
        return token;
    }

//    public static void setUsername(String usernameValue)
//    {
//        username = usernameValue;
//    }
//
//    public static String getUsername()
//    {
//        return username;
//    }
//
//    public static void setNickname(String nicknameValue)
//    {
//        nickname = nicknameValue;
//    }
//
//    public static String getNickname ()
//    {
//        return nickname;
//    }
//
//    public static void setBitcoinAddress(String bitcoinAddressValue)
//    {
//        bitcoinAddress = bitcoinAddressValue;
//    }
//
//    public static String getBitcoinAddress()
//    {
//        return bitcoinAddress;
//    }
//
//    public static void setBitcoinBalance(String bitcoinBalanceValue)
//    {
//        bitcoinBalance = bitcoinBalanceValue;
//    }
//
//    public static String getBitcoinBalance()
//    {
//        return bitcoinBalance;
//    }
}
