package app.paypro.payproapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import app.paypro.payproapp.asynctask.QRBitmapGeneratorAsyncTask;

/**
 * Created by rogerbaiget on 22/11/17.
 */

public class ShowQRCodeFragment extends Fragment {

    private String addr;

    public static ShowQRCodeFragment newInstance() {
        ShowQRCodeFragment fragment = new ShowQRCodeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.addr  = bundle.getString("addr");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_show_qr, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView toolbarTitle = getActivity().findViewById(R.id.app_toolbar_title);
        toolbarTitle.setText("");

        TextView toolbar_back_button_text = getActivity().findViewById(R.id.app_toolbar_back_button_text);
        toolbar_back_button_text.setText(getResources().getString(R.string.title_account));
        toolbar_back_button_text.setVisibility(View.VISIBLE);

        ImageButton toolbar_back_button_image = getActivity().findViewById(R.id.app_toolbar_back_button_image);
        toolbar_back_button_image.setVisibility(View.VISIBLE);
        toolbar_back_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
//                transaction.replace(R.id.frame_layout, AccountFragment.newInstance());
//                transaction.commit();
                getFragmentManager().popBackStack();
            }
        });

        ImageView qrImageView = getView().findViewById(R.id.qr_code);
        ProgressBar progressBar = getView().findViewById(R.id.progress_bar);

        QRBitmapGeneratorAsyncTask qrBitmapGeneratorAsyncTask = new QRBitmapGeneratorAsyncTask(qrImageView,progressBar);
        qrBitmapGeneratorAsyncTask.execute("bitcoin:"+addr);

    }

}
