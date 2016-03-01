package com.saschahuth.brewy.domain

import com.saschahuth.brewy.domain.model.Location
import com.saschahuth.brewy.domain.model.ResultPage
import rx.Observable
import rx.schedulers.Schedulers

public class BreweryDbServiceProvider(val breweryDbService: BreweryDbService) {

    public fun getLocationsByGeoPoint(): Observable<ResultPage<Location>> =
            breweryDbService.getLocationsByGeoPoint(40.024925, -83.0038657, unit = DISTANCE_UNIT_MILES)
                    .subscribeOn(Schedulers.io())

}