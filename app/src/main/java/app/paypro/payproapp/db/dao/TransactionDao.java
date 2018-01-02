package app.paypro.payproapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import app.paypro.payproapp.db.entity.Transaction;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by rogerbaiget on 17/11/17.
 */
@Dao
public interface TransactionDao {

    @Query("SELECT * FROM `transaction`")
    LiveData<List<Transaction>> findAll();

    @Insert(onConflict = REPLACE)
    void insert(Transaction[] transactions);

    @Query("DELETE FROM `transaction`")
    void deleteAll();
}
