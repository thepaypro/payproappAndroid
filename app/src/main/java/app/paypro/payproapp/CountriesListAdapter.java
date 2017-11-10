package app.paypro.payproapp;

/**
 * Created by rogerbaiget on 9/11/17.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import android.widget.AlphabetIndexer;

import java.util.ArrayList;

public class CountriesListAdapter extends RecyclerView.Adapter<CountriesListAdapter.ViewHolder> implements Filterable, SectionIndexer {

    private ArrayList<Country>  data;
    private ArrayList<Country> filtered_data;
    AlphabetIndexer mAlphabetIndexer;

    public CountriesListAdapter(ArrayList<Country> myDataset) {
        data = myDataset;
        filtered_data = myDataset;
    }

    @Override
    public CountriesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CountriesListAdapter.ViewHolder holder, int position) {

        Country country = filtered_data.get(position);

        holder.mTextView.setText(country.getName().trim() + " (+" + country.getCallingCodes().trim() + ")");
        holder.mTextView.getRootView().setTag(country);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filtered_data.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    filtered_data = data;
                } else {

                    ArrayList<Country> filteredList = new ArrayList<>();

                    for (Country dataObject : data) {

                        if (dataObject.getCallingCodes().toLowerCase().contains(charString) || dataObject.getName().toLowerCase().contains(charString)) {
                            filteredList.add(dataObject);
                        }
                    }

                    filtered_data = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered_data;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filtered_data = (ArrayList<Country>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public Object[] getSections() {
        return mAlphabetIndexer.getSections();
    }

    @Override
    public int getPositionForSection(int i) {
        return mAlphabetIndexer.getPositionForSection(i);
    }

    @Override
    public int getSectionForPosition(int i) {
        return mAlphabetIndexer.getSectionForPosition(i);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.txtViewCountryName);
        }
    }


}
