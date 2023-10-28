package com.metanoiasystem.go4lunchxoc.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.data.models.UserAndPictureWithYourSelectedRestaurant;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentWorkmatesItemBinding;
import com.metanoiasystem.go4lunchxoc.view.viewholders.WorkmatesViewHolder;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesViewHolder>{


    private final List<UserAndPictureWithYourSelectedRestaurant> users;

    public WorkmatesAdapter(List<UserAndPictureWithYourSelectedRestaurant> users) {
        this.users = users;
    }


    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FragmentWorkmatesItemBinding fragmentWorkmatesItemBinding = FragmentWorkmatesItemBinding.inflate(layoutInflater,parent,false);
        return new WorkmatesViewHolder(fragmentWorkmatesItemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {
        UserAndPictureWithYourSelectedRestaurant user = users.get(position);


        // Obtenez le contexte à partir de holder.itemView
        Context context = holder.itemView.getContext();

        //set name
        holder.getBinding().userNameFragmentWorkMates.setText(user.getUsername());

        if (user.getRestaurantName() != null) {
            String isEatingText = context.getString(R.string.is_eating, user.getRestaurantName());
            holder.getBinding().restaurantNameFragmentWorkMates.setText(isEatingText);
            holder.getBinding().restaurantNameFragmentWorkMates.setTextColor(context.getResources().getColor(R.color.app_color));
        } else {
            holder.getBinding().restaurantNameFragmentWorkMates.setText(context.getString(R.string.has_not_decided));
        }


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
        return users.size();
    }
}
