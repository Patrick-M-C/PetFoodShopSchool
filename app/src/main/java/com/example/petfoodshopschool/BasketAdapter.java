package com.example.petfoodshopschool;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {
    private List<PetFoodItem> basketItems;
    private BasketActivity activity;

    public BasketAdapter(List<PetFoodItem> basketItems, BasketActivity activity) {
        this.basketItems = basketItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Check if position is valid
        if (position < 0 || position >= basketItems.size()) {
            Log.e("BasketAdapter", "Invalid position: " + position);
            return;
        }

        PetFoodItem item = basketItems.get(position);

        holder.nameTextView.setText(item.getName());
        holder.descriptionTextView.setText(item.getDescription());
        holder.priceTextView.setText(String.format("$%.2f", item.getPrice()));

        holder.deleteButton.setOnClickListener(v -> {
            // Ensure the item exists in the list
            if (position >= 0 && position < basketItems.size()) {
                PetFoodItem itemToRemove = basketItems.get(position);

                // Remove item from the basket
                Basket.getInstance().removeItem(itemToRemove);

                // Remove item from the adapter's list and notify
                basketItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, basketItems.size());

                // Update the total price
                activity.updateTotalPrice();
            } else {
                Log.e("BasketAdapter", "Item to remove not found or invalid position");
            }
        });
    }


    @Override
    public int getItemCount() {
        return basketItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.itemNameTextView);
            descriptionTextView = itemView.findViewById(R.id.itemDescriptionTextView);
            priceTextView = itemView.findViewById(R.id.itemPriceTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
