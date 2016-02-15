package com.saschahuth.brewy.domain.brewerydb.model

/**
 * Created by sascha on 13.02.16.
 */

data class Result<T>(
        var status: String,
        var message: String,
        var data: T
)