package com.saschahuth.brewy

import android.app.Application
import android.content.Context
import com.squareup.okhttp.OkHttpClient
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import javax.inject.Inject

class BrewyApp : Application() {

    companion object {

        @JvmStatic fun appComponent(context: Context): AppComponent =
                (context.applicationContext as BrewyApp).appComponent

    }

    val appComponent: AppComponent by lazy { AppComponent.Initializer.init(this) }
    @Inject lateinit var appLifecycleCallbacks: AppLifecycleCallbacks
    @Inject lateinit var okHttpClient: OkHttpClient

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        AppGlideModule.registerComponents(this, okHttpClient)
        appLifecycleCallbacks.onCreate(this)

        CalligraphyConfig
                .initDefault(CalligraphyConfig.Builder()
                        .setDefaultFontPath(getString(R.string.fontPathRegular))
                        .setFontAttrId(R.attr.fontPath)
                        .build())
    }

    override fun onTerminate() {
        appLifecycleCallbacks.onTerminate(this)
        super.onTerminate()
    }
}