package com.metanoiasystem.go4lunchxoc.data.models;

public class SelectedRestaurant {

    private String restaurantId;
    private String userId;
    private String dateSelected;



    public SelectedRestaurant() { }

    public SelectedRestaurant (String restaurantId,String userId,String dateSelected) {
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.dateSelected = dateSelected;

    }

    // --- GETTERS ---
    public String getRestaurantId() {return restaurantId;}
    public String getUserId() {return userId;}
    public String getDateSelected() {return dateSelected;}

    // --- SETTERS ---
    public void setRestaurantId(String restaurantId) {this.restaurantId = restaurantId;}
    public void setUserId(String userId) {this.userId = userId;}
    public void setDateSelected(String dateSelected) {this.dateSelected = dateSelected;}
}
