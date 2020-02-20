package bokarev.stepan.mymuseum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    public static String pacageName;
    public static Uri myUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        loadFragment(ExcurtionFragment.newInstance());

        String test = getPackageName();
        pacageName = getPackageName();
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.i("//////", "SD-карта не доступна: " + Environment.getExternalStorageState());

        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + "aDIR_SD");
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, "FILENAME_SD");

        File externalAppDir = new File("/Android/data/bokarev.stepan.mymuseum/");
        if (!externalAppDir.exists()) {
            externalAppDir.mkdir();
        }

        String src = "https://firebasestorage.googleapis.com/v0/b/procao.appspot.com/o/AUDIO%2Fhouselebedi.mp3?alt=media&token=9cf33edd-379e-4cce-bdc9-2a5c2fef1b7b";
        File dest = new File("/Android/data/bokarev.stepan.mymuseum/" + "/files/");


        String FileName = "FileName.mp4";
        File file = new File(dest, FileName);
        new LoadFile(src, file).start();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    private class LoadFile extends Thread {
        private final String src;
        private final File dest;

        LoadFile(String src, File dest) {
            this.src = src;
            this.dest = dest;
        }

        private void onDownloadComplete(boolean success) {
            // файл скачался, можно как-то реагировать
            Log.i("***", "************** " + success);
        }

        @Override
        public void run() {
            try {
                FileUtils.copyURLToFile(new URL(src), dest);
                onDownloadComplete(true);
            } catch (IOException e) {
                e.printStackTrace();
                onDownloadComplete(false);
            }
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.menu_events:
                    loadFragment(ExcurtionFragment.newInstance());
                    return true;
                case R.id.menu_search:
                    loadFragment(AboutFragment.newInstance());
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentView, fragment);
        ft.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
