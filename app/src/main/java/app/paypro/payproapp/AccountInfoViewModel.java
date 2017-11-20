//package app.paypro.payproapp;
//
//import android.app.Application;
//import android.arch.lifecycle.AndroidViewModel;
//import android.arch.lifecycle.LiveData;
//
//import java.util.List;
//
//import app.paypro.payproapp.db.AppDatabase;
//import app.paypro.payproapp.db.entity.Account;
//
///**
// * Created by rogerbaiget on 20/11/17.
// */
//
//public class AccountInfoViewModel extends AndroidViewModel {
//
//    private final LiveData<List<Account>> accountData;
//
//    private AppDatabase appDatabase;
//
//    public AccountInfoViewModel(Application application){
//        super(application);
//
//        appDatabase = AppDatabase.getAppDatabase(this.getApplication());
//
//        accountData = appDatabase.accountDao().findAllLiveData();
//
//    }
//
//    public LiveData<List<Account>> getAccountData(){
//        return accountData;
//    }
//
//}
