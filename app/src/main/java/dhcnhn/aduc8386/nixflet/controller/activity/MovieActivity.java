package dhcnhn.aduc8386.nixflet.controller.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.helper.FirebaseAuthHelper;
import dhcnhn.aduc8386.nixflet.helper.FirebaseStorageHelper;
import dhcnhn.aduc8386.nixflet.helper.SharedPreferencesHelper;

public class MovieActivity extends AppCompatActivity {
    private ImageView imageViewBackButton;
    private ImageView imageViewProfileManage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        bindView();
    }

    private void bindView() {
        imageViewBackButton = findViewById(R.id.imageview_movie_back_button);
        imageViewProfileManage = findViewById(R.id.imageview_movie_account_icon);
        setProfilePicture();

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void setProfilePicture() {
        String uri = SharedPreferencesHelper.getUserProfilePicture();
        Glide.with(this)
                .load(uri)
                .error(R.drawable.user_avatar_holder)
                .centerCrop()
                .into(imageViewProfileManage);
    }
}
