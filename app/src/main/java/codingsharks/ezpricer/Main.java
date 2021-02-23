package codingsharks.ezpricer;

import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Main extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Page");

        if (mAuth.getCurrentUser() != null) {
            db = FirebaseFirestore.getInstance();

            // Create a new db entry with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("item_name", "Shoes");
            user.put("item_price", 60);

            // Add a new document with a generated ID
            db.collection("items")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            //do something

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            //toast

                        }
                    });


        }


    }


    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {

            final Intent i = new Intent(Main.this, Login.class);
            startActivity(i);
            finish();
        }

        //bottomNav.setSelectedItemId(R.id.navList);

    }
}
