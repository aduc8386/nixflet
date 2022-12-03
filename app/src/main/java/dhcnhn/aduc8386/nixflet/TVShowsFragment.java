package dhcnhn.aduc8386.nixflet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TVShowsFragment extends Fragment {

    private TextView textViewTVShows;

    public TVShowsFragment() {
        super(R.layout.fragment_tv_shows);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        bindView(view);
    }


}
