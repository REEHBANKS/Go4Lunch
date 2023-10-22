package com.metanoiasystem.go4lunchxoc.view.adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.metanoiasystem.go4lunchxoc.data.models.Restaurant;
import com.metanoiasystem.go4lunchxoc.data.models.RestaurantWithNumberUser;
import com.metanoiasystem.go4lunchxoc.data.models.SelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentListRestaurantsBinding;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentListRestaurantsItemBinding;
import com.metanoiasystem.go4lunchxoc.utils.ImageUtils;
import com.metanoiasystem.go4lunchxoc.utils.RatingUtils;
import com.metanoiasystem.go4lunchxoc.utils.RestaurantStatusUtils;
import com.metanoiasystem.go4lunchxoc.view.viewholders.ListRestaurantsViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ListRestaurantsAdapter extends RecyclerView.Adapter<ListRestaurantsViewHolder> {

    private final List<RestaurantWithNumberUser> restaurantWithNumberUsers;
    private final OnRestaurantClickListener mListener;

    public ListRestaurantsAdapter(List<RestaurantWithNumberUser> restaurantWithNumberUsers, OnRestaurantClickListener listener) {
        this.restaurantWithNumberUsers = restaurantWithNumberUsers;;
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
        RestaurantWithNumberUser restaurant = restaurantWithNumberUsers.get(position);



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

    @Override
    public int getItemCount() {
        return restaurantWithNumberUsers.size();
    }

}
