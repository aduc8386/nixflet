package dhcnhn.aduc8386.nixflet.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthHelper {

    private static FirebaseAuth instance;

    public static FirebaseAuth getInstance() {
        if(instance == null) {
            instance = FirebaseAuth.getInstance();
        }
        return instance;
    }

    public static FirebaseUser getCurrentUser() {
        return getInstance().getCurrentUser();
    }

}
