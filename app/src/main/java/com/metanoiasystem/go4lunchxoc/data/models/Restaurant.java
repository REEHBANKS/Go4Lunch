package com.metanoiasystem.go4lunchxoc.data.models;

import java.io.Serializable;

public class Restaurant  implements Serializable {


    private String id;
    private String restaurantName;
    private Double latitude;
    private Double longitude;
    private String urlPictureRestaurant;
    private String restaurantAddress;
    private Boolean openingHours;
    private Float rating;
    private int distanceKm;
    private String numberPhone;
    private String email;

    public Restaurant() {
    }

    public Restaurant(String id, String restaurantName, Double latitude, Double longitude,
                      String urlPictureRestaurant, String restaurantAddress, Boolean openingHours,
                      Float rating, int distanceKm) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.urlPictureRestaurant = urlPictureRestaurant;
        this.restaurantAddress = restaurantAddress;
        this.openingHours = openingHours;
        this.rating = rating;
        this.distanceKm = distanceKm;
    }

    public Restaurant(String id, String restaurantName, Double latitude, Double longitude, String urlPictureRestaurant, String restaurantAddress, Boolean openingHours, Float rating, int distanceKm, String numberPhone, String email) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.urlPictureRestaurant = urlPictureRestaurant;
        this.restaurantAddress = restaurantAddress;
        this.openingHours = openingHours;
        this.rating = rating;
        this.distanceKm = distanceKm;
        this.numberPhone = numberPhone;
        this.email = email;
    }

//    public Restaurant (String id){this.id = id;}



    // --- GETTERS ---
    public String getId() {
        return id;
    }

    public String getUrlPictureRestaurant() {
        return urlPictureRestaurant;
    }

    public int getDistanceKm() {
        return distanceKm;
    }


    public String getRestaurantName() {
        return restaurantName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public Boolean getOpeningHours() {
        return openingHours;
    }

    public Float getRating() {
        return rating;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public String getEmail() {
        return email;
    }


    // --- SETTERS ---
    public void setId(String id) {
        this.id = id;
    }



    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + id + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", urlPictureRestaurant='" + urlPictureRestaurant + '\'' +
                ", restaurantAddress='" + restaurantAddress + '\'' +
                ", openingHours=" + openingHours +
                ", rating=" + rating +
                ", distanceKm=" + distanceKm +
                ", numberPhone='" + numberPhone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


}
