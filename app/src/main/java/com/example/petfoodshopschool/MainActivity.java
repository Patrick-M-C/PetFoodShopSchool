package com.example.petfoodshopschool;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodItemAdapter adapter;
    private List<PetFoodItem> foodItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list of food items
        foodItems = new ArrayList<>();
        foodItems.add(new PetFoodItem("Dog Food", "Premium dry and wet food for dogs", 9.99));
        foodItems.add(new PetFoodItem("Cat Food", "Delicious cat treats for any felian out there", 4.59));
        foodItems.add(new PetFoodItem("Fish Food", "Nutritious flakes for fish of all kinds", 2.99));
        foodItems.add(new PetFoodItem("Hamster Food", "Got options of both wet and dry food", 4.99));

        // Initialize the adapter and set it to the RecyclerView
        adapter = new FoodItemAdapter(foodItems, this::onItemClick);
        recyclerView.setAdapter(adapter);
    }

    // Handle clicking on an item in the food list
    private void onItemClick(PetFoodItem item) {
        // Open the FoodDetailActivity when an item is clicked
        Intent intent = new Intent(this, FoodDetailActivity.class);
        intent.putExtra("foodName", item.getName());
        intent.putExtra("foodDescription", item.getDescription());
        intent.putExtra("foodPrice", item.getPrice());
        startActivity(intent);
    }

    // Inflate the options menu (to add a basket icon)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu); // Menu with basket icon
        return true;
    }

    // Handle menu item selection (Basket icon)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_basket) {
            // Open BasketActivity when the basket icon is clicked
            Intent intent = new Intent(this, BasketActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
