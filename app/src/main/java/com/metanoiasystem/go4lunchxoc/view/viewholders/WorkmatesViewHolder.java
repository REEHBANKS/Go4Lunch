package com.metanoiasystem.go4lunchxoc.view.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.metanoiasystem.go4lunchxoc.databinding.FragmentListRestaurantsItemBinding;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentWorkmatesItemBinding;

public class WorkmatesViewHolder extends RecyclerView.ViewHolder {


    FragmentWorkmatesItemBinding binding;

    public FragmentWorkmatesItemBinding getBinding() {
        return binding;
    }

    public WorkmatesViewHolder(FragmentWorkmatesItemBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
