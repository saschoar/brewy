package com.saschahuth.brewy.domain.model

/**
 * Created by sascha on 13.02.16.
 */

data class Style(
        var id: Number?,
        var name: String?,
        var categoryId: Number?,
        var category: Category?,
        var abvMax: String?,
        var abvMin: String?,
        var createDate: String?,
        var description: String?,
        var fgMax: String?,
        var fgMin: String?,
        var ibuMax: String?,
        var ibuMin: String?,
        var ogMin: String?,
        var srmMax: String?,
        var srmMin: String?
)