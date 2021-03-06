package com.saschahuth.brewy.domain.model

import nz.bradcampbell.paperparcel.PaperParcel

/**
 * Created by sascha on 13.02.16.
 */

@PaperParcel
data class Beer(
        var id: String?,
        var name: String?,
        var abv: String?,
        var available: Available?,
        var availableId: Number?,
        var createDate: String?,
        var description: String?,
        var foodPairings: String?,
        var glass: Glass?,
        var glasswareId: Number?,
        var ibu: String?,
        var isOrganic: Boolean?,
        var originalGravity: Number?,
        var status: String?,
        var statusDisplay: String?,
        var style: Style?,
        var styleId: Number?,
        var type: String?,
        var updateDate: String?,
        var breweries: List<Brewery>?,
        var labels: Images?
)