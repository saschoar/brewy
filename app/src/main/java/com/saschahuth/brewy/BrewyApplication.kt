package com.saschahuth.brewy

import android.app.Application
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class BrewyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CalligraphyConfig
                .initDefault(CalligraphyConfig.Builder()
                        .setDefaultFontPath(getString(R.string.fontPathRegular))
                        .setFontAttrId(R.attr.fontPath)
                        .build())
    }
}