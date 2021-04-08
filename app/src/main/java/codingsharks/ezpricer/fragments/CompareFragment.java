package codingsharks.ezpricer.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import codingsharks.ezpricer.models.ItemsAdapter;
import codingsharks.ezpricer.models.Vendor;
import codingsharks.ezpricer.models.vendorListAdapter;


public class CompareFragment extends Fragment{
    private ListView mListView;
    private ImageView pImage;
    private EditText itemET;
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
        pImage = view_main.findViewById(R.id.productImage);
        itemET = view_main.findViewById(R.id.searchbox);

        //Checks to see if searched by barcode scanner
        Bundle extras = getArguments();
        if (extras != null) {
            String upcSearch = getArguments().getString("upc2");

            createBarcodeVendorListView(view_main, upcSearch);

            getArguments().remove("upc2");
        }

        itemET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Log.i("TextView upon clicked", String.valueOf(itemET.getText()));
                    createVendorListView(view_main, String.valueOf(itemET.getText()));
                    return true;
                }
                return false;
            }
        });
        return view_main;
    }
    private void LoadImageFromWeb(String url){
        Picasso.get().load(url).into(pImage);
    }

    private void createBarcodeVendorListView(View view, String barcode) {
        mListView = view.findViewById(R.id.vendorListView);
        Log.i("TextView upon clicked", String.valueOf(itemET.getText()));

        new BarcodeRequestAmazonAPI().execute(barcode);

        vendorListAdapter adapter = new vendorListAdapter(this.getContext(), R.layout.vendor_row, vendorsList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                Log.i("ONITEMCLICK",vendorsList.get(i).getItem().getProductUrl());
                String productURL = vendorsList.get(i).getItem().getProductUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productURL));
                startActivity(browserIntent);
            }
        });
    }

    private void createVendorListView(View view, String itemName) {
        mListView = view.findViewById(R.id.vendorListView);
        Log.i("TextView upon clicked", String.valueOf(itemET.getText()));

        //UNCOMMENT THIIS
        new RequestWalmartAPI().execute(itemName);
        new RequestAmazonAPI().execute(itemName);

        vendorListAdapter adapter = new vendorListAdapter(this.getContext(), R.layout.vendor_row, vendorsList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                Log.i("ONITEMCLICK",vendorsList.get(i).getItem().getProductUrl());
                String productURL = vendorsList.get(i).getItem().getProductUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productURL));
                startActivity(browserIntent);
            }
        });
    }

    private class RequestWalmartAPI extends AsyncTask<String, Void, Items> {

        @Override
        protected Items doInBackground(String... strings) {
            Log.i("String[0] is", strings[0]);
            OkHttpClient client = new OkHttpClient();
            String url = "https://walmart2.p.rapidapi.com/search?query=" + strings[0] + "&page=1";

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
                        String item_name = array.getJSONObject(i).getString("title");
                        String product_id = array.getJSONObject(i).getString("usItemId");
                        String product_url = "https://www.walmart.com/ip/" + product_id;
                        Log.i("PRODUCT URL", product_url);
                        Log.i("ITEM name", item_name);
                        Log.i("ITEM URL", image_url);
                        Log.i("ITEM PRICE:", String.valueOf(price.getDouble("offerPrice")));
                        //change item_name
                        return new Items(item_name, item_price, mAuth.getCurrentUser().getUid(),image_url,product_url,description);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Items result){
            //Log.i("Item", result.toString());
            vendorsList.clear();
            LoadImageFromWeb(result.getImageUrl());
            Vendor WalmartVendorTest = new Vendor("Walmart",result);
            vendorsList.add(WalmartVendorTest);
            Log.i("DONE", "done");
        }
    }

    private class RequestAmazonAPI extends AsyncTask<String, Void, Items> {
        @Override
        protected Items doInBackground(final String... strings) {
            Log.i("String[0] is", strings[0]);
            String url = "https://amazon-price1.p.rapidapi.com/search?keywords=" + strings[0] + "&marketplace=US";

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("x-rapidapi-key", "11b6ebdc42msh09ac88d621f6ab5p177cdfjsn50498d090800")
                        .addHeader("x-rapidapi-host", "amazon-price1.p.rapidapi.com")
                        .build();
                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                Log.i("Amazon API", jsonData);
                JSONArray jArray= new JSONArray(jsonData);

                String item_name = jArray.getJSONObject(0).getString("title");
                String item_price = jArray.getJSONObject(0).getString("price");
                String item_image = jArray.getJSONObject(0).getString("imageUrl");
                String item_url = jArray.getJSONObject(0).getString("detailPageURL");
                Double newPrice = Double.parseDouble(item_price.replace("$", ""));

                Log.i("ITEM name", item_name);
                Log.i("ITEM URL", item_price);
                Log.i("ITEM PRICE:", newPrice+"");
                Log.i("ITEM image", item_image);

                return new Items(item_name, newPrice, mAuth.getCurrentUser().getUid(),item_image, item_url,"");

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Items result){
            Log.i("AMAZON", result.toString());
            Vendor amazonVendorTest = new Vendor("Amazon",result);
            vendorsList.add(amazonVendorTest);
            Log.i("DONE", "done");
        }
    }

    private class BarcodeRequestAmazonAPI extends AsyncTask<String, Void, Items> {
        @Override
        protected Items doInBackground(final String... strings) {
            Log.i("String[0] is", strings[0]);

            //String url = "https://amazon-price1.p.rapidapi.com/upcToAsin?upc=" + strings[0] + "&marketplace=US";
            String url = "https://amazon-price1.p.rapidapi.com/upcToAsin?upc=" +  "711719511793" + "&marketplace=US";

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("x-rapidapi-key", "11b6ebdc42msh09ac88d621f6ab5p177cdfjsn50498d090800")
                        .addHeader("x-rapidapi-host", "amazon-price1.p.rapidapi.com")
                        .build();
                Response response = client.newCall(request).execute();

                String asin = response.body().string();
                Log.i("Amazon ASIN", asin);

                String url2 = "https://amazon-price1.p.rapidapi.com/priceReport?asin=" + asin + "&marketplace=US";

                Request request2 = new Request.Builder()
                        .url(url2)
                        .get()
                        .addHeader("x-rapidapi-key", "11b6ebdc42msh09ac88d621f6ab5p177cdfjsn50498d090800")
                        .addHeader("x-rapidapi-host", "amazon-price1.p.rapidapi.com")
                        .build();
                Response response2 = client.newCall(request).execute();

                String jsonData = response.body().string();
                Log.i("Amazon API", jsonData);

                JSONArray jArray= new JSONArray(jsonData);

                String item_name = jArray.getJSONObject(0).getString("title");
                String item_price = jArray.getJSONObject(0).getString("price");
                String item_image = jArray.getJSONObject(0).getString("imageUrl");
                String item_url = jArray.getJSONObject(0).getString("detailPageURL");
                Double newPrice = Double.parseDouble(item_price.replace("$", ""));

                Log.i("ITEM name", item_name);
                Log.i("ITEM URL", item_price);
                Log.i("ITEM PRICE:", newPrice+"");
                Log.i("ITEM image", item_image);

                return new Items(item_name, newPrice, mAuth.getCurrentUser().getUid(),item_image, item_url,"");

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Items result){
            Log.i("AMAZON", result.toString());
            Vendor amazonVendorTest = new Vendor("Amazon",result);
            vendorsList.add(amazonVendorTest);
            Log.i("DONE", "done");
        }
    }


}
