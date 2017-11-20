package app.paypro.payproapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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

        String amount = String.valueOf(transaction.getAmount());
        String title = "";
        if(transaction.getPayer()){
            amount = "-"+amount;
            title = "Transfer out ";
        }else{
            title = "Transfer in ";
        }


        holder.titleTextView.setText(title);
        holder.addressTextView.setText(transaction.getAddressTo());
        holder.amountTextView.setText(amount);
        holder.subtitleTextView.setText(transaction.getSubject());
        holder.dateTextView.setText(transaction.getDate().toString());
        holder.itemView.setTag(transaction);

    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    public void addItems(List<Transaction> transactionsList){
        this.transactionsList = transactionsList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView addressTextView;
        private TextView amountTextView;
        private TextView subtitleTextView;
        private TextView dateTextView;

        ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.title_text);
            addressTextView = (TextView) view.findViewById(R.id.address_text);
            amountTextView = (TextView) view.findViewById(R.id.amount_text);
            subtitleTextView = (TextView) view.findViewById(R.id.subtitle_text);
            subtitleTextView = (TextView) view.findViewById(R.id.date_text);
        }
    }
}

