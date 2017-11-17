package app.paypro.payproapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import app.paypro.payproapp.db.dao.AccountDao;
import app.paypro.payproapp.db.dao.TransactionDao;
import app.paypro.payproapp.db.dao.UserDao;
import app.paypro.payproapp.db.entity.Account;
import app.paypro.payproapp.db.entity.Transaction;
import app.paypro.payproapp.db.entity.User;

/**
 * Created by rogerbaiget on 16/11/17.
 */

@Database(entities = {Account.class, User.class, Transaction.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract AccountDao accountDao();

    public abstract UserDao userDao();

    public abstract TransactionDao transactionDao();

    public static AppDatabase getAppDatabase(Context context){
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"paypro-db").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
