package com.metanoiasystem.go4lunchxoc.data.models;

import java.io.Serializable;

public class RestaurantWithNumberUser implements Serializable {

    private Restaurant restaurant;

    private int numberUser;

    public RestaurantWithNumberUser(Restaurant restaurant, int numberUser) {
        this.restaurant = restaurant;
        this.numberUser = numberUser;
    }

    public int  getNumberUser() {
        return numberUser;
    }

    public void setNumberUser(int numberUser) {
        this.numberUser = numberUser;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

}