package dhcnhn.aduc8386.nixflet.controller.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import dhcnhn.aduc8386.nixflet.R;

public class MovieActivity extends AppCompatActivity {
    private ImageView imageViewBackButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        bindView();
    }

    private void bindView() {
        imageViewBackButton = findViewById(R.id.imageview_movie_back_button);

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
