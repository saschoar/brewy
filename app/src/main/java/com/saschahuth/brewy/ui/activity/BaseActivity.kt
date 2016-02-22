package com.saschahuth.brewy.ui.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

open class BaseActivity : AppCompatActivity() {
    /**
     * Required for https://github.com/chrisjenx/Calligraphy
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}