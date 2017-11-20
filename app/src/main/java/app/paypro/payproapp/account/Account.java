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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by kike on 10/11/17.
 */

public class Account {

    public static void info(final Context context, final ResponseListener<JSONObject> listener) throws JSONException {

        PayProRequest.get(context, "accounts_info", "", new ResponseListener<JSONObject>() {
            @Override
            public void getResult(JSONObject object) throws JSONException {
                Log.i("accountinfo", object.toString());
                if (object.has("info"))
                {
                    JSONObject infoJSON = object.getJSONObject("info");

                    try {
                        app.paypro.payproapp.db.entity.Account accountEntity = new GetAccountAsyncTask(context).execute().get()[0];
                        accountEntity.setBalance(infoJSON.getInt("bitcoinBalance"));
                        new UpdateAccountAsyncTask(context).execute(accountEntity);

                        if (!infoJSON.isNull("bitcoinTransactions")) {
                            JSONArray bitcoinTransactions = infoJSON.getJSONArray("bitcoinTransactions");
                            List<Transaction> transactionsList = null;

                            for (int i = 0, size = bitcoinTransactions.length(); i < size; i++) {
                                Boolean payer = !((JSONObject) bitcoinTransactions.get(i)).isNull("payer");

                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSSSSS,", Locale.ENGLISH);
                                Date date = format.parse(((JSONObject) bitcoinTransactions.get(i)).getJSONObject("createdAt").getString("date"));

                                Transaction transaction = new Transaction(((JSONObject) bitcoinTransactions.get(i)).getInt("id"), payer, ((JSONObject) bitcoinTransactions.get(i)).getInt("amount"), date);
                                transactionsList.add(transaction);
                            }
                            Transaction[] transactionsArray = new Transaction[transactionsList.size()];
                            transactionsArray = transactionsList.toArray(transactionsArray);
                            new SaveTransactionsAsyncTask(context).execute(transactionsArray);
                        }
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
                    errorResponse.put("status", false);
                    listener.getResult(errorResponse);
                }
            }
        });
    }
}
