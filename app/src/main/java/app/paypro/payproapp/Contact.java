package app.paypro.payproapp;

import java.util.ArrayList;

/**
 * Created by rogerbaiget on 24/11/17.
 */

public class Contact{

    private String imageURi = "";
    private String name = "";
    private String backFullName = "";
    private Integer userId;
    private Integer accountId;
    private Boolean isUser = false;
    private ArrayList<String> numbers;

    public String getBackFullName() {
        return backFullName;
    }

    public void setBackFullName(String backFullName) {
        this.backFullName = backFullName;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;
    }

    public Boolean getIsUser() {
        return isUser;
    }

    public void setIsUser(Boolean user) {
        isUser = user;
    }

    public String getImageURi() {
        return imageURi;
    }

    public void setImageURi(String imageURi) {
        this.imageURi = imageURi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }


    public Contact(String name, String imageURi, ArrayList<String> numbers){

        this.name = name;
        this.imageURi = imageURi;
        this.numbers = numbers;
    }


}
