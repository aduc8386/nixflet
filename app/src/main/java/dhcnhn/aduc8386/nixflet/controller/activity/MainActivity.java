package dhcnhn.aduc8386.nixflet.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.controller.fragment.SearchingFragment;
import dhcnhn.aduc8386.nixflet.helper.SharedPreferencesHelper;

public class MainActivity extends AppCompatActivity {

    public static final String MOVIE_ID = "MOVIE_ID";
    public static final String IS_MOVIE = "IS_MOVIE";

    private ImageView imageViewProfileManage;
    private ImageView imageViewSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();
    }

    private void bindView() {
        imageViewProfileManage = findViewById(R.id.imageview_main_account_icon);
        imageViewSearch = findViewById(R.id.imageview_main_search_icon);

        setProfilePicture();

        imageViewProfileManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchingActivity.class);
                startActivity(intent);
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