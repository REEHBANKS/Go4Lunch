package com.metanoiasystem.go4lunchxoc.data.network;

import com.metanoiasystem.go4lunchxoc.data.models.apiresponse.AllTheListRestaurantsResponse;
import com.metanoiasystem.go4lunchxoc.data.models.apiresponse.OneRestaurantResponse;



import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RestaurantService {

    // Fetches a list of nearby restaurants using Google Places API.
    @Headers("Log-Tag: NearbySearch")
    @GET("maps/api/place/nearbysearch/json?radius=1500&type=restaurant")
    Observable<AllTheListRestaurantsResponse> getAllRestaurantsResponse(@Query("key") String api,
                                                                        @Query("location") String location,
                                                                        @Query("fields") String fields);

    // Retrieves details for a specific restaurant by ID using Google Places API.
    @Headers("Log-Tag: PlaceDetails")
    @GET("maps/api/place/details/json?")
    Observable<OneRestaurantResponse> getOneRestaurantByIdResponse(@Query("key") String api,
                                                                   @Query("place_id") String id);
}
