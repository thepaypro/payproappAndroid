package app.paypro.payproapp.asynctask.db.user;

import android.content.Context;
import android.os.AsyncTask;

import app.paypro.payproapp.db.AppDatabase;
import app.paypro.payproapp.db.entity.User;

/**
 * Created by rogerbaiget on 17/11/17.
 */

public class GetUserAsyncTask extends AsyncTask <Void, Void, User[]>{
    Context context;
    public GetUserAsyncTask(Context context) {
        this.context = context;
    }

    protected User[] doInBackground(Void... voids) {

        return AppDatabase.getAppDatabase(context).userDao().findAll();
    }
}
