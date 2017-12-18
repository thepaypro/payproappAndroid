package app.paypro.payproapp.asynctask;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

import app.paypro.payproapp.R;

/**
 * Created by rogerbaiget on 14/12/17.
 */

public class QRBitmapGeneratorAsyncTask extends AsyncTask<String, Void, Bitmap>{
    private ImageView qrCode;
    private ProgressBar progressBar;
    public QRBitmapGeneratorAsyncTask(ImageView qrCode, ProgressBar progressBar) {
        this.qrCode = qrCode;
        this.progressBar = progressBar;
    }


    @Override
    protected Bitmap doInBackground(String... contents) {
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();

        hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(contents[0], BarcodeFormat.QR_CODE, 512, 512, hints);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {

                    bmp.setPixel(x , y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap img){
        if(qrCode!=null && img!=null){
            qrCode.setImageBitmap(img);
            progressBar.setVisibility(View.GONE);
            qrCode.setVisibility(View.VISIBLE);
        }
    }
}
