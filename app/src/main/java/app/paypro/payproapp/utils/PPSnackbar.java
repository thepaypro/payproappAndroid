package app.paypro.payproapp.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import app.paypro.payproapp.R;

/**
 * Created by rogerbaiget on 17/11/17.
 */

public class PPSnackbar {

    public static Snackbar getSnackbar(View view,Context context, String type){
        Snackbar snackbar;
        switch(type){
            case "connection_error":
                snackbar = Snackbar
                        .make(view, context.getResources().getString(R.string.error_connection) , Snackbar.LENGTH_LONG);
                return snackbar;
            case "error_save":
                snackbar = Snackbar
                        .make(view, context.getResources().getString(R.string.error_save), Snackbar.LENGTH_LONG);
                return snackbar;
            default:
                snackbar = Snackbar
                        .make(view, context.getResources().getString(R.string.error_basic), Snackbar.LENGTH_LONG);
                return snackbar;
        }
    }
}
