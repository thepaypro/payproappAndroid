package app.paypro.payproapp.asynctask.db.user;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import app.paypro.payproapp.db.AppDatabase;
import app.paypro.payproapp.db.entity.Account;

/**
 * Created by rogerbaiget on 17/11/17.
 */

public class SaveAccountAsyncTask extends AsyncTask<Account, Void, Void> {
    Context context;
    public SaveAccountAsyncTask(Context context) {
        this.context = context;
    }

    protected Void doInBackground(Account... accounts) {
        AppDatabase.getAppDatabase(context).accountDao().deleteAll();
        AppDatabase.getAppDatabase(context).transactionDao().deleteAll();
        AppDatabase.getAppDatabase(context).accountDao().insert(accounts[0]);
        return null;
    }
}
