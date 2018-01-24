package app.paypro.payproapp.asynctask.db.user;

import android.content.Context;
import android.os.AsyncTask;

import app.paypro.payproapp.db.AppDatabase;
import app.paypro.payproapp.db.entity.User;

/**
 * Created by rogerbaiget on 16/11/17.
 */

public class SaveUserAsyncTask extends AsyncTask<User, Void, Void> {
    Context context;
    public SaveUserAsyncTask(Context context) {
        this.context = context;
    }

    protected Void doInBackground(User... users) {
        AppDatabase.getAppDatabase(context).userDao().deleteAll();
        AppDatabase.getAppDatabase(context).userDao().insert(users[0]);
        return null;
    }
}
