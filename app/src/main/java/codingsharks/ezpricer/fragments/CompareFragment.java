package codingsharks.ezpricer.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import codingsharks.ezpricer.R;


public class CompareFragment extends Fragment {


    public CompareFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Compare Prices");
        final View view = inflater.inflate(R.layout.fragment_compare, container, false);

        // code here


        return view;
    }
}
