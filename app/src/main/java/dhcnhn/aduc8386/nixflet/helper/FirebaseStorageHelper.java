package dhcnhn.aduc8386.nixflet.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class FirebaseStorageHelper {

    private static FirebaseStorage instance;

    public static FirebaseStorage getInstance() {
        if(instance == null) {
            instance = FirebaseStorage.getInstance("gs://movie-app-nixflet.appspot.com");
        }
        return instance;
    }

    public static void setProfilePicture(Uri profile) {
        if(FirebaseAuthHelper.getCurrentUser() != null) {
            StorageReference storageReference = getInstance().getReference().child("profile_images/" + FirebaseAuthHelper.getCurrentUser().getUid());

            storageReference.putFile(profile).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("TAG", "onSuccess: " + uri);
                            SharedPreferencesHelper.setUserProfilePicture(uri.toString());
                        }
                    });
                }
            });
        }
    }

}
