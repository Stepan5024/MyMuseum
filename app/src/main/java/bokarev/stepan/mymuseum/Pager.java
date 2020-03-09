package bokarev.stepan.mymuseum;


import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;




public class Pager extends FragmentStatePagerAdapter {

    private String tabTitles[] = new String[]{"КИЛО", "ШАГИ", "ГРАФИК"};

    int tabCount;
    Context context;

    public Pager(FragmentManager fm, int tabCount, Context context) {
        super(fm);
        this.context = context;
        //количество вкладок
        this.tabCount = tabCount;
    }


    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ExpeditionFragment tab1 = new ExpeditionFragment();

                return tab1;
            case 1:
                ModeOfLifePolernikFragment tab2 = new ModeOfLifePolernikFragment();

                return tab2;
            case 2:
                AboutStationFragment tab3 = new AboutStationFragment();

                return tab3;
            case 3:
                FloraAndAnimalFragment tab4 = new FloraAndAnimalFragment();

                return tab4;
            case 4:
                ClothesFragment tab5 = new ClothesFragment();

                return tab5;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // генерируем заголовок в зависимости от позиции
        return tabTitles[position];
    }

    @Override
    public int getCount() {

        return tabCount;
    }


}