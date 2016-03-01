package com.saschahuth.brewy.domain.model

/**
 * Created by sascha on 13.02.16.
 */

data class ResultPage<T>(
        var status: String?,
        var currentPage: Number?,
        var numberOfPages: Number?,
        var totalResults: Number?,
        var data: List<T>?
)