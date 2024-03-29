package app.paypro.payproapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import app.paypro.payproapp.global.Global;
import app.paypro.payproapp.utils.CropImage;

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
        private ImageView ppLogo;
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Contact item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.contacts_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.name =  convertView.findViewById(R.id.contact_name);
            viewHolder.photo = convertView.findViewById(R.id.contact_photo);
            viewHolder.number = convertView.findViewById(R.id.contact_number);
            viewHolder.ppLogo = convertView.findViewById(R.id.pp_logo);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (item!= null) {
            Bitmap bitmapImage = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.default_profile);
            if(item.getImageURi() != null){
                try {
                    Bitmap bitmapUser = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(item.getImageURi()));
                    bitmapImage = getResizedBitmap(bitmapUser, 100, 100); //pick you default size
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


//            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), bitmapImage);
//            roundedBitmapDrawable.setAntiAlias(true);
//            roundedBitmapDrawable.setCircular(true);
//            viewHolder.photo.setImageDrawable(roundedBitmapDrawable);

            viewHolder.photo.setImageBitmap(CropImage.transform(bitmapImage));

            Bitmap bitmapLogo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.logo_alone);
//            roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), bitmapLogo);
//            roundedBitmapDrawable.setCircular(true);
//            viewHolder.ppLogo.setImageDrawable(roundedBitmapDrawable);

            viewHolder.ppLogo.setImageBitmap(CropImage.transform(bitmapLogo));

            if (item.getIsUser()) {
                viewHolder.ppLogo.setVisibility(View.VISIBLE);
            }else{
                viewHolder.ppLogo.setVisibility(View.GONE);
            }

            viewHolder.name.setText(item.getName());
            viewHolder.number.setText(item.getNumbers().get(0));

        }

        return convertView;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        int     width           = bm.getWidth();
        int     height          = bm.getHeight();
        float   scaleWidth      = ((float) newWidth) / width;
        float   scaleHeight     = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();



                    ArrayList<Contact> filteredList = new ArrayList<>();

                    for (Contact dataObject : data) {

                        if (dataObject.getName().toLowerCase().contains(charString) || dataObject.getNumbers().get(0).toLowerCase().contains(charString)) {
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