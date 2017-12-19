package app.paypro.payproapp.asynctask;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import app.paypro.payproapp.ScanFragment;
import app.paypro.payproapp.ui.camera.GraphicOverlay;
import app.paypro.payproapp.utils.Barcode.BarcodeGraphic;
import app.paypro.payproapp.utils.Barcode.BarcodeGraphicTracker;
import app.paypro.payproapp.R;
import app.paypro.payproapp.SendMoney;
import app.paypro.payproapp.SendMoneyAmountFragment;
import app.paypro.payproapp.TabActivity;
import app.paypro.payproapp.global.Global;
import app.paypro.payproapp.ui.camera.CameraSource;
import app.paypro.payproapp.utils.Barcode.BarcodeTrackerFactory;

/**
 * Created by rogerbaiget on 19/12/17.
 */

public class CreateCameraSourceAsyncTask extends AsyncTask<Void,Void,Void> {
    private static final String TAG = "Barcode-reader";
    private Context context;
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private ScanFragment fragment;

    public CreateCameraSourceAsyncTask(Context context, GraphicOverlay<BarcodeGraphic> mGraphicOverlay, ScanFragment fragment){
        this.context = context;
        this.mGraphicOverlay = mGraphicOverlay;
        this.fragment = fragment;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        createCameraSource();
        fragment.setmCameraSource(cameraSource);
        return null;
    }

    private void createCameraSource() {

        barcodeDetector = new BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.QR_CODE).build();
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(new BarcodeTrackerFactory(mGraphicOverlay, fragment)).build());

        if (!barcodeDetector.isOperational()) {
            Log.w(TAG, "Detector dependencies are not yet available.");

            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = context.registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(context, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, context.getResources().getString(R.string.low_storage_error));
            }
        }

        CameraSource.Builder builder = new CameraSource.Builder(context, barcodeDetector)
                .setRequestedPreviewSize(768, 1240)
                .setRequestedFps(15.0f);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        cameraSource = builder.build();

    }
    @Override
    protected void onPreExecute(){
        ((TabActivity)fragment.getActivity()).dissableBottomNavigationView();
    }

    @Override
    protected void onPostExecute(Void v){
        ((TabActivity)fragment.getActivity()).enableBottomNavigationView();
    }
}