package app.paypro.payproapp.asynctask.db.user;

import android.content.Context;
import android.os.AsyncTask;


import app.paypro.payproapp.db.AppDatabase;
import app.paypro.payproapp.db.entity.Account;


/**
 * Created by rogerbaiget on 17/11/17.
 */

public class GetUserAccountAsyncTask extends AsyncTask<Void, Void, Account[]> {
    Context context;
    public GetUserAccountAsyncTask(Context context) {
        this.context = context;
    }

    protected Account[] doInBackground(Void... voids) {

        return AppDatabase.getAppDatabase(context).userDao().findUserAccount();
    }
}
