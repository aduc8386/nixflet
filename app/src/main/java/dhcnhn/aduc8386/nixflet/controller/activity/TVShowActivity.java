package dhcnhn.aduc8386.nixflet.controller.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dhcnhn.aduc8386.nixflet.R;

public class TVShowActivity extends AppCompatActivity {

    private ImageView imageViewBackButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show);

        bindView();
    }

    private void bindView() {
        imageViewBackButton = findViewById(R.id.imageview_tv_show_back_button);

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
