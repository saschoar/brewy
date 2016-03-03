package com.saschahuth.brewy

import com.saschahuth.brewy.domain.DomainModule
import com.saschahuth.brewy.ui.brewery.BreweryActivity
import com.saschahuth.brewy.ui.brewery.BreweryLocationAdapter
import com.saschahuth.brewy.ui.main.MainActivity
import com.saschahuth.brewy.ui.main.NearbyBreweriesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidModule::class, DomainModule::class))
interface AppComponent {
    fun inject(brewyApp: BrewyApp)

    fun inject(mainActivity: MainActivity)

    fun inject(breweryActivity: BreweryActivity)

    fun inject(nearbyBreweriesFragment: NearbyBreweriesFragment)

    fun inject(breweryLocationAdapter: BreweryLocationAdapter)
}