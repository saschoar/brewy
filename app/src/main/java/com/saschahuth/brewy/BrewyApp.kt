package com.saschahuth.brewy

import android.app.Application
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class BrewyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CalligraphyConfig
                .initDefault(CalligraphyConfig.Builder()
                        .setDefaultFontPath(getString(R.string.fontPathRegular))
                        .setFontAttrId(R.attr.fontPath)
                        .build())
    }
}