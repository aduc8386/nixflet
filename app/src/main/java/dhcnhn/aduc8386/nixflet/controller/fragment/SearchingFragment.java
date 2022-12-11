package dhcnhn.aduc8386.nixflet.controller.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dhcnhn.aduc8386.nixflet.R;

public class SearchingFragment extends Fragment {

    private ImageView imageViewBackButton;
    private ImageView imageViewSearchButton;
    private EditText editTextSearchInput;

    public SearchingFragment() {
        super(R.layout.activity_searching);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView(view);
    }

    private void bindView(View view) {
        imageViewBackButton = view.findViewById(R.id.imageview_searching_back_icon);
        imageViewSearchButton = view.findViewById(R.id.imageview_searching_search_icon);
        editTextSearchInput = view.findViewById(R.id.edittext_searching_input);

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchingFragment.this.getParentFragmentManager().popBackStack();
            }
        });
    }
}
