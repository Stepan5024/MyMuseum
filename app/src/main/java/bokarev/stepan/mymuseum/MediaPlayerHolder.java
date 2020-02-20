/*
 * Copyright 2018 Nazmul Idris. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bokarev.stepan.mymuseum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;


public class MediaPlayerHolder {

    private static final int SEEKBAR_REFRESH_INTERVAL_MS = 1000;

    private Uri mResourceId;
    private final MediaPlayer mMediaPlayer;
    private Context mContext;
    private ArrayList<String> mLogMessages = new ArrayList<>();
    private ScheduledExecutorService mExecutor;
    private Runnable mSeekbarProgressUpdateTask;

    enum PlayerState {
        PLAYING, PAUSED, COMPLETED, RESET
    }

    MediaPlayerHolder(Context context, Uri uri) {
        mResourceId = uri;
        mContext = context;
        EventBus.getDefault().register(this);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer.getDuration() == mediaPlayer.getCurrentPosition()) {
                    stopUpdatingSeekbarWithPlaybackProgress(true);
                    logToUI("MediaPlayer playback completed");
                    EventBus.getDefault().post(new LocalEventFromMediaPlayerHolder.PlaybackCompleted());
                    EventBus.getDefault()
                            .post(new LocalEventFromMediaPlayerHolder.StateChanged(
                                    PlayerState.COMPLETED));
                }
            }
        });
        logToUI("mMediaPlayer = new MediaPlayer()");
    }


    void release() {
        logToUI("release() and mMediaPlayer = null");
        mMediaPlayer.release();
        EventBus.getDefault().unregister(this);
    }

    private void play() {
        if (!mMediaPlayer.isPlaying()) {

            mMediaPlayer.start();
            startUpdatingSeekbarWithPlaybackProgress();
            EventBus.getDefault()
                    .post(new LocalEventFromMediaPlayerHolder.StateChanged(PlayerState.PLAYING));
        }
    }

    private void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            logToUI("pause()");
            EventBus.getDefault()
                    .post(new LocalEventFromMediaPlayerHolder.StateChanged(PlayerState.PAUSED));
        }
    }

    private void reset() {
        logToUI("reset()");
        mMediaPlayer.reset();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            load(mResourceId);
        }
        stopUpdatingSeekbarWithPlaybackProgress(true);
        EventBus.getDefault()
                .post(new LocalEventFromMediaPlayerHolder.StateChanged(PlayerState.RESET));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void load(Uri resourceId) {


        try {
            logToUI("load() {1. setDataSource}");
            mMediaPlayer.setDataSource(mContext, resourceId);
        } catch (Exception e) {
            logToUI(e.toString());
        }

        try {
            logToUI("load() {2. prepare}");
            mMediaPlayer.prepare();
        } catch (Exception e) {
            logToUI(e.toString());
        }
        initSeekbar();
    }

    private void seekTo(int duration) {
        logToUI(String.format("seekTo() %d ms", duration));
        mMediaPlayer.seekTo(duration);
    }

    // Reporting media playback position to Seekbar in MainActivity.

    private void stopUpdatingSeekbarWithPlaybackProgress(boolean resetUIPlaybackPosition) {
        try {
            mExecutor.shutdownNow();
        } catch (NullPointerException e) {
        }
        mExecutor = null;
        mSeekbarProgressUpdateTask = null;
        if (resetUIPlaybackPosition) {
            EventBus.getDefault().post(new LocalEventFromMediaPlayerHolder.PlaybackPosition(0));
        }
    }

    public int getCurrentPositionSong() {

        try {
            return mMediaPlayer.getCurrentPosition();
        } catch (IllegalStateException e) {
            return 0;
        }

    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    private void startUpdatingSeekbarWithPlaybackProgress() {
        // Синхронизация  mMediaPlayer с  Seekbar.
        if (mExecutor == null) {
            mExecutor = newSingleThreadScheduledExecutor();
        }
        if (mSeekbarProgressUpdateTask == null) {
            mSeekbarProgressUpdateTask = new Runnable() {
                @Override
                public void run() {
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        int currentPosition = mMediaPlayer.getCurrentPosition();
                        EventBus.getDefault().post(new LocalEventFromMediaPlayerHolder.PlaybackPosition(currentPosition));

                    }
                }
            };
        }
        mExecutor.scheduleAtFixedRate(
                mSeekbarProgressUpdateTask, 0, SEEKBAR_REFRESH_INTERVAL_MS, TimeUnit.MILLISECONDS
        );
    }

    @SuppressLint("DefaultLocale")
    private void initSeekbar() {
        // Set the duration.
        final int duration = mMediaPlayer.getDuration();
        EventBus.getDefault().post(
                new LocalEventFromMediaPlayerHolder.PlaybackDuration(duration));
        logToUI(String.format("setting seekbar max %d sec",
                TimeUnit.MILLISECONDS.toSeconds(duration)));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(LocalEventFromMainActivity.SeekTo event) {
        seekTo(event.position);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(
            LocalEventFromMainActivity.StopUpdatingSeekbarWithMediaPosition event) {
        stopUpdatingSeekbarWithPlaybackProgress(false);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(
            LocalEventFromMainActivity.StartUpdatingSeekbarWithPlaybackPosition event) {
        startUpdatingSeekbarWithPlaybackProgress();
    }

    // Logging to UI methods.

    private void logToUI(String msg) {
        mLogMessages.add(msg);
        fireLogUpdate();
    }

    /**
     * update the MainActivity's UI with the debug log messages
     */
    private void fireLogUpdate() {
        StringBuffer formattedLogMessages = new StringBuffer();
        for (int i = 0; i < mLogMessages.size(); i++) {
            formattedLogMessages.append(i)
                    .append(" - ")
                    .append(mLogMessages.get(i));
            if (i != mLogMessages.size() - 1) {
                formattedLogMessages.append("\n");
            }
        }
        EventBus.getDefault().post(
                new LocalEventFromMediaPlayerHolder.UpdateLog(formattedLogMessages));
    }

    // Respond to playback localevents.

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(LocalEventFromMainActivity.PausePlayback event) {
        pause();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(LocalEventFromMainActivity.StartPlayback event) {
        play();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(LocalEventFromMainActivity.ResetPlayback event) {
        reset();
    }

}
