package app.paypro.payproapp;

/**
 * Created by rogerbaiget on 28/11/17.
 */

import android.support.v4.app.Fragment;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

import app.paypro.payproapp.ui.camera.GraphicOverlay;

/**
 * Factory for creating a tracker and associated graphic to be associated with a new barcode.  The
 * multi-processor uses this factory to create barcode trackers as needed -- one for each barcode.
 */
class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;
    private Fragment mFragment;
    private SendMoney sendMoney;

    public BarcodeTrackerFactory(GraphicOverlay<BarcodeGraphic> mGraphicOverlay, SendMoney sendMoney, Fragment mFragment) {
        this.mGraphicOverlay = mGraphicOverlay;
        this.mFragment = mFragment;
        this.sendMoney = sendMoney;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        BarcodeGraphic graphic = new BarcodeGraphic(mGraphicOverlay,sendMoney);
        return new BarcodeGraphicTracker(mGraphicOverlay, graphic, mFragment);
    }

}