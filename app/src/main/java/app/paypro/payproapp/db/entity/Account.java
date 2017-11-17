package app.paypro.payproapp.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by rogerbaiget on 16/11/17.
 */

@Entity
public class Account {

    public Account(int uid, String address){
        this.uid = uid;
        this.address = address;
    }

    @PrimaryKey(autoGenerate = false)
    private int uid;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "balance")
    private int balance;

    @ColumnInfo(name = "last_synced_transaction_id")
    private String last_synced_transaction_id;

//    @Relation(parentColumn = "uid", entityColumn = "account_id", entity = User.class)
//    public List<User> user;

    public int getUid()
    {
        return uid;
    }

    public void setUid(int uidValue)
    {
        this.uid = uidValue;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String addressValue)
    {
        this.address = addressValue;
    }

    public int getBalance()
    {
        return balance;
    }

    public void setBalance(int balanceValue)
    {
        this.balance = balanceValue;
    }

    public String getLast_synced_transaction_id()
    {
        return last_synced_transaction_id;
    }

    public void setLast_synced_transaction_id(String last_synced_transaction_idValue)
    {
        this.last_synced_transaction_id = last_synced_transaction_idValue;
    }
}
