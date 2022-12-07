package dhcnhn.aduc8386.nixflet.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import dhcnhn.aduc8386.nixflet.R;

public class MainActivity extends AppCompatActivity {

    public static final String MOVIE_ID = "MOVIE_ID";
    public static final String IS_MOVIE = "IS_MOVIE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}