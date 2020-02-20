package bokarev.stepan.mymuseum;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;




public class Pager extends FragmentStatePagerAdapter {

    private String tabTitles[] = new String[]{"КИЛО", "ШАГИ", "ГРАФИК"};

    int tabCount;


    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //оличество вкладок
        this.tabCount = tabCount;
    }


    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentCard tab1 = new FragmentCard();
                return tab1;
            case 1:
                FragmentCard tab2 = new FragmentCard();
                return tab2;
            case 2:
                FragmentCard tab3 = new FragmentCard();
                return tab3;

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