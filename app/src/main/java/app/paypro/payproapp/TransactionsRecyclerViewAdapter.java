package app.paypro.payproapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import app.paypro.payproapp.db.entity.Transaction;

/**
 * Created by rogerbaiget on 20/11/17.
 */

public class TransactionsRecyclerViewAdapter  extends RecyclerView.Adapter<TransactionsRecyclerViewAdapter.ViewHolder>{

    private List<Transaction> transactionsList;

    public TransactionsRecyclerViewAdapter(List<Transaction> transactionsList){
        this.transactionsList = transactionsList;
    }

    @Override
    public TransactionsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(TransactionsRecyclerViewAdapter.ViewHolder holder, int position) {
        Transaction transaction = transactionsList.get(position);

        NumberFormat format = NumberFormat.getInstance(Locale.UK);
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        String amount = format.format(transaction.getAmount());
        String signal = "";
        String title;
        if(transaction.getPayer()){
            signal = "-";
            title = "Transfer out ";
        }else{
            title = "Transfer in ";
        }

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(transaction.getDate());

        try {
            holder.titleTextView.setText(title);
            holder.addressTextView.setText(transaction.getAddressTo());
            holder.signalTextView.setText(signal);
            holder.amountTextView.setText(amount);
            holder.subtitleTextView.setText(URLDecoder.decode(transaction.getSubject(), "UTF-8"));
            holder.dateTextView.setText(date);
            holder.itemView.setTag(transaction);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    public void addItems(List<Transaction> transactionsList){
        this.transactionsList = sortList(transactionsList);
        notifyDataSetChanged();
    }

    List<Transaction> sortList(List<Transaction> list) {
        Collections.sort(list, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction transaction1, Transaction transaction2) {
                if (transaction1.getDate().before(transaction2.getDate())) {
                    return 1;
                } else if (transaction1.getDate().after(transaction2.getDate())) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView addressTextView;
        private TextView amountTextView;
        private TextView signalTextView;
        private TextView subtitleTextView;
        private TextView dateTextView;

        ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.title_text);
            addressTextView = (TextView) view.findViewById(R.id.address_text);
            amountTextView = (TextView) view.findViewById(R.id.amount_text);
            signalTextView = (TextView) view.findViewById(R.id.amount_sign) ;
            subtitleTextView = (TextView) view.findViewById(R.id.subtitle_text);
            dateTextView = (TextView) view.findViewById(R.id.date_text);
        }
    }
}

