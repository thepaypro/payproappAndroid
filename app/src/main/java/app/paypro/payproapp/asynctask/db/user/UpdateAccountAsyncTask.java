package app.paypro.payproapp.asynctask.db.user;

import android.content.Context;
import android.os.AsyncTask;

import app.paypro.payproapp.db.AppDatabase;
import app.paypro.payproapp.db.entity.Account;
import app.paypro.payproapp.db.entity.Transaction;

/**
 * Created by rogerbaiget on 17/11/17.
 */

public class UpdateAccountAsyncTask extends AsyncTask<Account, Void, Void>{
    Context context;
    public UpdateAccountAsyncTask(Context context) {
        this.context = context;
    }

    protected Void doInBackground(Account... accounts) {
        AppDatabase.getAppDatabase(context).accountDao().update(accounts[0]);
        return null;
    }
}
