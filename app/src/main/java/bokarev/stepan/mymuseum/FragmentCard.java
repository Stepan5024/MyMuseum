package bokarev.stepan.mymuseum;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import org.apache.commons.io.FileUtils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import java.io.File;
import java.io.IOException;
import java.net.URL;

import static bokarev.stepan.mymuseum.Constants.a2;

public class FragmentCard extends Fragment {


    public static SeekBar mSeekBar;
    public static TextView songTitle;

    public static MediaPlayer mMediaPlayer;

    public static TextView curTime;
    public static TextView totTime;
    public static ImageView playIcon;
    public static ImageView prevIcon;
    public static ImageView nextIcon;


    public static View view;
    public static String[] audioName = new String[7];
    public static Uri myUri;

    public static ViewPager viewPager;
    MediaPlayer mediaPlayer = new MediaPlayer();


    public static Fragment newInstance() {

        FragmentCard fragment = new FragmentCard();

        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_card, container, false);
        mSeekBar = view.findViewById(R.id.mSeekBar);
        songTitle = view.findViewById(R.id.songTitle);
        curTime = view.findViewById(R.id.curTime);
        totTime = view.findViewById(R.id.totalTime);

        playIcon = view.findViewById(R.id.playIcon);
        prevIcon = view.findViewById(R.id.prevIcon);
        nextIcon = view.findViewById(R.id.nextIcon);


        MainActivity.fragmentIs = a2;
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }


        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        prevIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewPager.getCurrentItem() == 0) {
                } else {
                    mMediaPlayer.stop();
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
                }


            }
        });

        nextIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == audioName.length) {

                } else {
                    mMediaPlayer.stop();
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);

                }

            }
        });

        initNameAudio();

        Pager adapter = new Pager(getActivity().getSupportFragmentManager(), 5, getActivity());
        //установка adapter to pager
        viewPager.setClipToPadding(false);
        // отступ от родителя элемента
        viewPager.setPadding(60, 0, 60, 0);
        // отступ между друг другом
        viewPager.setPageMargin(8);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Toast.makeText(getContext(), "onPageScrolled " +  position+ " positionOffset " + positionOffset, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {
                 mMediaPlayer = null;
                initPlayer(viewPager.getCurrentItem());
                // mMediaPlayer.pause();
                //Toast.makeText(getContext(), "onPageSelected " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Toast.makeText(getContext(), "onPageScrollStateChanged " +  state, Toast.LENGTH_SHORT).show();
            }
        });
        loadAudio();
        myUri = Uri.parse(Environment.getExternalStorageDirectory() + "/Android/data/" + getActivity().getPackageName() + "/files/" + audioName[0]);

        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);

                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    // update TextView here!
                                    // UpdateSongTime();
                                }
                            });
                        } catch (NullPointerException e) {
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();

        initPlayer(0);
        //  mMediaPlayer.stop();
        //mMediaPlayer.pause();
        //  mMediaPlayer.start();
        return view;
    }

    public static void initPlayer(final int position) {

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            // mMediaPlayer.reset();

        }

        //  String sname = allSongs.get(position).getName().replace(".mp3", "").replace(".m4a", "").replace(".wav", "").replace(".m4b", "");
        songTitle.setText("Вы можете прослушать запись нажав \"загрузить контент\"");
        // Uri songResourceUri = Uri.parse(allSongs.get(position).toString());
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(view.getContext(), Uri.parse(Environment.getExternalStorageDirectory() + "/Android/data/" + view.getContext().getPackageName() + "/files/" + audioName[position]));
            mMediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                String totalTime = createTimeLabel(mMediaPlayer.getDuration());
                totTime.setText(totalTime);
                mSeekBar.setMax(mMediaPlayer.getDuration());

                mMediaPlayer.start();
                playIcon.setImageResource(R.drawable.ic_pause_black_24dp);
                //  playIcon.setImageResource(R.drawable.ic_play_arrow_black_24dp);

            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                mMediaPlayer.stop();
                playIcon.setImageResource(R.drawable.ic_play_arrow_black_24dp);

            }
        });


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    mMediaPlayer.seekTo(progress);
                    mSeekBar.setProgress(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null) {
                    try {
//                        Log.i("Thread ", "Thread Called");
                        // create new message to send to handler
                        if (mMediaPlayer.isPlaying()) {
                            Message msg = new Message();
                            msg.what = mMediaPlayer.getCurrentPosition();
                            handler.sendMessage(msg);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            Log.i("handler ", "handler called");
            int current_position = msg.what;
            mSeekBar.setProgress(current_position);
            String cTime = createTimeLabel(current_position);
            curTime.setText(cTime);
        }
    };


    private void play() {

        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
            playIcon.setImageResource(R.drawable.ic_pause_black_24dp);
        } else {
            pause();
        }

    }

    private void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            playIcon.setImageResource(R.drawable.ic_play_arrow_black_24dp);

        }

    }


    public static String createTimeLabel(int duration) {
        String timeLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        timeLabel += min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearMediaPlayer();
    }

    private void clearMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }


    private void initNameAudio() {
        audioName[0] = "expedition.mp3";
        audioName[1] = "lifePolyarnic.mp3";
        audioName[2] = "floraAndAnimals.mp3";
        audioName[3] = "stations.mp3";
        audioName[4] = "clothes.mp3";

    }

    private void loadAudio() {
        //создание каталога хранения приложения
        File externalAppDir = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getActivity().getPackageName() + "/files");
        if (!externalAppDir.exists()) {
            externalAppDir.mkdir();
        }

        //экспедиция
        String src_about_expedition = "https://firebasestorage.googleapis.com/v0/b/procao.appspot.com/o/AUDIO%2Fhouselebedi.mp3?alt=media&token=9cf33edd-379e-4cce-bdc9-2a5c2fef1b7b";
        String FileName = audioName[0];
        File file = new File(externalAppDir, FileName);
        new LoadFile(src_about_expedition, file).start();

        //Быт, освоение
        String src_life_style = "https://firebasestorage.googleapis.com/v0/b/grandpovar.appspot.com/o/27%20%D1%84%D0%B5%D0%B2%D1%80.%2C%2021.19(2).mp3?alt=media&token=2c25b255-34bf-4b65-98ee-006b430c4ec5";
        String FileName2 = audioName[1];
        File file2 = new File(externalAppDir, FileName2);
        new LoadFile(src_life_style, file2).start();

        //музыка 1

        String src_1 = "https://firebasestorage.googleapis.com/v0/b/grandpovar.appspot.com/o/a-studio-uletayu_ostonline_net.mp3?alt=media&token=9f391d32-6ffa-40b4-89d6-4d55862a8452";
        String FileName3 = audioName[2];
        File file3 = new File(externalAppDir, FileName3);
        new LoadFile(src_1, file3).start();

        // музыка 2
        String src_2 = "https://firebasestorage.googleapis.com/v0/b/grandpovar.appspot.com/o/muzlome_MOZGI_-_Vertoljot_49755028.mp3?alt=media&token=2eccb146-3083-4122-aa2f-12e054e25cf9";
        String FileName4 = audioName[3];
        File file4 = new File(externalAppDir, FileName4);
        new LoadFile(src_2, file4).start();
        // музыка 3
        String src_3 = "https://firebasestorage.googleapis.com/v0/b/grandpovar.appspot.com/o/mymusic.mp3?alt=media&token=9193132c-fdf3-41bd-be26-7a5cd2cf4377";
        String FileName5 = audioName[4];
        File file5 = new File(externalAppDir, FileName5);
        new LoadFile(src_3, file5).start();

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


    @Override
    public void onStart() {
        super.onStart();


    }


}



