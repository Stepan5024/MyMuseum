package bokarev.stepan.mymuseum;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import static bokarev.stepan.mymuseum.FragmentCard.initPlayer;
import static bokarev.stepan.mymuseum.FragmentCard.viewPager;

public class AboutStationFragment extends Fragment {

    View view;
    public static Fragment newInstance() {
        AboutStationFragment fragment = new AboutStationFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stations, container, false);


        CardView card = view.findViewById(R.id.stationCard);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(ExponatMedia.newInstance());
            }
        });
        TextView tv = view.findViewById(R.id.mainText12);
        tv.setText("\"Станции\"");
        Button but = view.findViewById(R.id.button);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPlayer(viewPager.getCurrentItem());
            }
        });

        return view;
    }


    public void loadFragment(Fragment fragment) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentView, fragment);
        ft.commit();

    }
}
