package app.paypro.payproapp;

import java.util.ArrayList;

/**
 * Created by rogerbaiget on 24/11/17.
 */

public class Contact{

    private String imageURi = "";
    private String name = "";
    private String backFullName = "";
    private String id = "";
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }





    public Contact(String id, String name, String imageURi, ArrayList<String> numbers){

        this.id = id;
        this.name = name;
        this.imageURi = imageURi;
        this.numbers = numbers;
    }


}
