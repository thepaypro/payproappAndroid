package app.paypro.payproapp.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by rogerbaiget on 17/11/17.
 */

@Entity
public class Transaction {

    public Transaction(int uid, Boolean payer, int amount, Date date){
        this.uid = uid;
        this.payer = payer;
        this.amount = amount;
        this.date = date;
    }

    @PrimaryKey(autoGenerate = false)
    private int uid;

    @ColumnInfo(name = "payer")
    private Boolean payer;

    @ColumnInfo(name = "amount")
    private int amount;

    @ColumnInfo(name = "subject")
    private String subject;

    @ColumnInfo(name = "address_to")
    private String addressTo;

    @ColumnInfo(name = "created_at")
    private Date date;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Boolean getPayer() {
        return payer;
    }

    public void setPayer(Boolean payer) {
        this.payer = payer;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
