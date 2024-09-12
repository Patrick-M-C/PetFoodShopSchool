package com.example.petfoodshopschool;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasketActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BasketAdapter adapter;
    private TextView totalPriceTextView;
    private Button submitButton;
    private RequestQueue requestQueue;
    private List<PetFoodItem> basketItems; // Basket items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        recyclerView = findViewById(R.id.recyclerView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        submitButton = findViewById(R.id.submitButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get basket instance and load items
        basketItems = Basket.getInstance().getItems();

        // Set up adapter
        adapter = new BasketAdapter(basketItems, this); // Pass the activity context
        recyclerView.setAdapter(adapter);

        // Calculate total price
        updateTotalPrice();

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Set up button click listener to send data to the API
        submitButton.setOnClickListener(v -> createBasketItem());
    }

    // Method to send basket data to the API using Volley
    void createBasketItem() {
        String url = "http://192.168.0.115:8080/basketitems"; // Update with your API URL

        // Prepare the basket data for submission
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response
                        int result = Integer.parseInt(response);
                        if (result == -1) {
                            Log.d("Create", "onResponse: Basket submission failed");
                        } else {
                            Toast.makeText(BasketActivity.this, "Basket submitted successfully!", Toast.LENGTH_SHORT).show();
                            // Optionally, clear the basket after successful submission
                            Basket.getInstance().clearBasket();
                            basketItems.clear();
                            adapter.notifyDataSetChanged();
                            updateTotalPrice(); // Update the total price after clearing the basket
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Log.e("Volley Error", "onErrorResponse: ", error);
                        Toast.makeText(BasketActivity.this, "Error sending basket: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    // Create a JSON array of basket items
                    JSONArray jsonArray = new JSONArray();
                    for (PetFoodItem item : basketItems) {
                        JSONObject itemObject = new JSONObject();
                        itemObject.put("name", item.getName());
                        itemObject.put("description", item.getDescription());
                        itemObject.put("price", item.getPrice());
                        jsonArray.put(itemObject);
                    }

                    // Wrap the items in a JSON object (if needed)
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("basketItems", jsonArray);

                    String requestBody = jsonObject.toString();
                    return requestBody.getBytes(StandardCharsets.UTF_8);
                } catch (JSONException e) {
                    Log.e("TAG", "getBody: JSON conversion error", e);
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json"); // Set request headers
                return params;
            }
        };

        // Add request to the request queue
        requestQueue.add(request);
    }

    // Method to update the total price
    public void updateTotalPrice() {
        runOnUiThread(() -> {
            double totalPrice = Basket.getInstance().getTotalPrice();
            totalPriceTextView.setText("Total: $" + String.format("%.2f", totalPrice));
        });
    }
}
