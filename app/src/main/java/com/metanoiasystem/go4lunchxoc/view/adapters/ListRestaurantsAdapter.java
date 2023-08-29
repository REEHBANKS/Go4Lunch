package com.metanoiasystem.go4lunchxoc.view.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metanoiasystem.go4lunchxoc.view.viewholders.ListRestaurantsViewHolder;

public class ListRestaurantsAdapter extends RecyclerView.Adapter<ListRestaurantsViewHolder> {





    @NonNull
    @Override
    public ListRestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FragmentListItemBinding fragmentListItemBinding = FragmentListItemBinding.inflate(layoutInflater, parent, false);
        return new ListRestaurantsViewHolder(fragmentListItemBinding);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    static String statusRestaurant = "";

    public static String openOrClose(RestaurantWithNumberUser restaurant) {
        if (restaurant.getRestaurant().getOpeningHours()) {
            statusRestaurant = "Ouvert";
        } else {
            statusRestaurant = "Fermé";
        }
        return statusRestaurant;
    }


}
