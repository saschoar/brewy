package com.saschahuth.brewy

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
public class AppModule(private val app: Application) {

    @Provides
    @AppScope
    fun provideApplication(): Application = app

}
