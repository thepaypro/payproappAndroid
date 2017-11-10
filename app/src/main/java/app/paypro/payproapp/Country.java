package app.paypro.payproapp;

/**
 * Created by rogerbaiget on 10/11/17.
 */

public class Country {

    private String name;
    private String callingCodes;

    public Country(){

    }

    public Country(String nameValue, String callingCodesValue){
        this.name = nameValue;
        this.callingCodes = callingCodesValue;
    }
    public String getName(){
        return name;
    }

    public String getCallingCodes(){
        return callingCodes;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCallingCodes(String callingCodes){
        this.callingCodes = callingCodes;
    }
}
