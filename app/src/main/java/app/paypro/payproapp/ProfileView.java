package app.paypro.payproapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import app.paypro.payproapp.asynctask.db.user.GetUserAsyncTask;
import app.paypro.payproapp.db.entity.User;
import app.paypro.payproapp.utils.CropImage;

/**
 * Created by kike on 22/11/17.
 */

public class ProfileView extends Fragment {
    private ImageView avatarImage;
    private static final String IMAGE_DIRECTORY = "/PayPro", AVATAR_FILENAME = "payproprofile.jpg";
    private int GALLERY = 1, CAMERA = 2, CROP = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_view, container, false);
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("which: ", String.valueOf(which));
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Log.i("aaaaaaabbbbbbbb","aaabbbb");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Log.i("cccccdddddd","cdccccdddd");
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            getActivity().getContentResolver(),
                            contentURI);
                    saveImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            saveImage(thumbnail);
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File profileDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!profileDirectory.exists()) {
            profileDirectory.mkdirs();
        }

        try {
            File f = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMAGE_DIRECTORY+"/"+AVATAR_FILENAME);
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();

            setAvatarImage();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        avatarImage = getView().findViewById(R.id.avatarProfileView);

        try {
            //Avatar
            setAvatarImage();

            //Nickname
            User userEntity = new GetUserAsyncTask(getContext()).execute().get()[0];

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
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.frame_layout, new SettingsFragment());
                transaction.commit();
            }
        });

        Button editAvatarButton = getView().findViewById(R.id.editAvatarButton);
        editAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        TableRow rowProfile;
        rowProfile = getView().findViewById(R.id.rowNickname);
        rowProfile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right);
                transaction.replace(R.id.frame_layout, new ProfileEdit());
                transaction.commit();
            }
        } );
    }

    private void setAvatarImage()
    {
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
    }
}
