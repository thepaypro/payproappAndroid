package app.paypro.payproapp.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import app.paypro.payproapp.db.entity.Account;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by rogerbaiget on 16/11/17.
 */

@Dao
public interface AccountDao {

    @Query("SELECT * FROM account where uid like :id")
    Account findAll(int id);

    @Insert(onConflict = REPLACE)
    void insert(Account account);

    @Update
    void update(Account account);

}
