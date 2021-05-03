package codingsharks.ezpricer.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import codingsharks.ezpricer.R;
import codingsharks.ezpricer.models.Item;
import codingsharks.ezpricer.models.Vendor;
import codingsharks.ezpricer.models.VendorListAdapter;


public class CompareFragment extends Fragment{
    private ListView mListView;
    private ImageView pImage;
    private ImageView refreshIV;
    private EditText itemET;
    private ArrayList<Vendor> vendorsList;
    private View view_main;
    private VendorListAdapter adapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference itemRef = db.collection("items");

    private ProgressDialog dialog;

    public CompareFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Compare Prices");
        view_main = inflater.inflate(R.layout.fragment_compare, container, false);
        vendorsList = new ArrayList<>();
        pImage = view_main.findViewById(R.id.productImage);
        itemET = view_main.findViewById(R.id.searchbox);
        refreshIV = view_main.findViewById(R.id.refreshIV);
        adapter = new VendorListAdapter(this.getContext(), R.layout.vendor_row, vendorsList);

        //Checks to see if searched by barcode scanner
        Bundle extras = getArguments();
        if (extras != null) {
            String upcSearch = getArguments().getString("upc2") + "";
            Log.i("UPC:", upcSearch);
            getArguments().remove("upc2");
            createBarcodeVendorListView(view_main, upcSearch);
        }

        itemET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                Log.d("KEY PRESSED", String.valueOf(keyCode));
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

        refreshIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                vendorsList.clear();
                adapter.notifyDataSetChanged();
                itemET.setText("");
                pImage.setImageDrawable(null);
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
        new AsinRequestAmazonAPI().execute(barcode);
        new BarcodeRequestWalmartAPI().execute(barcode);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                Log.i("ONITEMCLICK",vendorsList.get(i).getItem().getProductUrl());
                String productURL = vendorsList.get(i).getItem().getProductUrl();
                if(!"".equals(productURL)){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productURL));
                    startActivity(browserIntent);
                }
            }
        });
    }

    private void createVendorListView(View view, String itemName) {
        mListView = view.findViewById(R.id.vendorListView);
        Log.i("TextView upon clicked", String.valueOf(itemET.getText()));

        //UNCOMMENT THIIS
        new RequestWalmartAPI().execute(itemName);
        new RequestAmazonAPI().execute(itemName);
        new RequestTargetAPI().execute(itemName);

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

    private class RequestWalmartAPI extends AsyncTask<String, Void, Item> {
        @Override
        protected void onPreExecute(){
            dialog = ProgressDialog.show(getActivity(), "",
                    "Loading. Please wait...");
        }
        @Override
        protected Item doInBackground(String... strings) {
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
                        String name = item_name.replace("<mark>", "").replace("</mark>", "");
                        String product_id = array.getJSONObject(i).getString("usItemId");
                        String product_url = "https://www.walmart.com/ip/" + product_id;
                        Log.i("PRODUCT URL", product_url);
                        Log.i("ITEM name", name);
                        Log.i("ITEM URL", image_url);
                        Log.i("ITEM PRICE:", String.valueOf(price.getDouble("offerPrice")));
                        //change item_name
                        return new Item(name, item_price, mAuth.getCurrentUser().getUid(),image_url,product_url,description);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Item result){
            //Log.i("Item", result.toString());
            vendorsList.clear();
            if (result != null) {
                LoadImageFromWeb(result.getImageUrl());
                Vendor WalmartVendorTest = new Vendor("Walmart", result);
                vendorsList.add(WalmartVendorTest);
                adapter.notifyDataSetChanged();
                Log.i("DONE", "done");
            }
        }
    }

    private class RequestAmazonAPI extends AsyncTask<String, Void, Item> {
        @Override
        protected Item doInBackground(final String... strings) {
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
                String name = item_name.replace("<mark>", "").replace("</mark>", "");
                String item_price = jArray.getJSONObject(0).getString("price");
                String item_image = jArray.getJSONObject(0).getString("imageUrl");
                String item_url = jArray.getJSONObject(0).getString("detailPageURL");
                Double newPrice = Double.parseDouble(item_price.replace("$", ""));

                Log.i("ITEM name", name);
                Log.i("ITEM URL", item_price);
                Log.i("ITEM PRICE:", newPrice+"");
                Log.i("ITEM image", item_image);

                return new Item(name, newPrice, mAuth.getCurrentUser().getUid(),item_image, item_url,"");

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Item result){
            if(pImage.getDrawable() == null){
                LoadImageFromWeb(result.getImageUrl());
            }
            Log.i("AMAZON", result.toString());
            Vendor amazonVendorTest = new Vendor("Amazon",result);
            vendorsList.add(amazonVendorTest);
            adapter.notifyDataSetChanged();
            Log.i("DONE", "done");
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }

    private class AsinRequestAmazonAPI extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(final String... strings) {
            if (strings.length != 0) {
                //Log.i("String[0] is", strings[0]);

                //String url = "https://amazon-price1.p.rapidapi.com/upcToAsin?upc=" + strings[0] + "&marketplace=US";
                String url = "https://amazon-price1.p.rapidapi.com/upcToAsin?upc=" + "711719511793" + "&marketplace=US";

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .addHeader("x-rapidapi-key", "11b6ebdc42msh09ac88d621f6ab5p177cdfjsn50498d090800")
                            .addHeader("x-rapidapi-host", "amazon-price1.p.rapidapi.com")
                            .build();
                    Response response = client.newCall(request).execute();

                    JSONObject obj = new JSONObject(response.body().string());

                    String asin = obj.getString("asin");

                    return asin;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result){
            Log.i("AMAZONBarcode", result.toString());
            new BarcodeRequestAmazonAPI().execute(result);
        }
    }
    private class BarcodeRequestWalmartAPI extends AsyncTask<String, Void, Item> {
        @Override
        protected Item doInBackground(String... strings) {
            Log.i("In Barcode Walmart", strings[0]);
            OkHttpClient client = new OkHttpClient();
            String url = "https://walmart2.p.rapidapi.com/searchByUPC?upc=00" + strings[0];

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
                JSONObject jsonObject= new JSONObject(jsonData);

                String item_name = jsonObject.getString("title");
                String name = item_name.replace("<mark>", "").replace("</mark>", "");
                String image_url = jsonObject.getString("imageUrl");
                String id = jsonObject.getString("usItemId");
                String desc = jsonObject.getString("description");

                JSONObject jsonObject1 = jsonObject.getJSONObject("primaryOffer");
                Double price = jsonObject1.getDouble("minPrice");

                Iterator<String> keys = jsonObject1.keys();

                String product_url = "https://www.walmart.com/ip/" + id;
                while(keys.hasNext()) {
                    String key = keys.next();
                    Log.i("KEYS",key);
                }
                return new Item(name, price, mAuth.getCurrentUser().getUid(),image_url,product_url ,desc);


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Item result){
            //Log.i("Item", result.toString());
            vendorsList.clear();
            if (result != null) {
                LoadImageFromWeb(result.getImageUrl());
                Vendor WalmartVendorTest = new Vendor("Walmart", result);
                vendorsList.add(WalmartVendorTest);
                adapter.notifyDataSetChanged();
                Log.i("DONE", "done");
            }
        }
    }


    private class BarcodeRequestAmazonAPI extends AsyncTask<String, Void, Item> {
        @Override
        protected Item doInBackground(final String... strings) {
            Log.i("String[0] is", strings[0]);
            String url2 = "https://amazon-price1.p.rapidapi.com/priceReport?asin=" + strings[0] + "&marketplace=US";

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url2)
                        .get()
                        .addHeader("x-rapidapi-key", "11b6ebdc42msh09ac88d621f6ab5p177cdfjsn50498d090800")
                        .addHeader("x-rapidapi-host", "amazon-price1.p.rapidapi.com")
                        .build();
                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                Log.i("Amazon API", jsonData);

                JSONObject jsonObject= new JSONObject(jsonData);

                String item_name = jsonObject.getString("title");
                String name = item_name.replace("<mark>", "").replace("</mark>", "");
                JSONObject ob = new JSONObject(jsonObject.getString("lastPrice"));
                String item_price = ob.getString("priceAmazon");
                item_price = item_price.substring(0, item_price.length() - 2);

                Log.i("ITEM name", item_name);
                Log.i("ITEM URL", item_price + "");
                Log.i("ITEM PRICE:", item_price+"");
                Log.i("ITEM image", "");

                return new Item(name, Double.parseDouble(item_price), mAuth.getCurrentUser().getUid(),"", "","");

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Item result){
            Log.i("AMAZONITEM", result.toString());
            Vendor amazonVendorTest = new Vendor("Amazon",result);
            vendorsList.add(amazonVendorTest);
            adapter.notifyDataSetChanged();
            Log.i("DONE", "done");
        }
    }

    private class RequestTargetAPI extends AsyncTask<String, Void, Item> {

        @Override
        protected Item doInBackground(String... strings) {
            Log.i("String[0] is", strings[0]);
            OkHttpClient client = new OkHttpClient();
            String location_id = "2319";
            String url="https://target1.p.rapidapi.com/products/list?storeId="+location_id+
                    "&pageSize=20&pageNumber=1&sortBy=relevance&searchTerm="+strings[0];

            try {
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("x-rapidapi-key","aa2d94dbbdmsh65b52c90df134e6p16e909jsn48939947babf")
                        .addHeader("x-rapidapi-host","target1.p.rapidapi.com")
                        .build();
                Response response = client.newCall(request).execute();
                String jsonData=response.body().string();
                Log.i("TargetAPI",jsonData);
                JSONObject object = new JSONObject(jsonData);
                JSONArray array = object.getJSONArray("products");
                String image_url = array.getJSONObject(0).getJSONObject("images").getString("primaryUri");
                JSONObject oPrice = array.getJSONObject(0).getJSONObject("price");
                String item_price = oPrice.getString("formatted_current_price");
                double price = Double.parseDouble(item_price.substring(1));
                String description = array.getJSONObject(0).getString("description");
                String item_name = array.getJSONObject(0).getString("title");
                String name = item_name.replace("<mark>", "").replace("</mark>", "");
                String product_url = array.getJSONObject(0).getString("targetDotComUri");
                Log.i("ITEMURL",image_url);
                Log.i("ITEMPRICE:",String.valueOf(price));
                return new Item(name,price,mAuth.getCurrentUser().getUid(),image_url,product_url,"");

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Item item) {
            Vendor TargetVendorTest = new Vendor("Target",item);
            vendorsList.add(TargetVendorTest);
            adapter.notifyDataSetChanged();
            Log.i("DONE","done");
        }
    }


}
