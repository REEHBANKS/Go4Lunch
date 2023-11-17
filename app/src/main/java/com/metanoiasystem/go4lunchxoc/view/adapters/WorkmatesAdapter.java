package com.metanoiasystem.go4lunchxoc.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.UserAndPictureWithYourSelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentWorkmatesItemBinding;
import com.metanoiasystem.go4lunchxoc.view.viewholders.WorkmatesViewHolder;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesViewHolder>{

    private final List<UserAndPictureWithYourSelectedRestaurant> users;

    // Constructor to initialize the adapter with a list of users
    public WorkmatesAdapter(List<UserAndPictureWithYourSelectedRestaurant> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for individual list items
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FragmentWorkmatesItemBinding binding = FragmentWorkmatesItemBinding.inflate(layoutInflater, parent, false);
        return new WorkmatesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {
        // Get the user at the current position
        UserAndPictureWithYourSelectedRestaurant user = users.get(position);

        // Set user's name and restaurant details in the view
        Context context = holder.itemView.getContext();
        holder.getBinding().userNameFragmentWorkMates.setText(user.getUsername());
        if (user.getRestaurantName() != null) {
            String isEatingText = context.getString(R.string.is_eating, user.getRestaurantName());
            holder.getBinding().restaurantNameFragmentWorkMates.setText(isEatingText);
        } else {
            holder.getBinding().restaurantNameFragmentWorkMates.setText(context.getString(R.string.has_not_decided));
        }

        // Load user's profile picture using Glide, or set a default picture if not available
        if (user.getUrlPictureUser() != null) {
            Glide.with(holder.getBinding().itemUserPicture.getContext())
                    .load(user.getUrlPictureUser())
                    .transform(new CircleCrop())
                    .into(holder.getBinding().itemUserPicture);
        } else {
            holder.getBinding().itemUserPicture.setImageResource(R.drawable.mbappe_picture);
        }
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the list
        return users.size();
    }
}

