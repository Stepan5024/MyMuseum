package bokarev.stepan.mymuseum;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static bokarev.stepan.mymuseum.Constants.a23;
import static bokarev.stepan.mymuseum.FragmentCard.initPlayer;
import static bokarev.stepan.mymuseum.FragmentCard.mMediaPlayer;
import static bokarev.stepan.mymuseum.FragmentCard.viewPager;


public class ExponatMedia extends Fragment {

    View view;

    public static Fragment newInstance() {
        ExponatMedia fragment = new ExponatMedia();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frahment_media, container, false);
        //mMediaPlayer.pause();
        MainActivity.fragmentIs = a23;


        return view;
    }
}
