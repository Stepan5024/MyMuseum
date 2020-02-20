package bokarev.stepan.mymuseum;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class FragmentCard extends Fragment {
    View view;
    public static Fragment newInstance() {
        FragmentCard fragment = new FragmentCard();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_card, container, false);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);


        Pager adapter = new Pager(getActivity().getSupportFragmentManager(), 3);
        //установка adapter to pager
        viewPager.setClipToPadding(false);
        // отступ от родителя элемента
        viewPager.setPadding(60, 0, 60, 0);
        // отступ между друг другом
        viewPager.setPageMargin(8);
        viewPager.setAdapter(adapter);
        return viewPager;
    }
}
