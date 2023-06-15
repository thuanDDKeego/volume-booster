package dev.keego.volume.booster.shared.utils

import android.content.Context

object Dimensions {

    fun Float.toDp(context: Context): Float {
        return this / context.resources.displayMetrics.density
    }
}