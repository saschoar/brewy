package com.saschahuth.brewy.domain.brewerydb.model

/**
 * Created by sascha on 13.02.16.
 */

data class Brewery(
        var id: String,
        var name: String,
        var createDate: String,
        var description: String,
        var image: Image,
        var updateDate: String,
        var isOrganic: String,
        var established: String,
        var status: String,
        var statusDisplay: String,
        var website: String,
        var locations: List<Location>
)