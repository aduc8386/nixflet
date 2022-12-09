package dhcnhn.aduc8386.nixflet.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.helper.FirebaseAuthHelper;
import dhcnhn.aduc8386.nixflet.helper.SharedPreferencesHelper;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView imageViewUserProfilePicture;
    private TextView textViewUserName;
    private TextView textViewUserEmail;
    private EditText editTextUserName;
    private EditText editTextUserEmail;
    private Button buttonLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        bindView();
    }

    private void bindView() {
        imageViewUserProfilePicture = findViewById(R.id.circle_imageview_user_profile_user_profile);
        editTextUserName = findViewById(R.id.edittext_user_profile_user_name);
        editTextUserEmail = findViewById(R.id.edittext_user_profile_user_email);
        textViewUserName = findViewById(R.id.textview_user_profile_user_name);
        textViewUserEmail = findViewById(R.id.textview_user_profile_user_email);
        buttonLogout = findViewById(R.id.button_user_profile_logout_button);

        Glide.with(this)
                .load(SharedPreferencesHelper.getUserProfilePicture())
                .error(R.drawable.user_avatar_holder)
                .centerCrop()
                .into(imageViewUserProfilePicture);

        editTextUserName.setText(SharedPreferencesHelper.getUserName());
        editTextUserEmail.setText(SharedPreferencesHelper.getUserEmail());
        textViewUserName.setText(SharedPreferencesHelper.getUserName());
        textViewUserEmail.setText(SharedPreferencesHelper.getUserEmail());

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuthHelper.getInstance().signOut();
                Intent intent = new Intent(UserProfileActivity.this, StartingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

}
