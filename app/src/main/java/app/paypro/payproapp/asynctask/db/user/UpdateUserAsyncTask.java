package app.paypro.payproapp.asynctask.db.user;

import android.content.Context;
import android.os.AsyncTask;

import app.paypro.payproapp.db.AppDatabase;
import app.paypro.payproapp.db.entity.Account;
import app.paypro.payproapp.db.entity.User;

/**
 * Created by rogerbaiget on 22/11/17.
 */

public class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
    Context context;
    public UpdateUserAsyncTask(Context context) {
        this.context = context;
    }

    protected Void doInBackground(User... users) {
        AppDatabase.getAppDatabase(context).userDao().update(users[0]);
        return null;
    }
}

