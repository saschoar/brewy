package com.saschahuth.brewy

/**
 * Created by sascha on 13.02.16.
 */
object Model {
    data class Beer(
            var id: String,
            var name: String,
            var abv: String,
            var available: Available,
            var availableId: Number,
            var createDate: String,
            var description: String,
            var foodPairings: String,
            var glass: Glass,
            var glasswareId: Number,
            var ibu: String,
            var isOrganic: String,
            var originalGravity: Number,
            var status: String,
            var statusDisplay: String,
            var style: Style,
            var styleId: Number,
            var type: String,
            var updateDate: String,
            var breweries: List<Brewery>,
            var labels: Images
    )

    data class Brewery(
            var id: String,
            var name: String,
            var createDate: String,
            var description: String,
            var images: Images,
            var updateDate: String,
            var isOrganic: String,
            var established: String,
            var status: String
            var statusDisplay: String,
            var website: String,
            var locations: List<Locations>
    )

    data class Available(
            var id: String,
            var name: String,
            var description: String
    )

    data class Category(
            var id: String,
            var name: String,
            var createDate: String
    )

    data class Country(
            var isoCode: String,
            var isoThree: String,
            var displayName: String,
            var name: String,
            var numberCode: Number,
            var urlTitle: String,
            var createDate: String
    )

    data class Glass(
            var id: String,
            var name: String,
            var createDate: String
    )

    data class Images(
            var icon: String,
            var medium: String,
            var large: String
    )

    data class Locations(
            var id: String,
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
            var name: String,
            var openToPublic: String,
            var postalCode: String,
            var region: String,
            var status: String,
            var statusDisplay: String,
            var updateDate: String,
            var website: String,
            var yearOpened: String
    )

    data class Style(
            var id: Number,
            var categoryId: Number,
            var category: Category,
            var abvMax: String,
            var abvMin: String,
            var createDate: String,
            var description: String,
            var fgMax: String,
            var fgMin: String,
            var ibuMax: String,
            var ibuMin: String,
            var name: String,
            var ogMin: String,
            var srmMax: String,
            var srmMin: String
    )
}