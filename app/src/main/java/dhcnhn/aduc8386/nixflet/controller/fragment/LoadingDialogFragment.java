package dhcnhn.aduc8386.nixflet.controller.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import dhcnhn.aduc8386.nixflet.R;

public class LoadingDialogFragment extends DialogFragment {

    public final static String TAG = "LOADING_DIALOG_FRAGMENT";

    public LoadingDialogFragment() {
        super(R.layout.fragment_loading_dialog);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.LoadingDialog);
    }
}
