package codingsharks.ezpricer.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.Console;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import codingsharks.ezpricer.R;
import codingsharks.ezpricer.models.Items;
import codingsharks.ezpricer.models.Vendor;
import codingsharks.ezpricer.models.vendorListAdapter;


public class CompareFragment extends Fragment {

    private ListView mListView;
    ImageView pImage;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public CompareFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Compare Prices");
        View view = inflater.inflate(R.layout.fragment_compare, container, false);

        //search and add items to
        pImage = view.findViewById(R.id.productImage);
        createVendorListView(view);
        LoadImageFromWeb("https://media.gettyimages.com/photos/elegant-black-leather-shoes-picture-id172417586?s=612x612");
        return view;
    }
    private void LoadImageFromWeb(String url){
        Picasso.get().load(url).into(pImage);
    }
    private void createVendorListView(View view) {
        mListView = view.findViewById(R.id.vendorListView);

        //temp image
        Items item = new Items("Shoes",60.0,mAuth.getCurrentUser().getUid());
        Vendor WalmartVendorTest = new Vendor("Walmart",item);
        Items item2 = new Items("Shoes",100.0,mAuth.getCurrentUser().getUid());
        Vendor BestBuyVendorTest = new Vendor("Bestbuy",item2);

        ArrayList<Vendor> vendorsList = new ArrayList<>();
        vendorsList.add(WalmartVendorTest);
        vendorsList.add(BestBuyVendorTest);

        vendorListAdapter adapter = new vendorListAdapter(this.getContext(), R.layout.vendor_row, vendorsList);
        mListView.setAdapter(adapter);
    }

}
