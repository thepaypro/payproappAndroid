package app.paypro.payproapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import app.paypro.payproapp.asynctask.db.user.GetUserAsyncTask;

/**
 * Created by kike on 22/11/17.
 */

public class ProfileView extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageView avatarImage;

        avatarImage = getView().findViewById(R.id.avatarProfileView);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);

        avatarImage.setImageDrawable(roundedBitmapDrawable);

        try {
            app.paypro.payproapp.db.entity.User userEntity = new GetUserAsyncTask(getContext()).execute().get()[0];
            TextView nicknameProfileView = getView().findViewById(R.id.nicknameProfileView);
            nicknameProfileView.setText(userEntity.getNickname().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ImageButton backButton = getView().findViewById(R.id.backButtonToolbar);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment myfragment = new SettingsFragment();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.frame_layout, myfragment);
                transaction.commit();
            }
        });

        TableRow rowProfile;
        rowProfile = getView().findViewById(R.id.rowNickname);
        rowProfile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                ProfileEdit myfragment = new ProfileEdit();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.add(R.id.frame_layout, myfragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        } );
    }
}
