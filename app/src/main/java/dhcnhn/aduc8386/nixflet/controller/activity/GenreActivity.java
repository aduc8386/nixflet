package dhcnhn.aduc8386.nixflet.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.bumptech.glide.Glide;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.controller.fragment.GenreFragment;
import dhcnhn.aduc8386.nixflet.helper.SharedPreferencesHelper;

public class GenreActivity extends AppCompatActivity {

    public static final String GENRE_NAME = "GENRE_NAME";
    public static final String IS_MOVIE = "IS_MOVIE";
    public static final String GENRE_ID = "GENRE_ID";

    private String genreName;
    private String genreId;
    private boolean isMovie;

    private TextView textViewGenre;
    private ImageView imageViewBackButton;
    private ImageView imageViewProfileManage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);

        bindView();

    }

    private void bindView() {
        setFragment();
        textViewGenre = findViewById(R.id.textview_genre_genre);
        imageViewBackButton = findViewById(R.id.imageview_genre_back_button);
        imageViewProfileManage = findViewById(R.id.imageview_genre_account_icon);

        setProfilePicture();
        textViewGenre.setText(genreName);

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imageViewProfileManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GenreActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setFragment() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            genreName = bundle.getString(GENRE_NAME);
            genreId = bundle.getString(GENRE_ID);
            isMovie = bundle.getBoolean(IS_MOVIE);
        } else {
            genreName = "";
        }

        Bundle bundle = new Bundle();

        bundle.putString(GENRE_NAME, genreName);
        bundle.putString(GENRE_ID, genreId);
        bundle.putBoolean(IS_MOVIE, isMovie);

        GenreFragment genreFragment = new GenreFragment();
        genreFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_genre_fragment_container, genreFragment)
                .commit();
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
