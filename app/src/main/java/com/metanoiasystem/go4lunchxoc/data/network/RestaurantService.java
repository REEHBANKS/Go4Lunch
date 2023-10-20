package com.metanoiasystem.go4lunchxoc.data.network;

import android.os.Build;

import com.metanoiasystem.go4lunchxoc.BuildConfig;
import com.metanoiasystem.go4lunchxoc.data.models.apiresponse.AllTheListRestaurantsResponse;
import com.metanoiasystem.go4lunchxoc.data.models.apiresponse.OneRestaurantResponse;



import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RestaurantService {


    @Headers("Log-Tag: NearbySearch")
    @GET("maps/api/place/nearbysearch/json?radius=1500&type=restaurant")
    Observable<AllTheListRestaurantsResponse> getAllRestaurantsResponse(@Query("key") String api,
                                                                        @Query("location") String location,
                                                                        @Query("fields") String fields);
    @Headers("Log-Tag: PlaceDetails")
    @GET("maps/api/place/details/json?")
    Observable<OneRestaurantResponse> getOneRestaurantByIdResponse(@Query("key") String api,
                                                                   @Query("place_id") String id);


}
