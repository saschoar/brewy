package com.saschahuth.brewy.domain

import com.saschahuth.brewy.AppScope
import dagger.Module
import dagger.Provides

@Module
public class RepositoryModule {

    @Provides
    @AppScope
    fun provideBreweryDbServiceProvider(breweryDbService: BreweryDbService): BreweryDbServiceProvider = BreweryDbServiceProvider(breweryDbService)
}