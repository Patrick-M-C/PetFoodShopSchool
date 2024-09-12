package com.example.petfoodshopschool;

import java.util.ArrayList;
import java.util.List;

public class Basket {
    private static Basket instance;
    private List<PetFoodItem> items;

    private Basket() {
        items = new ArrayList<>();
    }

    public static synchronized Basket getInstance() {
        if (instance == null) {
            instance = new Basket();
        }
        return instance;
    }

    public void addItem(PetFoodItem item) {
        items.add(item);
    }

    public void removeItem(PetFoodItem item) {
        items.remove(item);
    }

    public List<PetFoodItem> getItems() {
        return new ArrayList<>(items); // Return a copy to avoid external modifications
    }

    public double getTotalPrice() {
        double total = 0;
        for (PetFoodItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    public void clearBasket() {
        items.clear();
    }
}
