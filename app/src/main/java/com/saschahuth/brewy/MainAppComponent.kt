package com.saschahuth.brewy

import android.app.Application
import com.saschahuth.brewy.domain.BreweryDbServiceProvider

public interface MainAppComponent {

    public fun application(): Application

    public fun breweryDbService(): BreweryDbServiceProvider

}
