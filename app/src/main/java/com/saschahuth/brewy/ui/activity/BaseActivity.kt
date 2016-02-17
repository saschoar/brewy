package com.saschahuth.brewy.ui.activity

import android.app.Activity
import android.content.Context
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

open class BaseActivity : Activity() {
    /**
     * Required for https://github.com/chrisjenx/Calligraphy
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}