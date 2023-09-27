package com.metanoiasystem.go4lunchxoc.view.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentListRestaurantsBinding;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentListRestaurantsItemBinding;
import com.metanoiasystem.go4lunchxoc.utils.ImageUtils;
import com.metanoiasystem.go4lunchxoc.utils.RatingUtils;
import com.metanoiasystem.go4lunchxoc.view.viewholders.ListRestaurantsViewHolder;

import java.util.List;

public class ListRestaurantsAdapter extends RecyclerView.Adapter<ListRestaurantsViewHolder> {

    private final List<Restaurant> restaurants;

    private final OnRestaurantClickListener mListener;



    public ListRestaurantsAdapter(List<Restaurant> restaurants, OnRestaurantClickListener listener) {
        this.restaurants = restaurants;
        this.mListener = listener;
    }

    public interface OnRestaurantClickListener {
        void onRestaurantClicked(Restaurant restaurant);
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
        Restaurant restaurant = restaurants.get(position);

        // Set Name

        holder.getBinding().itemListRestaurantName.setText(restaurant.getRestaurantName());

        // Set Address

        holder.getBinding().itemListRestaurantAddress.setText(restaurant.getRestaurantAddress());

        // Set Image

        ImageUtils.loadRestaurantImage(holder.getBinding().itemListRestaurantPicture, restaurant);

        // Set Rating

        RatingUtils.setRating(holder.getBinding().itemListRestaurantRatingBar, restaurant.getRating());

        // Set Open or close



        // Set User Number who choose this restaurant




        // Set Distance
        holder.getBinding().itemListRestaurantDistance.setText(String.valueOf(restaurant.getDistanceKm() + " m"));




        holder.itemView.setOnClickListener(v -> {
            mListener.onRestaurantClicked(restaurant);
        });


    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}
