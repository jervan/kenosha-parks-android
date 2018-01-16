package edu.uwp.appfactory.eventsmanagement.Components;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import edu.uwp.appfactory.eventsmanagement.util.Config;

/**
 * Created by hanh on 3/29/17.
 */

public class CustomYoutubePlayer extends YouTubePlayerSupportFragment implements YouTubePlayer.OnInitializedListener {
    private String videoId;
    public void setVideoId(String videoId){
        this.videoId = videoId;
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {

            youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
            youTubePlayer.cueVideo(videoId);

        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initialize(Config.YOUTUBE_API_KEY, this);
    }



    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
