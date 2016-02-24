package com.saschahuth.brewy.domain.brewerydb.model

import nz.bradcampbell.paperparcel.PaperParcel

/**
 * Created by sascha on 13.02.16.
 */

@PaperParcel
data class Country(
        var isoCode: String?,
        var isoThree: String?,
        var name: String?,
        var displayName: String?,
        var numberCode: Number?,
        var createDate: String?
)