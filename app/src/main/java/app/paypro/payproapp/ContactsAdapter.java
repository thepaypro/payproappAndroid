package app.paypro.payproapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rogerbaiget on 24/11/17.
 */

public class ContactsAdapter extends ArrayAdapter<Contact> {

    private class ViewHolder {
        private TextView name;
        private ImageView photo;
        private TextView number;
    }

    public ContactsAdapter(Context context, int textViewResourceId, ArrayList<Contact> items) {
        super(context, textViewResourceId, items);
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
}