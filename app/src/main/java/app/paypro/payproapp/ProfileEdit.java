package app.paypro.payproapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import app.paypro.payproapp.asynctask.db.user.GetUserAsyncTask;
import app.paypro.payproapp.http.ResponseListener;
import app.paypro.payproapp.utils.PPSnackbar;

/**
 * Created by kike on 22/11/17.
 */

public class ProfileEdit extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_edit, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            app.paypro.payproapp.db.entity.User userEntity = new GetUserAsyncTask(getContext()).execute().get()[0];
            TextView inputNickname = getView().findViewById(R.id.inputNickname);
            inputNickname.setText(userEntity.getNickname().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ImageButton backButton = getView().findViewById(R.id.backButtonToolbarProfileEdit);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.frame_layout, new ProfileView());
                transaction.commit();
            }
        });

        final Button doneButton;
        doneButton = getView().findViewById(R.id.buttonDoneProfileView);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                try {
                    doneButton.setVisibility(View.INVISIBLE);

                    final ProgressBar progressBar = getView().findViewById(R.id.progressBarProfileView);
                    progressBar.setVisibility(View.VISIBLE);

                    final TextView nickname = getView().findViewById(R.id.inputNickname);
                    nickname.setCursorVisible(false);

                    InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(
                            getContext().INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(nickname.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    JSONObject parameters = new JSONObject();
                    parameters.put("nickname", nickname.getText().toString());

                    app.paypro.payproapp.user.User.updateInfo(getContext(), parameters, new ResponseListener<JSONObject>() {
                        @Override
                        public void getResult(JSONObject object) throws JSONException {
                            try {
                                if(object.getBoolean("status")){
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                                    transaction.replace(R.id.frame_layout, new ProfileView());
                                    transaction.commit();

                                }else if (!object.getBoolean("status") && object.has("error_msg")){
                                    doneButton.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    PPSnackbar.getSnackbar(view,getContext(),object.getString("error_msg")).show();
                                }else{
                                    doneButton.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    PPSnackbar.getSnackbar(view,getContext(),"error_save").show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                doneButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                PPSnackbar.getSnackbar(view,getContext(),"Error").show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    PPSnackbar.getSnackbar(view,getContext(),"Error").show();
                }
            }
        });
    }
}
