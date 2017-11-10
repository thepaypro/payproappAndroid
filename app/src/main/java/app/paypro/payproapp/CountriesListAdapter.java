package app.paypro.payproapp;

/**
 * Created by rogerbaiget on 9/11/17.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;

public class CountriesListAdapter extends RecyclerView.Adapter<CountriesListAdapter.ViewHolder> implements Filterable {
    private ArrayList<JSONObject>  data;
    private ArrayList<JSONObject> filtered_data;


    // Provide a suitable constructor (depends on the kind of dataset)
    public CountriesListAdapter(ArrayList<JSONObject> myDataset) {
        data = myDataset;
        filtered_data = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CountriesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_list_item, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CountriesListAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        try {
            JSONObject jo_inside = filtered_data.get(position);
            String callingCodes_value = jo_inside.getString("callingCodes");
            String name_value = jo_inside.getString("name");

            holder.mTextView.setText(name_value.trim() + " (+" + callingCodes_value.trim() + ")");

        }catch (JSONException e) {
            e.printStackTrace();
        }
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

                try{
                    if (charString.isEmpty()) {
                        filtered_data = data;
                    } else {

                        ArrayList<JSONObject> filteredList = new ArrayList<>();

                        for (JSONObject dataObject : data) {

                            if (dataObject.getString("callingCodes").toLowerCase().contains(charString) || dataObject.getString("name").toLowerCase().contains(charString)) {
                                filteredList.add(dataObject);
                            }
                        }

                        filtered_data = filteredList;
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered_data;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filtered_data = (ArrayList<JSONObject>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.txtViewCountryName);
        }
    }


}
