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

    private final GetCompleteUserDataUseCase getCompleteUserDataUseCase;

    public UpdateUserViewDrawerUseCase(){
        getCompleteUserDataUseCase = Injector.provideGetCompleteUserDataUseCase();
    }

    public void updateUserView(NavigationView navigationView) {
        getCompleteUserDataUseCase.execute( new CallbackUserUseCase<User>() {
            @Override
            public void onUserDataFetched(User user) {
                if (user != null) {
                    // Mettez à jour la vue avec les données de l'utilisateur
                    View headerView = navigationView.getHeaderView(0);
                    TextView navUserName = headerView.findViewById(R.id.name_nav_header);
                    TextView navUserMail = headerView.findViewById(R.id.mail_nav_header);
                    ImageView navUserPicture = headerView.findViewById(R.id.picture_nav_header);

                    navUserName.setText(user.getUsername());
                    navUserMail.setText(user.getUserMail());

                    // Utilisez Glide pour charger l'image de profil de l'utilisateur
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

            }
        });

    }


}
