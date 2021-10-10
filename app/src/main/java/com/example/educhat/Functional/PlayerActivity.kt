package com.example.educhat.Functional

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.educhat.R
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_player.*
import java.io.File
import java.io.IOException


class PlayerActivity : AppCompatActivity() {
    private var exoPlayer: SimpleExoPlayer? = null
    private lateinit var data: String
    private lateinit var name: String
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        data = intent.getStringExtra("url").toString()
        name = intent.getStringExtra("name").toString()
        videoNameTV.text=name

        backFromPlayer.setOnClickListener { onBackPressed() }

    }

    private fun initializePlayer() {
        exoPlayer = SimpleExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        val mediaSource = buildMediaSource(Uri.parse(data))

        exoPlayer!!.playWhenReady = playWhenReady
        exoPlayer!!.seekTo(currentWindow, playbackPosition)
        exoPlayer!!.prepare(mediaSource!!, false, false)
    }

    private fun buildMediaSource(uri: Uri): MediaSource? {


            val dataSourceFactory: DataSource.Factory =
                DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(this, getString(R.string.app_name))
                )
            return ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)

    }


    private fun hideSystemUi() {
        playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun releasePlayer() {
        if (exoPlayer != null) {
            playWhenReady = exoPlayer!!.playWhenReady
            playbackPosition = exoPlayer!!.currentPosition
            currentWindow = exoPlayer!!.currentWindowIndex
            exoPlayer!!.release()
            exoPlayer = null
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT < 24 || exoPlayer == null) {
            initializePlayer()
        }
    }


    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

}