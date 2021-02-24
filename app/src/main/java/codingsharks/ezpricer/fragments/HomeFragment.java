package codingsharks.ezpricer.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import codingsharks.ezpricer.R;
import codingsharks.ezpricer.models.Items;
import codingsharks.ezpricer.models.ItemsAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class HomeFragment extends Fragment {

    private RecyclerView mRecylerView;
    private ItemsAdapter itemAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Home");
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        setUpRecyclerView(view);

        return view;
    }

    private void setUpRecyclerView(View view) {
        db = FirebaseFirestore.getInstance();
        CollectionReference itemRef = db.collection("items");

        //this will change (userID = getUserUid())
        //just making sure db is connected to firebase
        final Query query = itemRef.whereEqualTo("item_name", "Gameboy");

        final FirestoreRecyclerOptions<Items> options = new FirestoreRecyclerOptions.Builder<Items>()
                .setQuery(query, Items.class)
                .build();

        itemAdapter = new ItemsAdapter(options);

        final RecyclerView recView = view.findViewById(R.id.itemRecyclerView);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recView.setAdapter(itemAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder,
                                  @NonNull final RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, final int direction) {
                itemAdapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(mRecylerView);

        itemAdapter.setOnItemClickListener(new ItemsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, final int position) {

                //do something

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        itemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        itemAdapter.stopListening();
    }
}
