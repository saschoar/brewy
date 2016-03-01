package com.saschahuth.brewy.ui.main

import com.saschahuth.brewy.BrewyApp
import com.saschahuth.brewy.ui.main.MainActivity
import com.saschahuth.brewy.ui.main.BeersFragment
import com.saschahuth.brewy.ui.main.NearbyBreweriesFragment
import dagger.Component

@MainScope
@Component(modules = arrayOf(MainModule::class)) interface MainComponent {

    public fun inject(activity: MainActivity)

    public fun inject(fragment: NearbyBreweriesFragment)

    public fun inject(fragment: BeersFragment)

    public object Initializer {

        public fun init(activity: MainActivity): MainComponent {
            return MainComponent.builder()
                    .appComponent(BrewyApp.appComponent(activity))
                    .mainModule(MainModule())
                    .build()
        }
    }
}
