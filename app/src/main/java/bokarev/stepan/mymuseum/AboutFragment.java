package bokarev.stepan.mymuseum;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;

public class AboutFragment extends Fragment {

    View view;
    public static Fragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);
       // getActivity().setExitSharedElementCallback((Toolbar) view.findViewById(R.id.toolbar));
        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbar.setTitle("ff");
        return view;
    }
}
