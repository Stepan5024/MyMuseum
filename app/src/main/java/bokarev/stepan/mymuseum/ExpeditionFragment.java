package bokarev.stepan.mymuseum;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ExpeditionFragment extends Fragment {

    View view;
    public static Fragment newInstance() {
        ExpeditionFragment fragment = new ExpeditionFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_open_materic, container, false);



        return view;
    }
}
