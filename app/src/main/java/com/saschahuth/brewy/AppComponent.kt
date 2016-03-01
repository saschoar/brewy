package com.saschahuth.brewy

@AppScope interface AppComponent : MainAppComponent {

    fun inject(app: BrewyApp)

    object Initializer {
        fun init(app: BrewyApp): AppComponent =
                MainAppComponent.builder().build()
    }
}