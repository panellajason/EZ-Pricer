package codingsharks.ezpricer.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import codingsharks.ezpricer.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ItemsAdapter extends FirestoreRecyclerAdapter<Items, ItemsAdapter.ItemHolder> {

    private OnItemClickListener listener;

    public ItemsAdapter(@NonNull final FirestoreRecyclerOptions<Items> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ItemHolder itemHolder, final int position, @NonNull final Items item) {
        itemHolder.mImageView.setImageResource(item.getmImageResource());
        itemHolder.mItemTitle.setText(item.getItem_name());
        itemHolder.mItemDescription.setText(String.valueOf(item.getItem_price()));
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {

        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        return new ItemHolder(v);
    }

    public void deleteItem(final int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public void setOnItemClickListener(final OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mItemTitle;
        public TextView mItemDescription;

        public ItemHolder(@NonNull final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mItemTitle = itemView.findViewById(R.id.itemName);
            mItemDescription = itemView.findViewById(R.id.Description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

}
