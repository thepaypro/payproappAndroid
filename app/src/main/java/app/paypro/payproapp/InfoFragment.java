package app.paypro.payproapp;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by kike on 19/12/17.
 */

public class InfoFragment extends Fragment {
    View v;
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.info_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView toolbarTitle = getActivity().findViewById(R.id.app_toolbar_title);
        toolbarTitle.setText("Info");

        TextView toolbar_back_button_text = getActivity().findViewById(R.id.app_toolbar_back_button_text);
        toolbar_back_button_text.setText("Settings");
        toolbar_back_button_text.setVisibility(View.VISIBLE);

        Button confirmButton = getActivity().findViewById(R.id.app_toolbar_confirm_button);
        confirmButton.setVisibility(View.INVISIBLE);

        ProgressBar progressBar = getActivity().findViewById(R.id.app_toolbar_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        ImageButton toolbar_back_button_image = getActivity().findViewById(R.id.app_toolbar_back_button_image);
        toolbar_back_button_image.setVisibility(View.VISIBLE);
        toolbar_back_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
//                transaction.replace(R.id.frame_layout, new SettingsFragment());
//                transaction.commit();
                getFragmentManager().popBackStack();
                ((TabActivity) getActivity()).hideVirtualKeyboard();
            }
        });

        TableRow rowCommunity;
        rowCommunity = getView().findViewById(R.id.rowCommunity);
        rowCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                transaction.replace(R.id.frame_layout, new CommunityFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        TableRow rowTerms;
        rowTerms = getView().findViewById(R.id.rowTerms);
        rowTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                transaction.replace(R.id.frame_layout, new TermsFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
