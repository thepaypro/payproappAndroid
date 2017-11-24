package app.paypro.payproapp;

/**
 * Created by rogerbaiget on 24/11/17.
 */

public class Contact{

    private String imageURi = "";
    private String name = "";
    private String id = "";
    private String number = "";

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Contact(String id, String name, String imageURi, String number){

        this.id = id;
        this.name = name;
        this.imageURi = imageURi;
        this.number = number;
    }
}
