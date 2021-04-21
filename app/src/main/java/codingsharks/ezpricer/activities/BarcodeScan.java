package codingsharks.ezpricer.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import codingsharks.ezpricer.R;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.util.List;

public class BarcodeScan extends AppCompatActivity {

    private Uri postURI;
    private Bitmap mBitmap;
    private ImageView mImageView;
    private Button mButton;
    private TextView barcodeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        setTitle("Barcode Scan");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mImageView = findViewById(R.id.barcodeIV);
        mButton = findViewById(R.id.barcodeBTN);
        barcodeTV = findViewById(R.id.barcodeTV);

        openImagePicker();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                runBarcodeScanner();
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                openImagePicker();
            }
        });
    }

    private void openImagePicker() {
        barcodeTV.setText("");
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(128, 128)
                .setAspectRatio(3, 2)
                .start(BarcodeScan.this);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                postURI = result.getUri();
                mImageView.setImageURI(postURI);
                mImageView.invalidate();
                final BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();
                mBitmap = drawable.getBitmap();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                final Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(BarcodeScan.this, Main.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void runBarcodeScanner() {
        barcodeTV.setText("");
        if (mBitmap != null) {
            InputImage image = InputImage.fromBitmap(mBitmap, 0);
            BarcodeScanner scanner = BarcodeScanning.getClient();

            Task<List<Barcode>> result = scanner.process(image)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            if(barcodes.isEmpty())
                                barcodeTV.setText("No barcodes detected");
                            else {
                                for (Barcode barcode: barcodes) {
                                    String rawValue = barcode.getRawValue();

                                    bundleAndSendData(rawValue);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void bundleAndSendData(final String upc) {
        final Intent i = new Intent(BarcodeScan.this, Main.class);
        i.putExtra("upc", upc);
        startActivity(i);
        finish();
    }
}
