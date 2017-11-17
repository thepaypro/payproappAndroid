package app.paypro.payproapp.asynctask.db.user;

import android.content.Context;
import android.os.AsyncTask;

import app.paypro.payproapp.db.AppDatabase;
import app.paypro.payproapp.db.entity.User;

/**
 * Created by rogerbaiget on 16/11/17.
 */

public class GetUserByIdAsyncTask extends AsyncTask<Integer, Void, User> {
    Context context;
    public GetUserByIdAsyncTask(Context context) {
        this.context = context;
    }

    protected User doInBackground(Integer... ids) {

        return AppDatabase.getAppDatabase(context).userDao().findById(ids[0].intValue());
    }
}
