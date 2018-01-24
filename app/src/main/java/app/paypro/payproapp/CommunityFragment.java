package app.paypro.payproapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by kike on 19/12/17.
 */

public class CommunityFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.community_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView toolbarTitle = getActivity().findViewById(R.id.app_toolbar_title);
        toolbarTitle.setText("Community");

        TextView toolbar_back_button_text = getActivity().findViewById(R.id.app_toolbar_back_button_text);
        toolbar_back_button_text.setText("Info");
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
//                transaction.replace(R.id.frame_layout, new InfoFragment());
//                transaction.commit();
                getFragmentManager().popBackStack();
                ((TabActivity) getActivity()).hideVirtualKeyboard();
            }
        });

        WebView webView = getView().findViewById(R.id.webViewCommunity);
        webView.loadUrl("http://payproapp.boards.net/");
    }
}
