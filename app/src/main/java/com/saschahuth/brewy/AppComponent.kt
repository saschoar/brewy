package com.saschahuth.brewy

@AppScope
public interface AppComponent : MainAppComponent {

    fun inject(app: BrewyApp)

    object Initializer {
        fun init(app: BrewyApp): AppComponent =
                DaggerAppComponent.builder().build()
    }
}