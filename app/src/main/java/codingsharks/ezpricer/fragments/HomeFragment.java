package codingsharks.ezpricer.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import codingsharks.ezpricer.R;
import codingsharks.ezpricer.models.Item;
import codingsharks.ezpricer.models.ItemsAdapter;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class  HomeFragment extends Fragment {

    private RecyclerView mRecylerView;
    private ItemsAdapter itemAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference itemRef = db.collection("items");

    private Item mRecentlyDeletedItem;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Your Watchlist");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setUpRecyclerView(view);
        Log.i("rec view setup", "SUCCESSFUL");
        return view;
    }

    private void setUpRecyclerView(View view) {

        Query query = itemRef.whereEqualTo("userId", mAuth.getCurrentUser().getUid());

        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        itemAdapter = new ItemsAdapter(options);

        mRecylerView = view.findViewById(R.id.itemRecyclerView);
        mRecylerView.setHasFixedSize(true);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecylerView.setAdapter(itemAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Log.i("ITEM NAME 2.", itemAdapter.getItem(position).getItem_name());
                Log.i("ITEM URL 2.", itemAdapter.getItem(position).getProductUrl());
                itemAdapter.deleteItem(position);
                //Toast.makeText(getContext(),"Item Deleted",Toast.LENGTH_SHORT).show();
                mRecentlyDeletedItem = itemAdapter.getItem(position);
                showUndoSnackBar();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                Context context = itemView.getContext();
                new RecyclerViewSwipeDecorator.Builder(context, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(context, R.color.red))
                        .addActionIcon(R.drawable.ic_baseline_delete_24)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(mRecylerView);

        itemAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            Log.i("ItemOnClick", "TRUE");
            Item item = documentSnapshot.toObject(Item.class);
            String productURL = item.getProductUrl();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productURL));
            startActivity(browserIntent);
        });
 
    }

    private void showUndoSnackBar() {
        Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                "Item deleted", Snackbar.LENGTH_LONG);
        snackBar.setAction("UNDO", v-> undoDelete());
        snackBar.show();
    }

    private void undoDelete() {
        itemRef.add(mRecentlyDeletedItem);
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
