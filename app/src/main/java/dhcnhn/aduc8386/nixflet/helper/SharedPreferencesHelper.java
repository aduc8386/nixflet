package dhcnhn.aduc8386.nixflet.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import dhcnhn.aduc8386.nixflet.model.User;

public class SharedPreferencesHelper {

    public static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";
    private static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_PROFILE_PICTURE = "USER_PROFILE_PICTURE";

    private static SharedPreferences instance;

    public static void init(Context context) {
        if (instance == null) {
            instance = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        }
    }

    public static SharedPreferences getInstance() {
        return instance;
    }

    public static void setUsername(String username) {
        instance.edit().putString(USER_NAME, username).apply();
    }

    public static void setUserEmail(String email) {
        instance.edit().putString(USER_EMAIL, email).apply();
    }

    public static void setUserId(String id) {
        instance.edit().putString(USER_ID, id).apply();
    }

    public static void setUserProfilePicture(String userProfilePicture) {
        instance.edit().putString(USER_PROFILE_PICTURE, userProfilePicture).apply();
    }

    public static String getUserName() {
        return instance.getString(USER_NAME, "");
    }

    public static String getUserEmail() {
        return instance.getString(USER_EMAIL, "");
    }

    public static String getUserId() {
        return instance.getString(USER_ID, "");
    }

    public static String getUserProfilePicture() {
        return instance.getString(USER_PROFILE_PICTURE, "");
    }

    public static void setUser(User user) {
        setUsername(user.getFullName());
        setUserEmail(user.getEmail());
        setUserId(user.getId());

        Log.d("TAG", "setUser: user saved to shared preferences");
    }

    public static User getUser() {
        String userName = getUserName();
        String email = getUserEmail();
        String id = getUserId();
        String profilePicture = getUserProfilePicture();

        return new User(id, userName, email, profilePicture);
    }

}
