package com.metanoiasystem.go4lunchxoc.data.models;

public class FavoriteRestaurant {


    private String restaurantId;
    private String userId;



    public FavoriteRestaurant() { }

    public FavoriteRestaurant (String restaurantId, String userId) {
        this.restaurantId = restaurantId;
        this.userId = userId;
    }

    // --- GETTERS ---
    public String getRestaurantId() {return restaurantId;}
    public String getUserId() {return userId;}

    // --- SETTERS ---
    public void setRestaurantId(String restaurantId) {this.restaurantId = restaurantId;}
    public void setUserId(String userId) {this.userId = userId;}
}
