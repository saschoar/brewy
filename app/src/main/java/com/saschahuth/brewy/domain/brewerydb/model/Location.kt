package com.saschahuth.brewy.domain.brewerydb.model

/**
 * Created by sascha on 13.02.16.
 */

data class Location(
        var id: String,
        var name: String,
        var country: Country,
        var latitude: Number,
        var longitude: Number,
        var countryIsoCode: String,
        var createDate: String,
        var inPlanning: String,
        var isClosed: String,
        var isPrimary: String,
        var locality: String,
        var locationType: String,
        var locationTypeDisplay: String,
        var openToPublic: String,
        var postalCode: String,
        var region: String,
        var status: String,
        var statusDisplay: String,
        var updateDate: String,
        var website: String,
        var yearOpened: String
)