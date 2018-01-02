package app.paypro.payproapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import app.paypro.payproapp.db.entity.Account;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by rogerbaiget on 16/11/17.
 */

@Dao
public interface AccountDao {

    @Query("SELECT * FROM account")
    Account[] findAll();

//    @Query("SELECT * FROM account")
//    LiveData<List<Account>> findAllLiveData();

    @Insert(onConflict = REPLACE)
    void insert(Account account);

    @Update
    void update(Account account);

    @Query("DELETE FROM account")
    void deleteAll();

}
