package bokarev.stepan.mymuseum;


import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import static bokarev.stepan.mymuseum.FragmentCard.audioName;
import static bokarev.stepan.mymuseum.FragmentCard.initPlayer;
import static bokarev.stepan.mymuseum.FragmentCard.myUri;
import static bokarev.stepan.mymuseum.FragmentCard.viewPager;


public class ModeOfLifePolernikFragment extends Fragment {

    View view;

    public static Fragment newInstance() {
        ModeOfLifePolernikFragment fragment = new ModeOfLifePolernikFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_life_polyarnics, container, false);

        Button but = view.findViewById(R.id.button);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPlayer(viewPager.getCurrentItem());
            }
        });


        return view;
    }
}
