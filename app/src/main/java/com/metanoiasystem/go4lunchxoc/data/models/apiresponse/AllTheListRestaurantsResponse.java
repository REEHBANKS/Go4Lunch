package com.metanoiasystem.go4lunchxoc.data.models.apiresponse;

import java.util.List;

public class AllTheListRestaurantsResponse {
    private List<RestaurantResponse> results;

    // GETTER & SETTER
    public List<RestaurantResponse> getResults() {
        return results;
    }

    public void setResults(List<RestaurantResponse> results) {
        this.results = results;
    }

    // CONSTRUCTOR

    public void AllRestaurantsResponse(List<RestaurantResponse> results) {
        this.results = results;
    }
}
