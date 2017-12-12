package app.paypro.payproapp.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by rogerbaiget on 17/11/17.
 */





public class PPSnackbar {

    public static Snackbar getSnackbar(View view, String type){
        Snackbar snackbar;
        switch(type){
            case "connection_error":
                snackbar = Snackbar
                        .make(view, "No internet connection!", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.RED);
                return snackbar;
            case "Insufficient funds":
                snackbar = Snackbar
                        .make(view, "Insufficient funds", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.RED);
                return snackbar;
            default:
                snackbar = Snackbar
                        .make(view, "Ups! Sorry, something went wrong", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.RED);
                return snackbar;
        }
    }



}
