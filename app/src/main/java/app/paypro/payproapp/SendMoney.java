package app.paypro.payproapp;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

/**
 * Created by rogerbaiget on 28/11/17.
 */

public class SendMoney {

    private BigDecimal amount;
    private String address;
    private String message;
    private String label;
    private Integer userId;
    private Integer accountId;

    private DecimalFormatSymbols decimalFormatSymbols;
    private DecimalFormat decimalFormat;

    public SendMoney(){

        // Create a DecimalFormat that fits your requirements
        decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator(',');
        decimalFormatSymbols.setDecimalSeparator('.');
        String pattern = "#,##0.0#";

        decimalFormat = new DecimalFormat(pattern, decimalFormatSymbols);
        decimalFormat.setParseBigDecimal(true);
    }

    public String getFormatAmount() {
        return decimalFormat.format(amount);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setAmount(String amount) throws ParseException {
        this.amount = (BigDecimal) decimalFormat.parse(amount);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public Boolean bitcoinURISaveData(String bitcoinUriString){

//        bitcoin:175tWpb8K1S7NmH4Zx6rewF9WQrcZv245W?amount=50&label=Luke-Jr&message=Donation%20for%20project%20xyz

       String[] bitcoinUriScheme = bitcoinUriString.split(":");
       String bitcoinUri = "";


       try {

           if(bitcoinUriScheme.length == 2 ){
               if(bitcoinUriScheme[0].equals("bitcoin") && bitcoinUriScheme[1] != null){
                   bitcoinUri = bitcoinUriScheme[1];
               }
           }else{
               return false;
           }

           String[] bitcoinUriAddr = bitcoinUri.split("\\?");
           if(bitcoinUriAddr.length > 2 || !bitcoinUriAddr[0].matches("^[13][a-km-zA-HJ-NP-Z0-9]{26,33}$")){
               return false;
           }

           setAddress(bitcoinUriAddr[0]);
           if(bitcoinUriAddr.length == 2){
               String[] bitcoinUriParams = bitcoinUriAddr[1].split("&");
               for(int i=0;i<bitcoinUriParams.length;i++){
                   String[] param = bitcoinUriParams[i].split("=");
                   if(param.length == 2){
                       if(param[0].equals("amount") || param[0].equals("size")){
                           if(param[1].matches("^[0-9]*\\.?[0-9]{0,8}$")){
                               setAmount(param[1]);
                           }else{
                                return false;
                           }
                       }else if(param[0].equals("label")){
                           setLabel(URLDecoder.decode(param[1], "UTF-8"));
                       }else if(param[0].equals("message")){
                            setMessage(message = URLDecoder.decode(param[1], "UTF-8"));
                       }else{
                           return false;
                       }
                   }else{
                       return false;
                   }
               }
           }else{
               return true;
           }
           return true;
       } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
       } catch (ParseException e) {
           e.printStackTrace();
           return false;
       }
    }
}
