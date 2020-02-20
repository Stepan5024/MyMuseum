package bokarev.stepan.mymuseum;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ExcurtionFragment extends Fragment {

    View view;

    public static Fragment newInstance() {

        ExcurtionFragment fragment = new ExcurtionFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_excursion, container, false);
       Button but = view.findViewById(R.id.getExcurtion);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(FragmentCard.newInstance());
            }
        });

        return view;
    }
    public void loadFragment(Fragment fragment) {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentView, fragment);
        ft.commit();

    }
}
