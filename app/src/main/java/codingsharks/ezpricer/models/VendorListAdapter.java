package codingsharks.ezpricer.models;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import codingsharks.ezpricer.activities.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;

import codingsharks.ezpricer.R;

public class VendorListAdapter extends ArrayAdapter<Vendor> {

    private Context mContext;
    private int mResource;
    private ImageView mImageView;
    private TextView vendor_nameTV;
    private TextView item_priceTV;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference itemRef = db.collection("items");

    public VendorListAdapter(@NonNull Context context, int resource, ArrayList<Vendor> objects) {
        super(context, resource, objects);
        mResource = resource;
        mContext = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Item item = getItem(position).getItem();
        String vendorName = getItem(position).getName();
        double itemPrice= getItem(position).getItem().getItem_price();
        int vendorLogo = getItem(position).getVendorLogoImageResource();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        vendor_nameTV = convertView.findViewById(R.id.vendorName);
        item_priceTV = convertView.findViewById(R.id.productPrice);
        mImageView = convertView.findViewById(R.id.vendorLogo);

        vendor_nameTV.setText(vendorName);
        item_priceTV.setText("$" + String.format(Locale.US,"%.2f",itemPrice));
        mImageView.setImageResource(vendorLogo);

        ImageButton addButton = convertView.findViewById(R.id.addButton);
        addButton.setOnClickListener(view -> {
            Log.d("demo", "This item is being clicked" + vendorName);
            itemRef.add(item);
        });
        return convertView;
    }
    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0)
    {
        return true;
    }
}
