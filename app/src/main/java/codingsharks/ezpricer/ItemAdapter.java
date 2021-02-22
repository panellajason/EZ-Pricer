package codingsharks.ezpricer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private ArrayList<Items> mItemsList;

    public static class ItemViewHolder extends ViewHolder{
        public ImageView mImageView;
        public TextView mItemTitle;
        public TextView mItemDescription;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mItemTitle = itemView.findViewById(R.id.itemName);
            mItemDescription = itemView.findViewById(R.id.Description);
        }
    }
    public ItemAdapter(ArrayList<Items> list){
         mItemsList = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item,parent,false);
        ItemViewHolder ivh = new ItemViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Items curr_item = mItemsList.get(position);
        holder.mImageView.setImageResource(curr_item.getmImageResource());
        holder.mItemTitle.setText(curr_item.getmItemName());
        holder.mItemDescription.setText(curr_item.getmPrice());
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

}
