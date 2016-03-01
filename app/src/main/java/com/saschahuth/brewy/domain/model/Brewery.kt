package com.saschahuth.brewy.domain.model

import nz.bradcampbell.paperparcel.PaperParcel

/**
 * Created by sascha on 13.02.16.
 */

@PaperParcel
data class Brewery(
        var id: String?,
        var name: String?,
        var createDate: String?,
        var description: String?,
        var images: Images?,
        var updateDate: String?,
        var isOrganic: Boolean?,
        var established: String?,
        var status: String?,
        var statusDisplay: String?,
        var website: String?
)