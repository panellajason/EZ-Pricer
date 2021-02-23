package codingsharks.ezpricer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Console;
import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {
    private RecyclerView mRecylerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        ArrayList<Items> itemsArrayList = new ArrayList<>();
        itemsArrayList.add(new Items("Chess", 200.00));


        db = FirebaseFirestore.getInstance();
        CollectionReference itemCollectionRef = db.collection("items");
        Query itemsQuery = itemCollectionRef.whereEqualTo("userId", mAuth.getCurrentUser().getUid());

        itemsQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot i: task.getResult()){
                        Items item = i.toObject(Items.class);
                        itemsArrayList.add(item);
                        Log.i("Adding", item.getItem_name());
                    }
                    mAdapter.notifyDataSetChanged();
                }
                else {
                    Log.i("Query:", "failed");
                }
            }
        });

        mRecylerView = findViewById(R.id.itemRecyclerView);
        mRecylerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        Log.i("size", String.valueOf(itemsArrayList.size()));
        mAdapter = new ItemAdapter(itemsArrayList);

        mRecylerView.setLayoutManager(mLayoutManager);
        mRecylerView.setAdapter(mAdapter);
    }
}