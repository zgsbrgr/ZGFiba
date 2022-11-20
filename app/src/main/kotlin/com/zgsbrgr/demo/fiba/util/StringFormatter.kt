package com.zgsbrgr.demo.fiba.util

import android.content.Context
import com.zgsbrgr.demo.fiba.R

fun Context.formatPlayerHeightAndAgeToSetInTextView(height: String, age: Int?): String {
    val heightItems = height.split("m")
    return this.resources.getString(
        R.string.player_data, heightItems[0], heightItems[1], age
    )
}
