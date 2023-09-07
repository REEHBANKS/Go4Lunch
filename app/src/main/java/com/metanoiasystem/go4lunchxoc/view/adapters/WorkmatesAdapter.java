package com.metanoiasystem.go4lunchxoc.view.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentListRestaurantsItemBinding;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentWorkmatesItemBinding;
import com.metanoiasystem.go4lunchxoc.view.viewholders.ListRestaurantsViewHolder;
import com.metanoiasystem.go4lunchxoc.view.viewholders.WorkmatesViewHolder;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesViewHolder>{


    private final List<User> users;

    public WorkmatesAdapter(List<User> users) {
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
        User user = users.get(position);

        //set name
        holder.getBinding().userNameFragmentWorkMates.setText(user.getUsername());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
