package app.paypro.payproapp.utils;

import android.content.Context;
import android.content.DialogInterface;

import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import app.paypro.payproapp.R;

/**
 * Created by rogerbaiget on 19/12/17.
 */

public class PPAlertDialog {

    public static AlertDialog.Builder getAlertDialogBuilder(final Context context, final String type, DialogInterface.OnClickListener okAction, DialogInterface.OnClickListener cancelAction) {
        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, R.style.PPAlertDialog);
        } else {
            builder = new AlertDialog.Builder(context);
        }

        switch (type) {
            case "force_update":
                builder.setMessage(R.string.force_update)
                        .setPositiveButton(android.R.string.ok, okAction)
                        .setNegativeButton(android.R.string.cancel, cancelAction);
                return builder;
            case "optional_update":
                builder.setMessage(R.string.optional_update)
                        .setPositiveButton(android.R.string.ok, okAction)
                        .setNegativeButton(android.R.string.cancel, cancelAction);
                return builder;
            case "error_username_exist":
                builder.setMessage(R.string.error_username_exist)
                        .setPositiveButton(android.R.string.ok, okAction)
                        .setNegativeButton(android.R.string.cancel, cancelAction);
                return builder;
            case "error_invalid_verification_code":
                builder.setMessage(R.string.error_invalid_verification_code)
                        .setPositiveButton(R.string.try_again, okAction)
                        .setNegativeButton(android.R.string.cancel, cancelAction);
                return builder;
            case "error_passcode_dont_match":
                builder.setMessage(R.string.error_passcode_dont_match)
                        .setPositiveButton(R.string.try_again, okAction)
                        .setNegativeButton(android.R.string.cancel, cancelAction);
                return builder;
            case "error_invalid_phonenumber":
                builder.setMessage(R.string.error_invalid_phonenumber)
                        .setPositiveButton(android.R.string.ok, okAction)
                        .setNegativeButton(android.R.string.cancel, cancelAction);
                return builder;
            case "error_change_passcode":
                builder.setMessage(R.string.error_change_passcode)
                        .setTitle(R.string.error)
                        .setPositiveButton(android.R.string.ok, okAction);
                return builder;
            case "invite_dialog":
                builder.setMessage(R.string.invite_dialog)
                        .setTitle(R.string.invite_dialog_title)
                        .setPositiveButton(android.R.string.ok, okAction)
                        .setNegativeButton(android.R.string.cancel, cancelAction);
                return builder;
            case "insufficient_funds":
                builder.setMessage(R.string.error_insufficient_funds)
                        .setPositiveButton(android.R.string.ok, okAction);
                return builder;
            case "connection_error":
                builder.setMessage(R.string.error_connection)
                        .setPositiveButton(android.R.string.ok, okAction);
                return builder;
            default:
                builder.setMessage(R.string.error_basic)
                        .setPositiveButton(android.R.string.ok, okAction)
                        .setNegativeButton(android.R.string.cancel, cancelAction);
                return builder;
        }

    }
}
