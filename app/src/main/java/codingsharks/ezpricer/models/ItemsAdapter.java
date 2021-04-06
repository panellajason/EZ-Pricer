package codingsharks.ezpricer.models;

import android.content.Intent;
import android.net.Uri;
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
import com.squareup.picasso.Picasso;

public class ItemsAdapter extends FirestoreRecyclerAdapter<Items, ItemsAdapter.ItemHolder> {

    private OnItemClickListener listener;

    public ItemsAdapter(@NonNull  FirestoreRecyclerOptions<Items> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemHolder itemHolder,  int position, @NonNull Items item) {
        Picasso.get().load(item.getImageUrl()).into(itemHolder.mImageView);
        itemHolder.mItemTitle.setText(item.getItem_name());
        itemHolder.mItemDescription.setText(String.valueOf(item.getItem_price()));
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent,  int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ItemHolder(v);
    }

    public void deleteItem( int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mItemTitle;
        public TextView mItemDescription;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mItemTitle = itemView.findViewById(R.id.itemName);
            mItemDescription = itemView.findViewById(R.id.Description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v) {
                    final int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
//                        v.getContext().startActivity(browserIntent);
                    }
                }
            });
        }
    }

}
