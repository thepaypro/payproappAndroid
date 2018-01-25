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

        TextView toolbarTitle = getActivity().findViewById(R.id.app_toolbar_title);
        toolbarTitle.setText("");

        TextView toolbar_back_button_text = getActivity().findViewById(R.id.app_toolbar_back_button_text);
        toolbar_back_button_text.setText("Profile");
        toolbar_back_button_text.setVisibility(View.VISIBLE);

        ImageButton toolbar_back_button_image = getActivity().findViewById(R.id.app_toolbar_back_button_image);
        toolbar_back_button_image.setVisibility(View.VISIBLE);
        toolbar_back_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
                ((TabActivity) getActivity()).hideVirtualKeyboard();
            }
        });

        try {
            app.paypro.payproapp.db.entity.User userEntity = new GetUserAsyncTask(getContext()).execute().get()[0];
            TextView inputNickname = getView().findViewById(R.id.inputNickname);
            inputNickname.setText(userEntity.getNickname().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final Button confirmButton = getActivity().findViewById(R.id.app_toolbar_confirm_button);
        confirmButton.setVisibility(View.VISIBLE);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                try {
                    confirmButton.setVisibility(View.INVISIBLE);

                    final ProgressBar progressBar = getActivity().findViewById(R.id.app_toolbar_progress_bar);
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
                                    confirmButton.setVisibility(View.INVISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
//                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                                    transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
//                                    transaction.replace(R.id.frame_layout, new ProfileView());
//                                    transaction.commit();
                                    getFragmentManager().popBackStack();
                                    ((TabActivity) getActivity()).hideVirtualKeyboard();

                                }else if (!object.getBoolean("status") && object.has("error_msg")){
                                    confirmButton.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    PPSnackbar.getSnackbar(view,getContext(),object.getString("error_msg")).show();
                                }else{
                                    confirmButton.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    PPSnackbar.getSnackbar(view,getContext(),"error_save").show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                confirmButton.setVisibility(View.VISIBLE);
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
