package com.saschahuth.brewy.domain.model

import com.saschahuth.brewy.domain.LocationType
import nz.bradcampbell.paperparcel.PaperParcel

/**
 * Created by sascha on 13.02.16.
 */

@PaperParcel
data class Location(
        var id: String?,
        var name: String?,
        var brewery: Brewery?,
        var country: Country?,
        var streetAddress: String?,
        var latitude: Double?,
        var longitude: Double?,
        var countryIsoCode: String?,
        var createDate: String?,
        var inPlanning: Boolean?,
        var isClosed: Boolean?,
        var isPrimary: Boolean?,
        var locality: String?,
        @LocationType var locationType: String?,
        var locationTypeDisplay: String?,
        var openToPublic: String?,
        var postalCode: String?,
        var region: String?,
        var status: String?,
        var statusDisplay: String?,
        var updateDate: String?,
        var website: String?,
        var yearOpened: String?
)