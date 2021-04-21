package codingsharks.ezpricer.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import codingsharks.ezpricer.R;
import codingsharks.ezpricer.activities.AccountPage;
import codingsharks.ezpricer.activities.BarcodeScan;

public class BarcodeFragment extends Fragment {

    private Uri postURI;
    private Bitmap mBitmap;
    private ImageView mImageView;
    private Button mButton;
    private TextView barcodeTV;

    public BarcodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_barcode, container, false);

        getActivity().setTitle("Barcode Scan");

        startActivity(new Intent(getActivity(), BarcodeScan.class));

        return view;
    }

}
