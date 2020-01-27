package cl.y3rk0d3.itunes_search.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import javax.inject.Inject;

import cl.y3rk0d3.itunes_search.R;
import cl.y3rk0d3.itunes_search.databinding.ActivityMainBinding;
import cl.y3rk0d3.itunes_search.entity.Result;
import cl.y3rk0d3.itunes_search.ui.resources.NavigationControllerMain;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector,
        SearchFragment.clickEventListener, Player.EventListener {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    NavigationControllerMain navigationController;
    ActivityMainBinding binding;
    private ImageButton btPlay;
    private ImageView imgAlbum;

    private SimpleExoPlayer exoPlayer;
    private boolean isPlaying = false;

    private static final String TAG = "MainActivity_Audio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        imgAlbum = findViewById(R.id.sheet_iv_album);
        btPlay = findViewById(R.id.sheet_btn_play);
        initSearchInputListener();
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    private void initSearchInputListener() {
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                MainActivity.this.doSearch(v);
                return true;
            }
            return false;
        });

        binding.etSearch.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                doSearch(v);
                return true;
            }
            return false;
        });
    }

    private void doSearch(View v) {
        String query = binding.etSearch.getText().toString();
        dismissKeyboard(v.getWindowToken());
        if (!query.isEmpty()) {
            binding.messageMainActivity.setVisibility(View.GONE);
            binding.containerMainActivity.setVisibility(View.VISIBLE);
            navigationController.navigateToSearchFragment(capitalizeQuery(query));
        } else {
            Toast.makeText(this, "Escribe un nombre de artista para buscar su música", Toast.LENGTH_LONG).show();
        }
    }

    private String capitalizeQuery(String query) {
        char[] characters = query.toCharArray();
        characters[0] = Character.toUpperCase(characters[0]);
        // el -2 es para evitar una excepción al caernos del arreglo
        for (int i = 0; i < query.length() - 2; i++)
            // Es 'palabra'
            if (characters[i] == ' ' || characters[i] == '.' || characters[i] == ',')
                // Reemplazamos
                characters[i + 1] = Character.toUpperCase(characters[i + 1]);
        return new String(characters);
    }

    private void dismissKeyboard(IBinder windowToken) {
        FragmentActivity activity = this;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(windowToken, 0);
        }
    }

    @Override
    public void clickEvent(Result result) {
        binding.setResult(result);
        if (isPlaying) {
            exoPlayer.stop();
            Glide.with(this).load(result.artworkUrl100).into(imgAlbum);
            prepareExoPlayerFromURL(Uri.parse(result.previewUrl));
        }
        Glide.with(this).load(result.artworkUrl100).into(imgAlbum);
        prepareExoPlayerFromURL(Uri.parse(result.previewUrl));
    }

    private void prepareExoPlayerFromURL(Uri uri) {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();

        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), null);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource audioSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);
        exoPlayer.addListener(this);

        exoPlayer.prepare(audioSource);
        initMediaControls();
    }

    private void initMediaControls() {
        initPlayButton();
        setPlayPause(!isPlaying);
    }

    private void initPlayButton() {
        btPlay.requestFocus();
        btPlay.setOnClickListener(view -> setPlayPause(!isPlaying));
    }

    private void setPlayPause(boolean play) {
        isPlaying = play;
        exoPlayer.setPlayWhenReady(play);
        if (!isPlaying) {
            btPlay.setImageResource(android.R.drawable.ic_media_play);
        } else {
            btPlay.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
        Log.i(TAG, "onTimelineChanged");
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.i(TAG, "onTracksChanged");
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.i(TAG, "onLoadingChanged");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.i(TAG, "onPlayerStateChanged: playWhenReady = " + String.valueOf(playWhenReady)
                + " playbackState = " + playbackState);
        switch (playbackState) {
            case ExoPlayer.STATE_ENDED:
                Log.i(TAG, "Playback ended!");
                //Stop playback and return to start position
                setPlayPause(false);
                exoPlayer.seekTo(0);
                break;
            case ExoPlayer.STATE_READY:
                Log.i(TAG, "ExoPlayer ready! pos: " + exoPlayer.getCurrentPosition()
                        + " max: " + exoPlayer.getDuration());
                break;
            case ExoPlayer.STATE_BUFFERING:
                Log.i(TAG, "Playback buffering!");
                break;
            case ExoPlayer.STATE_IDLE:
                Log.i(TAG, "ExoPlayer idle!");
                break;
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.i(TAG, "onPlaybackError: " + error.getMessage());
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        Log.i(TAG, "onPositionDiscontinuity");
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null && isPlaying) {
            exoPlayer.stop();
        }
    }

}
