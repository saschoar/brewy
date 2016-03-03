package com.saschahuth.brewy

import android.app.Application
import android.location.LocationManager
import com.saschahuth.brewy.domain.DomainModule
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import javax.inject.Inject

class BrewyApp : Application() {

    companion object {
        @JvmStatic lateinit var appComponent: AppComponent
    }

    @Inject
    lateinit var locationManager: LocationManager

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
                .builder()
                .androidModule(AndroidModule(this))
                .domainModule(DomainModule(this))
                .build()
        appComponent.inject(this)

        CalligraphyConfig
                .initDefault(CalligraphyConfig.Builder()
                        .setDefaultFontPath(getString(R.string.fontPathRegular))
                        .setFontAttrId(R.attr.fontPath)
                        .build())
    }
}