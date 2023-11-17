package com.metanoiasystem.go4lunchxoc.data.repository;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.metanoiasystem.go4lunchxoc.data.models.User;
public class UserRepository {

    // Singleton instance of UserRepository.
    private static volatile UserRepository instance;
    private static final String COLLECTION_NAME = "users";

    // Private constructor to prevent direct instantiation.
    private UserRepository() {
    }

    // Singleton method to get the instance of UserRepository.
    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }

    // Returns the collection reference for users in Firestore.
    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Creates a new user in Firestore if not already present.
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            this.getUsersCollection().document(uid).get().addOnSuccessListener(documentSnapshot -> {
                if (!documentSnapshot.exists()) {
                    String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
                    String username = user.getDisplayName();
                    String userMail = user.getEmail();
                    User userToCreate = new User(uid, username, userMail, urlPicture);
                    this.getUsersCollection().document(uid).set(userToCreate);
                }
            }).addOnFailureListener(e -> {
                // Error handling for user creation failure.
            });
        }
    }

    // Retrieves the current user's data from Firestore.
    public Task<DocumentSnapshot> getUserData() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getUsersCollection().document(uid).get();
        } else {
            return Tasks.forException(new NullPointerException("UID is null"));
        }
    }

    // Gets the UID of the currently authenticated Firebase user.
    @Nullable
    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    // Returns the currently authenticated Firebase user.
    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    // Fetches data of all users from Firestore.
    public Task<QuerySnapshot> getAllUsersData() {
        return getUsersCollection().get();
    }
}

