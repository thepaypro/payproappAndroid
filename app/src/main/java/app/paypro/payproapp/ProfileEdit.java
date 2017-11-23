package app.paypro.payproapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

        Button doneButton;
        doneButton = getView().findViewById(R.id.buttonDoneProfileView);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                try {
                    TextView nickname = getView().findViewById(R.id.inputNickname);

                    JSONObject parameters = new JSONObject();
                    parameters.put("nickname", nickname.getText().toString());

                    app.paypro.payproapp.user.User.updateInfo(getContext(), parameters, new ResponseListener<JSONObject>() {
                        @Override
                        public void getResult(JSONObject object) throws JSONException {
                            try {
                                if(object.getBoolean("status")){
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                    ProfileView myfragment = new ProfileView();

                                    fragmentTransaction.replace(R.id.frame_layout, myfragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }else if (!object.getBoolean("status") && object.has("error_msg")){
                                    PPSnackbar.getSnackbar(view,object.getString("error_msg")).show();
                                }else{
                                    PPSnackbar.getSnackbar(view,"Error").show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                PPSnackbar.getSnackbar(view,"Error").show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    PPSnackbar.getSnackbar(view,"Error").show();
                }
            }
        });
    }
}
