package bokarev.stepan.mymuseum;


import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import org.apache.commons.io.FileUtils;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class FragmentCard extends Fragment {

    public static MediaPlayerHolder mMediaPlayerHolder;
    public boolean isUserSeeking;

    public TextView mTextDebug;
    public static TextView mCurrentSongLabel;
    public static TextView mTotalSongLabel;
    public Button mPlayButton;
    public Button mPauseButton;
    public Button mResetButton;
    public SeekBar mSeekbarAudio;
    public ScrollView mScrollContainer;
    public View view;

    public static Uri myUri;
    public static String text = "";


    public static Fragment newInstance() {

        FragmentCard fragment = new FragmentCard();
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_card, container, false);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);


        Pager adapter = new Pager(getActivity().getSupportFragmentManager(), 5);
        //установка adapter to pager
        viewPager.setClipToPadding(false);
        // отступ от родителя элемента
        viewPager.setPadding(60, 0, 60, 0);
        // отступ между друг другом
        viewPager.setPageMargin(8);
        viewPager.setAdapter(adapter);
        /*
        File externalAppDir = new File("/Android/data/bokarev.stepan.mymuseum/files");
        if (!externalAppDir.exists()) {
            externalAppDir.mkdir();
        }
        String src = "https://firebasestorage.googleapis.com/v0/b/procao.appspot.com/o/AUDIO%2Fhouselebedi.mp3?alt=media&token=9cf33edd-379e-4cce-bdc9-2a5c2fef1b7b";
        File dest = new File("/Android/data/bokarev.stepan.mymuseum/" + "/files/");


        String FileName = "FileName.mp4";
        File file = new File(dest, FileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new LoadFile(src, file).start();*/


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
                                    UpdateSongTime();
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

        bindViews();
        EventBus.getDefault().register(this);


        //myUri = Uri.parse("/Android/data/bokarev.stepan.mymuseum/files" + FileName);
        mMediaPlayerHolder = new MediaPlayerHolder(getContext(), myUri);

        setupSeekbar();

        return view;
    }

    private void bindViews() {

        mTextDebug = view.findViewById(R.id.text_debug);
        mPlayButton = view.findViewById(R.id.button_play);
        mPauseButton = view.findViewById(R.id.button_pause);
        mResetButton = view.findViewById(R.id.button_reset);
        mSeekbarAudio = view.findViewById(R.id.seekbar_audio);
        mScrollContainer = view.findViewById(R.id.scroll_container);
        mTotalSongLabel = view.findViewById(R.id.songTotalDurationLabel);
        mCurrentSongLabel = view.findViewById(R.id.songCurrentDurationLabel);


        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause();
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                play();
                UpdateSongTime();
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mMediaPlayerHolder.release();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mMediaPlayerHolder.load(myUri);
        }
    }

    // Handle user input for Seekbar changes.

    public void setupSeekbar() {


        mSeekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // This holds the progress value for onStopTrackingTouch.
            int userSelectedPosition = 0;

            @SuppressLint("DefaultLocale")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Only fire seekTo() calls when user stops the touch event.
                mCurrentSongLabel.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(progress),
                        TimeUnit.MILLISECONDS.toSeconds(progress) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(progress))));

                if (fromUser) {
                    userSelectedPosition = progress;
                    isUserSeeking = true;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserSeeking = false;
                EventBus.getDefault().post(new LocalEventFromMainActivity.SeekTo(
                        userSelectedPosition));
            }
        });
    }


    private void pause() {

        EventBus.getDefault().post(new LocalEventFromMainActivity.PausePlayback());
    }

    private void play() {
        EventBus.getDefault().post(new LocalEventFromMainActivity.StartPlayback());


    }

    public static void UpdateSongTime() {
        final Handler handler;

        Runnable handlerTask = null;

        // mSeekbarAudio.setMax(mMediaPlayerHolder.getDuration());


        handler = new Handler();
        final Runnable finalHandlerTask = handlerTask;
        handlerTask = new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                // do something

                int currentPosition = mMediaPlayerHolder.getCurrentPositionSong();

                mTotalSongLabel.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(mMediaPlayerHolder.getDuration()),
                        TimeUnit.MILLISECONDS.toSeconds(mMediaPlayerHolder.getDuration()) - 60));
                mCurrentSongLabel.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(currentPosition),
                        TimeUnit.MILLISECONDS.toSeconds(currentPosition) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentPosition))));

                //  hdlr.postDelayed(this, 100 );

                handler.postDelayed(finalHandlerTask, 1000);

            }
        };
        handlerTask.run();
    }

    private void reset() {
        EventBus.getDefault().post(new LocalEventFromMainActivity.ResetPlayback());
    }

    // Display log messges to the UI.

    public void log(StringBuffer formattedMessage) {
        if (mTextDebug != null) {
            mTextDebug.setText(formattedMessage);
            // Move the mScrollContainer focus to the end.
            mScrollContainer.post(new Runnable() {
                @Override
                public void run() {
                    mScrollContainer.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }

    // Event subscribers.
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocalEventFromMediaPlayerHolder.UpdateLog event) {
        log(event.formattedMessage);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocalEventFromMediaPlayerHolder.PlaybackDuration event) {
        mSeekbarAudio.setMax(event.duration);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocalEventFromMediaPlayerHolder.PlaybackPosition event) {
        if (!isUserSeeking) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mSeekbarAudio.setProgress(event.position, true);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocalEventFromMediaPlayerHolder.StateChanged event) {
        Toast.makeText(getContext(), String.format("State changed to:%s", event.currentState),
                Toast.LENGTH_SHORT).show();
    }
}
