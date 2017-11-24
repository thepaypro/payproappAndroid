package app.paypro.payproapp.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import app.paypro.payproapp.db.entity.User;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by rogerbaiget on 16/11/17.
 */

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    User[] findAll();

//    @Query("SELECT * FROM account WHERE user.account_id = account.uid")
//    Account[] findUserAccount();

    @Insert(onConflict = REPLACE)
    void insert(User user);

    @Update
    void update(User user);

}

