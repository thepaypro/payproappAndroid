package app.paypro.payproapp.account;

import android.content.Context;
import android.util.Log;

import app.paypro.payproapp.asynctask.db.user.GetAccountAsyncTask;
import app.paypro.payproapp.asynctask.db.user.SaveTransactionsAsyncTask;
import app.paypro.payproapp.asynctask.db.user.UpdateAccountAsyncTask;
import app.paypro.payproapp.db.entity.Transaction;
import app.paypro.payproapp.http.PayProRequest;
import app.paypro.payproapp.http.ResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static app.paypro.payproapp.WelcomeActivity.getContext;

/**
 * Created by kike on 10/11/17.
 */

public class Account {

    public static void info(final Context context, final ResponseListener<JSONObject> listener) throws JSONException, ExecutionException, InterruptedException {
        String parameters = "";

        app.paypro.payproapp.db.entity.Account account = new GetAccountAsyncTask(getContext()).execute().get()[0];
        if(account.getLast_synced_transaction_id() != null){
            parameters = "bitcoinTransactionId="+account.getLast_synced_transaction_id();
        }

        PayProRequest.get(context, "accounts_info", parameters, new ResponseListener<JSONObject>() {
            @Override
            public void getResult(JSONObject object) throws JSONException {
                if (object.has("info")){
                    try {
                        JSONObject infoJSON = object.getJSONObject("info");
                        Integer lastSyncedTransactionId = null;
                        app.paypro.payproapp.db.entity.Account account = new GetAccountAsyncTask(getContext()).execute().get()[0];
                        if(account.getLast_synced_transaction_id() != null){
                            lastSyncedTransactionId  = account.getLast_synced_transaction_id();
                        }

                        if (!infoJSON.isNull("bitcoinTransactions")) {
                            JSONArray bitcoinTransactions = infoJSON.getJSONObject("bitcoinTransactions").getJSONArray("content");
                            List<Transaction> transactionsList = new ArrayList<Transaction>();

                            for (int i = 0, size = bitcoinTransactions.length(); i < size; i++) {
                                Boolean payer = !((JSONObject) bitcoinTransactions.get(i)).isNull("payer");

                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSSSSS");
                                String transactionDate = ((JSONObject) bitcoinTransactions.get(i)).getJSONObject("createdAt").getString("date");
                                Date date = format.parse(transactionDate);

                                Transaction transaction = new Transaction(((JSONObject) bitcoinTransactions.get(i)).getInt("id"), payer, ((JSONObject) bitcoinTransactions.get(i)).getInt("amount"), date);

                                if(!((JSONObject) bitcoinTransactions.get(i)).isNull("subject")){
                                    transaction.setSubject(((JSONObject) bitcoinTransactions.get(i)).getString("subject"));
                                }
                                if(!((JSONObject) bitcoinTransactions.get(i)).isNull("addressTo")){
                                    transaction.setAddressTo(((JSONObject) bitcoinTransactions.get(i)).getString("addressTo"));
                                }
                                lastSyncedTransactionId = transaction.getUid();
                                transactionsList.add(transaction);
                            }
                          
                            if(transactionsList.size() > 0){
                                Transaction[] transactionsArray = new Transaction[transactionsList.size()];
                                transactionsArray = transactionsList.toArray(transactionsArray);
                                new SaveTransactionsAsyncTask(context).execute(transactionsArray);
                            }
                        }
                        account.setBalance(infoJSON.getInt("bitcoinBalance"));
                        account.setLast_synced_transaction_id(lastSyncedTransactionId);
                        new UpdateAccountAsyncTask(context).execute(account);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    JSONObject responseJSON = new JSONObject();
                    responseJSON.put("status", true);
                    responseJSON.put("info", object.getString("info"));
                    listener.getResult(responseJSON);
                } else {
                    JSONObject errorResponse = new JSONObject();
                    if(object.has("error_msg")){
                        errorResponse.put("error_msg", object.getString("error_msg"));
                    }
                    if (object.has("errorMessage")){
                        errorResponse.put("error_msg",object.getString("errorMessage"));
                    }
                    errorResponse.put("status", false);
                    listener.getResult(errorResponse);
                }
            }
        });
    }
}
