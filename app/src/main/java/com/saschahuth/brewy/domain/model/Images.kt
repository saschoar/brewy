package com.saschahuth.brewy.domain.model

import nz.bradcampbell.paperparcel.PaperParcel

/**
 * Created by sascha on 13.02.16.
 */

@PaperParcel
data class Images(
        var icon: String?,
        var medium: String?,
        var large: String?,
        var squareMedium: String?,
        var squareLarge: String?
)