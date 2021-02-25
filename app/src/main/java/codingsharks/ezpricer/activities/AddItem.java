package codingsharks.ezpricer.activities;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import codingsharks.ezpricer.R;
import codingsharks.ezpricer.models.Items;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddItem extends AppCompatActivity {

    private EditText itemNameET, itemPriceET;
    private Button addItemBTN;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference itemRef = db.collection("items");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        setTitle("Add Item to Watchlist");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        itemNameET = findViewById(R.id.itemNameET);
        itemPriceET = findViewById(R.id.itemPriceET);
        addItemBTN = findViewById(R.id.addItemBTN);

        addItemBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                saveItem();
            }
        });

    }

    private void saveItem() {
        final String itemName = itemNameET.getText().toString();
        final String itemPrice = itemPriceET.getText().toString();

        if (itemName.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter an item name", Toast.LENGTH_LONG).show();
            return;
        }

        if (itemPrice.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter an item price", Toast.LENGTH_LONG).show();
            return;
        }

        itemRef.add(new Items(itemName, Double.parseDouble(itemPrice), mAuth.getUid()));

        Toast.makeText(getApplicationContext(), "Item Saved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AddItem.this, Main.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(AddItem.this, Main.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
