package com.android.freak.voicedemo;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/10/9.
 */

public class VoiceSpeaker {

    private static VoiceSpeaker sInstance;

    private ExecutorService service;
    private ConcatenatingMediaSource mConcatenatingMediaSource;
    private List<MediaSource> mMediaSourceList;
    //    private SimpleExoPlayer mPlayer;

    private VoiceSpeaker() {
        service = Executors.newCachedThreadPool();
//        service = Executors.newSingleThreadExecutor();
    }


    public static synchronized VoiceSpeaker getInstance() {
        if (sInstance == null) {
            sInstance = new VoiceSpeaker();
        }
        return sInstance;
    }


    public void speak(final List<String> list) {
        if (service != null) {
            service.execute(new Runnable() {
                @Override
                public void run() {
//                    start(list);
                    exoPlayer(list);
                }
            });
        }
//        exoPlayer(list);
    }

    public void release() {
//        mPlayer.release();
//        mPlayer.getPlaybackState();
    }

    private void exoPlayer(final List<String> list) {
        synchronized (this){
            if (mMediaSourceList == null) {
                mMediaSourceList = new ArrayList<>();
            }
            mMediaSourceList.clear();
            final CountDownLatch latch = new CountDownLatch(1);//并发
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(App.getInstance().getContext(),
                    Util.getUserAgent(App.getInstance().getContext(), "VoiceDemo"));
            final SimpleExoPlayer mPlayer = ExoPlayerFactory.newSimpleInstance(App.getInstance().getContext());
            for (int i = 0; i < list.size(); i++) {
                MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(FileUtils.getAssetsPath(String.format("sound/tts_%s.mp3", list.get(i)))));
                mMediaSourceList.add(mediaSource);
                Logger.e(String.valueOf(Uri.parse(FileUtils.getAssetsPath(String.format("sound/tts_%s.mp3", list.get(i))))));
            }

            switch (mMediaSourceList.size()) {
                case 0:
                    break;
                case 1:
                    Logger.e("1");
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0));
                    break;
                case 2:
                    Logger.e("2");
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1));
                    break;
                case 3:
                    Logger.e("3");
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2));
                    break;
                case 4:
                    Logger.e("4");
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3));
                    break;
                case 5:
                    Logger.e("5");
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4));
                    break;
                case 6:
                    Logger.e("6");
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4), mMediaSourceList.get(5));
                    break;
                case 7:
                    Logger.e("7");
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4), mMediaSourceList.get(5), mMediaSourceList.get(6));
                    break;
                case 8:
                    Logger.e("8");
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4), mMediaSourceList.get(5), mMediaSourceList.get(6), mMediaSourceList.get(7));
                    break;
                case 9:
                    Logger.e("9");
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4), mMediaSourceList.get(5), mMediaSourceList.get(6), mMediaSourceList.get(7), mMediaSourceList.get(8));
                    break;
                case 10:
                    Logger.e("10");
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4), mMediaSourceList.get(5), mMediaSourceList.get(6), mMediaSourceList.get(7), mMediaSourceList.get(8), mMediaSourceList.get(9));
                    break;
                case 11:
                    Logger.e("11");
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4), mMediaSourceList.get(5), mMediaSourceList.get(6), mMediaSourceList.get(7), mMediaSourceList.get(8), mMediaSourceList.get(9),
                            mMediaSourceList.get(10));
                    break;
                case 12:
                    Logger.e("12");
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4), mMediaSourceList.get(5), mMediaSourceList.get(6), mMediaSourceList.get(7), mMediaSourceList.get(8), mMediaSourceList.get(9),
                            mMediaSourceList.get(10), mMediaSourceList.get(11));
                    break;
                case 13:
                    Logger.e("13");
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4), mMediaSourceList.get(5), mMediaSourceList.get(6), mMediaSourceList.get(7), mMediaSourceList.get(8), mMediaSourceList.get(9),
                            mMediaSourceList.get(10), mMediaSourceList.get(11), mMediaSourceList.get(12));
                    break;
                case 14:
                    Logger.e("14");
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4), mMediaSourceList.get(5), mMediaSourceList.get(6), mMediaSourceList.get(7), mMediaSourceList.get(8), mMediaSourceList.get(9),
                            mMediaSourceList.get(10), mMediaSourceList.get(11), mMediaSourceList.get(12), mMediaSourceList.get(13));
                    break;
                case 15:
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4), mMediaSourceList.get(5), mMediaSourceList.get(6), mMediaSourceList.get(7), mMediaSourceList.get(8), mMediaSourceList.get(9),
                            mMediaSourceList.get(10), mMediaSourceList.get(11), mMediaSourceList.get(12), mMediaSourceList.get(13), mMediaSourceList.get(14));
                    break;
                case 16:
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4), mMediaSourceList.get(5), mMediaSourceList.get(6), mMediaSourceList.get(7), mMediaSourceList.get(8), mMediaSourceList.get(9),
                            mMediaSourceList.get(10), mMediaSourceList.get(11), mMediaSourceList.get(12), mMediaSourceList.get(13), mMediaSourceList.get(14), mMediaSourceList.get(15));
                    break;
                case 17:
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4), mMediaSourceList.get(5), mMediaSourceList.get(6), mMediaSourceList.get(7), mMediaSourceList.get(8), mMediaSourceList.get(9),
                            mMediaSourceList.get(10), mMediaSourceList.get(11), mMediaSourceList.get(12), mMediaSourceList.get(13), mMediaSourceList.get(14), mMediaSourceList.get(15),
                            mMediaSourceList.get(16));
                    break;
                case 18:
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4), mMediaSourceList.get(5), mMediaSourceList.get(6), mMediaSourceList.get(7), mMediaSourceList.get(8), mMediaSourceList.get(9),
                            mMediaSourceList.get(10), mMediaSourceList.get(11), mMediaSourceList.get(12), mMediaSourceList.get(13), mMediaSourceList.get(14), mMediaSourceList.get(15),
                            mMediaSourceList.get(16), mMediaSourceList.get(17));
                    break;
                case 19:
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4), mMediaSourceList.get(5), mMediaSourceList.get(6), mMediaSourceList.get(7), mMediaSourceList.get(8), mMediaSourceList.get(9),
                            mMediaSourceList.get(10), mMediaSourceList.get(11), mMediaSourceList.get(12), mMediaSourceList.get(13), mMediaSourceList.get(14), mMediaSourceList.get(15),
                            mMediaSourceList.get(16), mMediaSourceList.get(17), mMediaSourceList.get(18));
                    break;
                case 20:
                    mConcatenatingMediaSource = new ConcatenatingMediaSource(mMediaSourceList.get(0), mMediaSourceList.get(1), mMediaSourceList.get(2), mMediaSourceList.get(3),
                            mMediaSourceList.get(4), mMediaSourceList.get(5), mMediaSourceList.get(6), mMediaSourceList.get(7), mMediaSourceList.get(8), mMediaSourceList.get(9),
                            mMediaSourceList.get(10), mMediaSourceList.get(11), mMediaSourceList.get(12), mMediaSourceList.get(13), mMediaSourceList.get(14), mMediaSourceList.get(15),
                            mMediaSourceList.get(16), mMediaSourceList.get(17), mMediaSourceList.get(18), mMediaSourceList.get(19));
                    break;
                default:
                    break;
            }





            mPlayer.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                    if (playWhenReady){
//                        if (playbackState==2){
//                            try {
//                                latch.await();
//                                this.notifyAll();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    if (playbackState==4){
//                        mPlayer.setPlayWhenReady(false);
//                    }
//                    }else {
//                        if (playbackState==4){
//                            latch.countDown();
//                            mPlayer.release();
//                        }
//                    }

                        if (playbackState == 4) {
                            mPlayer.release();
                            latch.countDown();
                            mPlayer.setPlayWhenReady(false);
                        }


                    Logger.e("播放状态+++++" + playbackState);
                    Logger.e("播放状态+++++" + playWhenReady);
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });


            mPlayer.prepare(mConcatenatingMediaSource);
            Logger.e("播放状态" + mPlayer.getPlaybackState());
            mPlayer.setPlayWhenReady(true);
            Logger.e("播放状态" + mPlayer.getPlaybackState());

            try {
                latch.await();
                this.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void start(final List<String> list) {
        Logger.e("开始打印");
        Logger.d(list);
        synchronized (this) {
            final CountDownLatch latch = new CountDownLatch(1);
            MediaPlayer player = new MediaPlayer();
            if (list != null && list.size() > 0) {
                final int[] counter = {0};
                String path = String.format("sound/tts_%s.mp3", list.get(counter[0]));
                AssetFileDescriptor fd = null;
                try {
                    fd = FileUtils.getAssetFileDescription(path);
                    Logger.e(path);
                    Logger.e("第一个" + FileUtils.getAssetsPath(path));
                    player.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(),
                            fd.getLength());
                    player.prepareAsync();
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.reset();
                            counter[0]++;
                            if (counter[0] < list.size()) {
                                try {
                                    Logger.e(FileUtils.getAssetsPath(String.format("sound/tts_%s.mp3", list.get(counter[0]))));
                                    AssetFileDescriptor fileDescriptor = FileUtils.getAssetFileDescription(String.format("sound/tts_%s.mp3", list.get(counter[0])));
                                    mp.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                                    mp.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    latch.countDown();
                                }
                            } else {
                                mp.release();
                                latch.countDown();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    latch.countDown();
                } finally {
                    if (fd != null) {
                        try {
                            fd.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            try {
                latch.await();
                this.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
