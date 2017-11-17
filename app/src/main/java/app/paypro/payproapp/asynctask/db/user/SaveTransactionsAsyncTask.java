package app.paypro.payproapp.asynctask.db.user;

import android.content.Context;
import android.os.AsyncTask;

import app.paypro.payproapp.db.AppDatabase;
import app.paypro.payproapp.db.entity.Transaction;
import app.paypro.payproapp.db.entity.User;

/**
 * Created by rogerbaiget on 17/11/17.
 */

public class SaveTransactionsAsyncTask extends AsyncTask<Transaction, Void, Void>{
    Context context;
    public SaveTransactionsAsyncTask(Context context) {
        this.context = context;
    }

    protected Void doInBackground(Transaction... transactions) {
        AppDatabase.getAppDatabase(context).transactionDao().insert(transactions);
        return null;
    }
}
