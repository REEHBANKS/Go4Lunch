package com.metanoiasystem.go4lunchxoc.data.models.apiresponse;


import com.google.gson.annotations.SerializedName;

public class GeometryResponse {


    @SerializedName("location")
    private LocationResponse locationResponse;

    public GeometryResponse(LocationResponse locationResponse) {
        this.locationResponse = locationResponse;
    }

    public LocationResponse getLocationResponse() {
        return locationResponse;
    }

    public void setLocationResponse(LocationResponse locationResponse) {
        this.locationResponse = locationResponse;
    }
}
