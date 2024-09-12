package com.example.petfoodshopschool;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDetailActivity extends AppCompatActivity {

    private Button addToBasketButton;
    private PetFoodItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView priceTextView = findViewById(R.id.priceTextView);
        addToBasketButton = findViewById(R.id.addToBasketButton);

        // Get the data from the intent
        String foodName = getIntent().getStringExtra("foodName");
        String foodDescription = getIntent().getStringExtra("foodDescription");
        double foodPrice = getIntent().getDoubleExtra("foodPrice", 0);

        // Set the data to the views
        nameTextView.setText(foodName);
        descriptionTextView.setText(foodDescription);
        priceTextView.setText("$" + foodPrice);

        // Create the current food item from the intent data
        currentItem = new PetFoodItem(foodName, foodDescription, foodPrice);

        // Add item to basket when "Add to Basket" button is clicked
        addToBasketButton.setOnClickListener(v -> {
            // Add the current item to the Basket
            Basket.getInstance().addItem(currentItem);

            // Show confirmation message to the user
            Toast.makeText(FoodDetailActivity.this, foodName + " added to basket!", Toast.LENGTH_SHORT).show();
        });
    }
}
