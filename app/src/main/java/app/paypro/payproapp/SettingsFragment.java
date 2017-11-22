package app.paypro.payproapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;

public class SettingsFragment extends Fragment {

    private ImageView avatarImage;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.settings_tab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        avatarImage = getView().findViewById(R.id.avatarImage);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);

        avatarImage.setImageDrawable(roundedBitmapDrawable);

        TableRow rowProfile;
        rowProfile = getView().findViewById(R.id.rowProfile);
        rowProfile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Log.i("eeeeee", "0000000000");
            }
        } );

        TableRow rowPasscode;
        rowPasscode = getView().findViewById(R.id.rowPasscode);
        rowPasscode.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Log.i("eeeeee", "11111111111");
            }
        } );

        TableRow rowTell;
        rowTell = getView().findViewById(R.id.rowTell);
        rowTell.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Log.i("eeeeee", "2222222222");
            }
        } );

        TableRow rowInfo;
        rowInfo = getView().findViewById(R.id.rowInfo);
        rowInfo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Log.i("eeeeee", "33333333333");
            }
        } );
    }
}