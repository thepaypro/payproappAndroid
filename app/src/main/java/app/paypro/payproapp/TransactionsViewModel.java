package app.paypro.payproapp;

        import android.app.Application;
        import android.arch.lifecycle.AndroidViewModel;
        import android.arch.lifecycle.LiveData;

        import java.util.List;

        import app.paypro.payproapp.db.AppDatabase;
        import app.paypro.payproapp.db.entity.Transaction;

/**
 * Created by rogerbaiget on 20/11/17.
 */

public class TransactionsViewModel extends AndroidViewModel {

    private final LiveData<List<Transaction>> transactionsData;

    private AppDatabase appDatabase;

    public TransactionsViewModel(Application application){
        super(application);

        appDatabase = AppDatabase.getAppDatabase(this.getApplication());

        transactionsData = appDatabase.transactionDao().findAll();

    }

    public LiveData<List<Transaction>> getTransactionsData(){
        return transactionsData;
    }

}

