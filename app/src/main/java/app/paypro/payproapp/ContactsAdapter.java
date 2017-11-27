package app.paypro.payproapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
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
            Bitmap bitmapImage = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.default_profile);
            if(item.getImageURi() != null){
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(item.getImageURi()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), bitmapImage);
            roundedBitmapDrawable.setCircular(true);
            viewHolder.photo.setImageDrawable(roundedBitmapDrawable);


//            BitmapShader shader;
//            shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//
//            Paint paint = new Paint();
//            paint.setAntiAlias(true);
//            paint.setShader(shader);
//
//            RectF rect = new RectF(0.0f, 0.0f, width, height);


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

                        if (dataObject.getName().toLowerCase().contains(charString) || dataObject.getNumber().toLowerCase().contains(charString)) {
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