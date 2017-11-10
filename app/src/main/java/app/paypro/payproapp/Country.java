package app.paypro.payproapp;

import android.widget.SearchView;

/**
 * Created by rogerbaiget on 10/11/17.
 */

public class Country {

    private String name;
    private String callingCodes;
    private String alpha2Code;


    public Country(String nameValue, String callingCodesValue, String alpha2CodeValue){
        this.name = nameValue;
        this.callingCodes = callingCodesValue;
        this.alpha2Code = alpha2CodeValue;
    }
    public String getName(){
        return name;
    }

    public String getCallingCodes(){
        return callingCodes;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }
}
