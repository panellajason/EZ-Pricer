package codingsharks.ezpricer.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;

import codingsharks.ezpricer.R;
import codingsharks.ezpricer.models.Items;
import codingsharks.ezpricer.models.Vendor;
import codingsharks.ezpricer.models.vendorListAdapter;


public class CompareFragment extends Fragment implements View.OnClickListener {

    private ListView mListView;
    private ImageView pImage;
    private Button submitButton;
    private ArrayList<Vendor> vendorsList;
    private View view_main;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference itemRef = db.collection("items");
    public CompareFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Compare Prices");
        view_main = inflater.inflate(R.layout.fragment_compare, container, false);
        vendorsList = new ArrayList<>();
        //search and add items to
        pImage = view_main.findViewById(R.id.productImage);

        submitButton = view_main.findViewById(R.id.APISubmitButton);
        submitButton.setOnClickListener(this);

        return view_main;
    }
    private void LoadImageFromWeb(String url){
        Picasso.get().load(url).into(pImage);
    }
    private void createVendorListView(View view) {
        mListView = view.findViewById(R.id.vendorListView);

//        Items item = new Items("Shoes",60.0,mAuth.getCurrentUser().getUid());
//        Vendor WalmartVendorTest = new Vendor("Walmart",item);
//        Items item2 = new Items("Shoes",100.0,mAuth.getCurrentUser().getUid());
//        Vendor BestBuyVendorTest = new Vendor("Bestbuy",item2);



//        vendorsList.add(WalmartVendorTest);
//        vendorsList.add(BestBuyVendorTest);

        //UNCOMMENT THIIS
        //new RequestWalmartAPI().execute();

        vendorListAdapter adapter = new vendorListAdapter(this.getContext(), R.layout.vendor_row, vendorsList);
        mListView.setAdapter(adapter);
    }
    @Override
    public void onClick(View view) {
        createVendorListView(view_main);
    }
    private class RequestWalmartAPI extends AsyncTask<Void, Void, Items> {

        @Override
        protected Items doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            String url = "https://walmart2.p.rapidapi.com/search?query=" + "playstation 5" + "&page=1";
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("x-rapidapi-key", "11b6ebdc42msh09ac88d621f6ab5p177cdfjsn50498d090800")
                        .addHeader("x-rapidapi-host", "walmart2.p.rapidapi.com")
                        .build();
                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                Log.i("Walmart API", jsonData);
                JSONObject jObject= new JSONObject(jsonData);
                JSONArray array = jObject.getJSONArray("items");
                for(int i = 0; i < array.length(); i++){
                    if(array.getJSONObject(i).getJSONObject("primaryOffer").has("offerPrice")){
                        String image_url = array.getJSONObject(i).getString("imageUrl");
                        JSONObject price = array.getJSONObject(i).getJSONObject("primaryOffer");
                        double item_price = price.getDouble("offerPrice");
                        String description = array.getJSONObject(i).getString("description");
                        Log.i("ITEM URL", image_url);
                        Log.i("ITEM PRICE:", String.valueOf(price.getDouble("offerPrice")));
                        return new Items("ipad", item_price, mAuth.getCurrentUser().getUid(),image_url,description);
                    }
                }
//                Log.i("WalmartItem", (String) json2.get("ppu"));
                //vendorsList.add(WalmartVendorTest);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Items result){
            Log.i("Item", result.toString());
            LoadImageFromWeb(result.getImageUrl());
            Vendor WalmartVendorTest = new Vendor("Walmart",result);
            vendorsList.add(WalmartVendorTest);
            itemRef.add(result);
        }
    }


}
