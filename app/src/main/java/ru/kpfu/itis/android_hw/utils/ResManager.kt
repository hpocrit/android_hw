package ru.kpfu.itis.android_hw.utils

import android.content.Context
import androidx.annotation.StringRes

class ResManager(private val ctx: Context) {

    fun getString(@StringRes res: Int): String = ctx.resources.getString(res)

    fun getString(@StringRes res: Int, vararg args: Any?): String {
        return ctx.resources.getString(res, *args)
    }
}