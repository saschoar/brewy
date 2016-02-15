package com.saschahuth.brewy.domain.brewerydb.model

/**
 * Created by sascha on 13.02.16.
 */

data class Country(
        var isoCode: String,
        var isoThree: String,
        var name: String,
        var displayName: String,
        var numberCode: Number,
        var urlTitle: String,
        var createDate: String
)