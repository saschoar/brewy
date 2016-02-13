package com.saschahuth.brewy

import android.app.Activity
import android.widget.Toast

/**
 * Created by sascha on 13.02.16.
 */

fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}