package codingsharks.ezpricer.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import codingsharks.ezpricer.R;


public class BarcodeFragment extends Fragment {


    public BarcodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_barcode, container, false);

        getActivity().setTitle("Account Settings");


        return view;
    }

}
