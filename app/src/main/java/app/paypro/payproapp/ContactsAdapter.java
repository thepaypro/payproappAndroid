package app.paypro.payproapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rogerbaiget on 24/11/17.
 */

public class ContactsAdapter extends ArrayAdapter<Contact> implements Filterable {

    private ArrayList<Contact>  data;
    private ArrayList<Contact> filtered_data;

    private class ViewHolder {
        private TextView name;
        private ImageView photo;
        private TextView number;
    }

    public ContactsAdapter(Context context, int textViewResourceId, ArrayList<Contact> items) {
        super(context, textViewResourceId, items);
        data = new ArrayList<Contact>(items);
        filtered_data = items;
    }

    @Override
    public int getCount() {
        return filtered_data.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.contacts_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.name =  convertView.findViewById(R.id.contact_name);
            viewHolder.photo = convertView.findViewById(R.id.contact_photo);
            viewHolder.number = convertView.findViewById(R.id.contact_number);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Contact item = getItem(position);
        if (item!= null) {
            if(item.getImageURi() == null){
                viewHolder.photo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.default_profile));
            }else{
                viewHolder.photo.setImageURI(Uri.parse(item.getImageURi()));
            }
            viewHolder.name.setText(item.getName());
            viewHolder.number.setText(item.getNumber());
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();



                    ArrayList<Contact> filteredList = new ArrayList<>();

                    for (Contact dataObject : data) {

                        if (dataObject.getName().toLowerCase().startsWith(charString) || dataObject.getNumber().toLowerCase().contains(charString)) {
                            filteredList.add(dataObject);
                        }
                    }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filtered_data.clear();
                filtered_data.addAll((ArrayList<Contact>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }
}