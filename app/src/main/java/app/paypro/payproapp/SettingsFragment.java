package app.paypro.payproapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.concurrent.ExecutionException;

import app.paypro.payproapp.asynctask.db.user.GetUserAsyncTask;
import app.paypro.payproapp.utils.CropImage;

public class SettingsFragment extends Fragment {
    private static final String IMAGE_DIRECTORY = "/PayPro", AVATAR_FILENAME = "payproprofile.jpg";

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

        //Avatar
        File profile = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY+"/"+AVATAR_FILENAME);

        Bitmap bitmap;

        if (profile.exists() && profile.canRead()){
            bitmap = BitmapFactory.decodeFile(profile.getPath());
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile);
        }

        avatarImage.setImageBitmap(CropImage.transform(bitmap));

        try {
            app.paypro.payproapp.db.entity.User userEntity = new GetUserAsyncTask(getContext()).execute().get()[0];
            TextView nicknameSettings = getView().findViewById(R.id.nicknameSettings);

            if (userEntity.getNickname().toString().isEmpty()){
                nicknameSettings.setText("Nickname");
            } else {
                nicknameSettings.setText(userEntity.getNickname().toString());
            }

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
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                transaction.replace(R.id.frame_layout, new ProfileView());
                transaction.commit();

            }
        } );

        TableRow rowPasscode;
        rowPasscode = getView().findViewById(R.id.rowPasscode);
        rowPasscode.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent(getActivity(), PasscodeActivity.class);
                intent.putExtra("passcode_state", "old");
                startActivity(intent);
            }
        } );

        TableRow rowTell;
        rowTell = getView().findViewById(R.id.rowTell);
        rowTell.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                PackageManager pm = getActivity().getPackageManager();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.sms_msg));
                sendIntent.setType("text/plain");
                try {
                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    sendIntent.setPackage("com.whatsapp");
                } catch (PackageManager.NameNotFoundException e) {
                    // SHOW ALL MSG APPS
                }
                startActivity(sendIntent);
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