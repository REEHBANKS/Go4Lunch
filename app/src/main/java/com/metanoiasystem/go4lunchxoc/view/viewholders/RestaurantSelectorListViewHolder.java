package com.metanoiasystem.go4lunchxoc.view.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.metanoiasystem.go4lunchxoc.databinding.FragmentListRestaurantsItemBinding;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentRestaurantSelectorListBinding;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentRestaurantSelectorListItemBinding;

public class RestaurantSelectorListViewHolder extends RecyclerView.ViewHolder {

    FragmentRestaurantSelectorListItemBinding binding;

    public  FragmentRestaurantSelectorListItemBinding getBinding(){
        return binding;
    }


    public RestaurantSelectorListViewHolder( FragmentRestaurantSelectorListItemBinding itemView) {
        super(itemView.getRoot());
        this.binding= itemView;
    }
}
