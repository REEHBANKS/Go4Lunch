package com.metanoiasystem.go4lunchxoc.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.metanoiasystem.go4lunchxoc.R;
import com.metanoiasystem.go4lunchxoc.data.models.User;
import com.metanoiasystem.go4lunchxoc.databinding.FragmentWorkmatesBinding;
import com.metanoiasystem.go4lunchxoc.domain.usecase.CreateUserUseCase;
import com.metanoiasystem.go4lunchxoc.domain.usecase.FetchAllUsersUseCase;
import com.metanoiasystem.go4lunchxoc.utils.Injector;
import com.metanoiasystem.go4lunchxoc.view.adapters.WorkmatesAdapter;
import com.metanoiasystem.go4lunchxoc.viewmodels.UserViewModel;
import com.metanoiasystem.go4lunchxoc.viewmodels.viewModelFactory.UserViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding binding;
    private WorkmatesAdapter adapter;
    private UserViewModel userViewModel;
    private List<User> users;

   @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instanciation du ViewModel ici (en utilisant l'Injector et la Factory)
        CreateUserUseCase createUserUseCase = Injector.provideCreateUserUseCase();
        FetchAllUsersUseCase fetchAllUsersUseCase = Injector.provideFetchAllUsersUseCase();

        UserViewModelFactory factory = new UserViewModelFactory(createUserUseCase, fetchAllUsersUseCase);
        userViewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);

        // Configuration du RecyclerView
        setupRecyclerView();

        // Observer les données du ViewModel
        observeViewModel();

        // Récupération des utilisateurs
        userViewModel.fetchAllUsers();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        this.users = new ArrayList<>();
        this.adapter = new WorkmatesAdapter(this.users);
        binding.fragmentListWorkmates.setAdapter(this.adapter);
        this.binding.fragmentListWorkmates.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    @SuppressLint("NotifyDataSetChanged")
    private void updateUI(List<User> theUsers){
        users.addAll(theUsers);
        adapter.notifyDataSetChanged();
    }

    private void observeViewModel() {
        userViewModel.getUsers().observe(getViewLifecycleOwner(), this::updateUI);

        userViewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
            // Show the error message to the user
            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
        });

    }

}
