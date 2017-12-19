package app.paypro.payproapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;

import app.paypro.payproapp.global.Global;
import app.paypro.payproapp.ui.camera.CameraSource;
import app.paypro.payproapp.ui.camera.CameraSourcePreview;
import app.paypro.payproapp.ui.camera.GraphicOverlay;


public class ScanFragment extends Fragment implements BarcodeGraphicTracker.BarcodeUpdateListener{
    private static final String TAG = "Barcode-reader";

    private RelativeLayout mainView;

    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    private LinearLayout permissionDeniedView;
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;

    private static final int RC_HANDLE_GMS = 9001;
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    public static ScanFragment newInstance() {
        ScanFragment fragment = new ScanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.scan_tab, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainView = getActivity().findViewById(R.id.main_view);
        permissionDeniedView = getActivity().findViewById(R.id.permission_denied_view);

        mPreview = getActivity().findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay<BarcodeGraphic>) getActivity().findViewById(R.id.graphicOverlay);

        int rc = ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }

    }

    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{android.Manifest.permission.CAMERA};

        requestPermissions(permissions, RC_HANDLE_CAMERA_PERM);

    }


    @SuppressLint("InlinedApi")
    private void createCameraSource() {
        Context context = getContext();

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.QR_CODE).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay, this);
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());

        if (!barcodeDetector.isOperational()) {
            Log.w(TAG, "Detector dependencies are not yet available.");

            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = getActivity().registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(getContext(), R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

            CameraSource.Builder builder = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(768, 1240)
                .setRequestedFps(15.0f);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        mCameraSource = builder
                .build();

    }

    @Override
    public void onResume() {
        super.onResume();
        startCameraSource();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            createCameraSource();
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        showPermissionDeniedView();
    }

    private void startCameraSource() throws SecurityException {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    @Override
    public void onBarcodeDetected(Barcode barcode) {
        SendMoney sendMoney = Global.resetSendMoney();
        if (sendMoney.bitcoinURISaveData(barcode.displayValue)){
            SendMoneyAmountFragment myfragment = new SendMoneyAmountFragment();
            FragmentManager fragmentManager = ((TabActivity)getContext()).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            transaction.replace(R.id.frame_layout, myfragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }else{
            Global.resetSendMoney();
        }
    }

    public void showPermissionDeniedView(){
        mPreview.setVisibility(View.GONE);
        permissionDeniedView.setVisibility(View.VISIBLE);
    }
}