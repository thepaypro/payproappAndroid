package app.paypro.payproapp.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by rogerbaiget on 16/11/17.
 */

@Entity
public class User {

    public User(int uid, String username){
        this.uid = uid;
        this.username = username;
    }

    @PrimaryKey(autoGenerate = false)
    private int uid;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "nickname")
    private String nickname;

    @ColumnInfo(name = "account_id")
    private int account_id;

    @ColumnInfo(name = "avatar")
    private String avatar = "";

    public int getUid()
    {
        return uid;
    }

    public void setUid(int uidValue)
    {
        this.uid = uidValue;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String usernameValue){
        this.username = usernameValue;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nicknameValue)
    {
        this.nickname = nicknameValue;
    }

    public int getAccount_id()
    {
        return account_id;
    }

    public void setAccount_id(int account_idValue)
    {
        this.account_id = account_idValue;
    }

    public String getAvatar() { return this.avatar; }

    public void setAvatar(String avatarValue)
    {
        this.avatar = avatarValue;
    }
}

