package io.j3solutions.boilerandroid.utils.extensions

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.TransferListener
import okhttp3.OkHttpClient

fun PlayerView.loadSignedHlsStream(
    context: Context?,
    url: String,
    cookie: String,
    playerEventListener: Player.EventListener?
): DefaultTrackSelector {
    val trackSelector = DefaultTrackSelector()
    val player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
    this.player = player

    val okHttpDataSourceFactory = OkHttpDataSourceFactory(
        OkHttpClient(),
        "AndroidApp",
        null as TransferListener?
    )

    okHttpDataSourceFactory.defaultRequestProperties.set("Cookie", cookie)

    val dataFactory = DefaultDataSourceFactory(context, null, okHttpDataSourceFactory)
    val uri = Uri.parse(url)
    val source: MediaSource = HlsMediaSource.Factory(dataFactory).createMediaSource(uri)

    // val subtitleMediaSources: Array<MediaSource> = subtitleResources.map {
    //     val textFormat = Format.createTextSampleFormat(null,
    //         MimeTypes.TEXT_VTT, null,
    //         Format.NO_VALUE, Format.NO_VALUE, language,
    //         null, Format.OFFSET_SAMPLE_RELATIVE)
    //     SingleSampleMediaSource.Factory(dataFactory)
    //         .createMediaSource(Uri.parse(subtitleUrl), textFormat, C.TIME_UNSET)
    // }.toTypedArray()

    val mediaSource = MergingMediaSource(source) // , *subtitleMediaSources)

    player.prepare(mediaSource)
    player.playWhenReady = true
    if (playerEventListener != null) player.addListener(playerEventListener)

    return trackSelector
}
