package dhcnhn.aduc8386.nixflet.controller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.helper.FirebaseAuthHelper;
import dhcnhn.aduc8386.nixflet.helper.FirebaseStorageHelper;
import dhcnhn.aduc8386.nixflet.helper.SharedPreferencesHelper;

public class MainActivity extends AppCompatActivity {

    public static final String MOVIE_ID = "MOVIE_ID";
    public static final String IS_MOVIE = "IS_MOVIE";

    private ImageView imageViewProfileManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();
    }

    private void bindView() {
        imageViewProfileManage = findViewById(R.id.imageview_main_account_icon);
        setProfilePicture();

        imageViewProfileManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuthHelper.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, StartingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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