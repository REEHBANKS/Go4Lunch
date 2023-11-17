package com.metanoiasystem.go4lunchxoc.domain.usecase;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.utils.callbacks.CallbackUserUseCase;

public class UpdateUserViewDrawerUseCase {

    // Use case for fetching complete user data.
    private final GetCompleteUserDataUseCase getCompleteUserDataUseCase;

    // Constructor initializing with the get complete user data use case.
    public UpdateUserViewDrawerUseCase(GetCompleteUserDataUseCase getCompleteUserDataUseCase){
        this.getCompleteUserDataUseCase = getCompleteUserDataUseCase;
    }

    // Updates the user view in the navigation drawer with user's data.
    public void updateUserView(NavigationView navigationView) {
        getCompleteUserDataUseCase.execute(new CallbackUserUseCase<User>() {
            @Override
            public void onUserDataFetched(User user) {
                if (user != null) {
                    // Update the navigation view with user's information.
                    View headerView = navigationView.getHeaderView(0);
                    TextView navUserName = headerView.findViewById(R.id.name_nav_header);
                    TextView navUserMail = headerView.findViewById(R.id.mail_nav_header);
                    ImageView navUserPicture = headerView.findViewById(R.id.picture_nav_header);

                    navUserName.setText(user.getUsername());
                    navUserMail.setText(user.getUserMail());

                    // Use Glide to load the user's profile picture.
                    if (user.getUrlPictureUser() != null && !user.getUrlPictureUser().isEmpty()) {
                        Glide.with(navigationView.getContext())
                                .load(user.getUrlPictureUser())
                                .apply(RequestOptions.circleCropTransform())
                                .into(navUserPicture);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                // Handle any errors during the user data fetch.
            }
        });
    }
}

