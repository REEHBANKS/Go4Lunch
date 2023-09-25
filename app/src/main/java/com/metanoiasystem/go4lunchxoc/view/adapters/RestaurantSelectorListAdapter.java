package com.metanoiasystem.go4lunchxoc.view.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentRestaurantSelectorListItemBinding;
import com.metanoiasystem.go4lunchxoc.view.viewholders.RestaurantSelectorListViewHolder;

import java.util.List;

public class RestaurantSelectorListAdapter extends RecyclerView.Adapter<RestaurantSelectorListViewHolder> {

    private final List<User> users;

    public RestaurantSelectorListAdapter(List<User> users) {this.users = users;}


    @NonNull
    @Override
    public RestaurantSelectorListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FragmentRestaurantSelectorListItemBinding fragmentRestaurantSelectorListItemBinding
                = FragmentRestaurantSelectorListItemBinding.inflate(layoutInflater,parent,false);

        return new RestaurantSelectorListViewHolder(fragmentRestaurantSelectorListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantSelectorListViewHolder holder, int position) {

        User user = users.get(position);

        holder.getBinding().textUserRestaurantSelectorDetail.setText(user.getUsername());


        if (user.getUrlPictureUser() != null) {
            Glide.with(holder.getBinding().pictureUserRestaurantSelectorDetail.getContext())
                    .load(user.getUrlPictureUser())
                    .into(holder.getBinding().pictureUserRestaurantSelectorDetail);
        } else {
            holder.getBinding().pictureUserRestaurantSelectorDetail.setImageResource(R.drawable.mbappe_picture);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
