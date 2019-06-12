package io.j3solutions.boilerandroid.utils.extensions

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.widget.ImageView
import io.j3solutions.boilerandroid.utils.glide.GlideApp
import io.j3solutions.boilerandroid.utils.glide.GlideRequest
import io.j3solutions.boilerandroid.utils.glide.SvgSoftwareLayerSetter

fun ImageView.loadSvg(
    context: Context,
    url: String,
    customize: (GlideRequest<PictureDrawable>) -> (GlideRequest<PictureDrawable>) = { it }
) {
    val request = GlideApp.with(context)
        .`as`(PictureDrawable::class.java)
        .listener(SvgSoftwareLayerSetter())
        .load(Uri.parse(url))

    customize(request).into(this)
}

fun ImageView.load(url: String) {
    GlideApp.with(this).load(url).into(this)
}

fun ImageView.changeBackgroundTint(color: String) {
    setColorFilter(color.toColor())
}
