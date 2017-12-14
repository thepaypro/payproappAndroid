package app.paypro.payproapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import app.paypro.payproapp.asynctask.QRBitmapGeneratorAsyncTask;

/**
 * Created by rogerbaiget on 22/11/17.
 */

public class ShowQRCodeFragment extends Fragment {

    String addr;
    private Toolbar toolbar;
    private Bitmap bitmap;

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

        ImageView qrImageView = getView().findViewById(R.id.qr_code);
        ProgressBar progressBar = getView().findViewById(R.id.progress_bar);
        toolbar = getActivity().findViewById(R.id.toolbar);

        ((TabActivity)getActivity()).setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.frame_layout, AccountFragment.newInstance());
//                transaction.addToBackStack(null);
                transaction.commit();
//                getActivity().onBackPressed();
            }
        });


        QRBitmapGeneratorAsyncTask qrBitmapGeneratorAsyncTask = new QRBitmapGeneratorAsyncTask(qrImageView,progressBar);
        qrBitmapGeneratorAsyncTask.execute("bitcoin:"+addr);


//        qrImageView.setImageBitmap(generateQRBitMap("bitcoin:"+addr));
    }

//    private Bitmap generateQRBitMap(final String content) {
//
//        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
//
//        hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H);
//
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//
//        try {
//            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 512, 512, hints);
//
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//
//            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//
//                    bmp.setPixel(x , y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
//                }
//            }
//
//            return bmp;
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
}
