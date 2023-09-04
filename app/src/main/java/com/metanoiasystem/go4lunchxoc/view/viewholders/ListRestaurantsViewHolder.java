package com.metanoiasystem.go4lunchxoc.view.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metanoiasystem.go4lunchxoc.databinding.FragmentListRestaurantsBinding;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentListRestaurantsItemBinding;

public class ListRestaurantsViewHolder extends RecyclerView.ViewHolder {



    FragmentListRestaurantsItemBinding binding;

    public FragmentListRestaurantsItemBinding getBinding() {
        return binding;
    }

    public ListRestaurantsViewHolder(FragmentListRestaurantsItemBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
