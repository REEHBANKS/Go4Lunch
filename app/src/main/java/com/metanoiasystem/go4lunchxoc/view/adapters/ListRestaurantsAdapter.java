package com.metanoiasystem.go4lunchxoc.view.adapters;


import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.metanoiasystem.go4lunchxoc.data.models.RestaurantWithNumberUser;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentListRestaurantsItemBinding;
import com.metanoiasystem.go4lunchxoc.utils.ImageUtils;
import com.metanoiasystem.go4lunchxoc.utils.RatingUtils;
import com.metanoiasystem.go4lunchxoc.utils.RestaurantStatusUtils;
import com.metanoiasystem.go4lunchxoc.view.viewholders.ListRestaurantsViewHolder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListRestaurantsAdapter extends RecyclerView.Adapter<ListRestaurantsViewHolder> {

    private final List<RestaurantWithNumberUser> restaurantWithNumberUsers;
    private final OnRestaurantClickListener mListener;

    public ListRestaurantsAdapter(List<RestaurantWithNumberUser> restaurantWithNumberUsers, OnRestaurantClickListener listener) {
        this.restaurantWithNumberUsers = restaurantWithNumberUsers;
        this.mListener = listener;

    }



    public interface OnRestaurantClickListener {
        void onRestaurantClicked(RestaurantWithNumberUser restaurantWithNumberUser);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public ListRestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FragmentListRestaurantsItemBinding fragmentListRestaurantsItemBinding = FragmentListRestaurantsItemBinding.inflate(layoutInflater, parent, false);
        return new ListRestaurantsViewHolder(fragmentListRestaurantsItemBinding);
    }

    @Override
    public void onBindViewHolder(ListRestaurantsViewHolder holder, int position) {
        // Get the restaurant data at the current position
        RestaurantWithNumberUser restaurant = restaurantWithNumberUsers.get(position);
        // Bind the restaurant data to the holder views

        // Set Name
        holder.getBinding().itemListRestaurantName.setText(restaurant.getRestaurant().getRestaurantName());

        // Set Address
        holder.getBinding().itemListRestaurantAddress.setText(restaurant.getRestaurant().getRestaurantAddress());

        // Set Image
        ImageUtils.loadRestaurantImage(holder.getBinding().itemListRestaurantPicture, restaurant.getRestaurant());

        // Set Rating
        RatingUtils.setRating(holder.getBinding().itemListRestaurantRatingBar, restaurant.getRestaurant().getRating());

        // Set Open or Close status
        holder.getBinding().itemListRestaurantOpening.setText(String.valueOf(RestaurantStatusUtils.getOpeningStatus(holder.getBinding().getRoot().getContext(), restaurant.getRestaurant())));

        // Set the number of users who chose this restaurant

        holder.getBinding().itemListRestaurantPersonNumber.setText(String.valueOf(restaurant.getNumberUser()));



        // Set Distance
        holder.getBinding().itemListRestaurantDistance.setText(String.valueOf(restaurant.getRestaurant().getDistanceKm() + " m"));

        // Set the click listener
        holder.itemView.setOnClickListener(v -> mListener.onRestaurantClicked(restaurant));
    }

    public void sortByDistance() {
        Collections.sort(restaurantWithNumberUsers, new Comparator<RestaurantWithNumberUser>() {
            @Override
            public int compare(RestaurantWithNumberUser r1, RestaurantWithNumberUser r2) {
                return Double.compare(r1.getRestaurant().getDistanceKm(), r2.getRestaurant().getDistanceKm());
            }
        });
        notifyDataSetChanged();
    }

    public void sortByRating() {
        // Sort the restaurant list by rating
        Collections.sort(restaurantWithNumberUsers, new Comparator<RestaurantWithNumberUser>() {
            @Override
            public int compare(RestaurantWithNumberUser r1, RestaurantWithNumberUser r2) {
                Float rating1 = r1.getRestaurant().getRating();
                Float rating2 = r2.getRestaurant().getRating();


                if (rating1 == null && rating2 == null) {
                    return 0;
                } else if (rating1 == null) {
                    return 1;
                } else if (rating2 == null) {
                    return -1;
                }

                return Float.compare(rating2, rating1); // Note: reversed for descending
            }
        });
        notifyDataSetChanged();
    }


    public void sortAlphabetically() {
        // Sort the restaurant list alphabetically by name
        Collections.sort(restaurantWithNumberUsers, new Comparator<RestaurantWithNumberUser>() {
            @Override
            public int compare(RestaurantWithNumberUser r1, RestaurantWithNumberUser r2) {
                return r1.getRestaurant().getRestaurantName().compareToIgnoreCase(r2.getRestaurant().getRestaurantName());
            }
        });
        notifyDataSetChanged();
    }




    @Override
    // Return the total number of items in the list
    public int getItemCount() {
        return restaurantWithNumberUsers.size();
    }

}
