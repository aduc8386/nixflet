package dhcnhn.aduc8386.nixflet.helper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseHelper {

    private static FirebaseDatabase instance;

    private static DatabaseReference users = getInstance().getReference("users");
    private static DatabaseReference movies = getInstance().getReference("movies");

    public static FirebaseDatabase getInstance(){
        if(instance == null) {
            instance = FirebaseDatabase.getInstance();
        }
        return instance;
    }

    public static DatabaseReference getUserReference() {
        return users;
    }

    public static DatabaseReference getMovieReference() {
        return movies;
    }

    public static DatabaseReference getSpecificMovieReference(String movieId) {
        String path = String.format("movies/%s", movieId);
        return getInstance().getReference(path);
    }

}
