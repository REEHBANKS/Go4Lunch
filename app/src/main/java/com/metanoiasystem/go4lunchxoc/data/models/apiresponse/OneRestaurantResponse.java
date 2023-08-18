package com.metanoiasystem.go4lunchxoc.data.models.apiresponse;

public class OneRestaurantResponse {


    private RestaurantResponse result;

    // GETTER & SETTER
    public RestaurantResponse getResult() {
        return result;
    }

    public void setResult(RestaurantResponse result) {
        this.result = result;
    }

    // CONSTRUCTOR

    public OneRestaurantResponse(RestaurantResponse result) {
        this.result = result;
    }
}
