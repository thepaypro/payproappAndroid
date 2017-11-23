package app.paypro.payproapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import app.paypro.payproapp.asynctask.db.user.GetUserAsyncTask;

public class SettingsFragment extends Fragment {

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

        ImageView avatarImage;

        avatarImage = getView().findViewById(R.id.avatarImage);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);

        avatarImage.setImageDrawable(roundedBitmapDrawable);

        try {
            app.paypro.payproapp.db.entity.User userEntity = new GetUserAsyncTask(getContext()).execute().get()[0];
            TextView nicknameSettings = getView().findViewById(R.id.nicknameSettings);
            nicknameSettings.setText(userEntity.getNickname().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        TableRow rowProfile;
        rowProfile = getView().findViewById(R.id.rowProfile);
        rowProfile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                ProfileView myfragment = new ProfileView();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.add(R.id.frame_layout, myfragment);
                transaction.addToBackStack(null);
                transaction.commit();
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