package com.saschahuth.brewy.domain.brewerydb

import android.support.annotation.StringDef
import com.saschahuth.brewy.BuildConfig
import com.saschahuth.brewy.domain.brewerydb.model.Brewery
import com.saschahuth.brewy.domain.brewerydb.model.Location
import com.saschahuth.brewy.domain.brewerydb.model.Result
import com.saschahuth.brewy.domain.brewerydb.model.ResultPage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by sascha on 13.02.16.
 */
interface Api {

    companion object {
        fun create(): Api {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor(logging)

            httpClient.addInterceptor({ chain ->
                val request = chain.request()
                request.header("Accept:application/json")
                val url = request
                        .url()
                        .newBuilder()
                        .addQueryParameter("key", BuildConfig.BREWERY_DB_API_KEY)
                        .build()
                chain.proceed(request.newBuilder().url(url).build())
            })

            val restAdapter = Retrofit.Builder()
                    .baseUrl("http://api.brewerydb.com/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()

            return restAdapter.create(Api::class.java)
        }
    }

    @GET("brewery/{id}/")
    fun getBrewery(
            @Path("id") id: String,
            @Query("withSocialAccounts") withSocialAccounts: Boolean? = null,
            @Query("withGuilds") withGuilds: Boolean? = null,
            @Query("withLocations") withLocations: Boolean? = null,
            @Query("withAlternateNames") withAlternateNames: Boolean? = null): Call<Result<Brewery>>

    @GET("locations/")
    fun getLocations(
            @Query("p") pageNumber: Int? = null,
            @Query("postalCode") postalCode: Int? = null,
            @Query("locality") locality: String? = null,
            @Query("isPrimary") isPrimary: Boolean? = null,
            @Query("isPlanning") isPlanning: Boolean? = null,
            @Query("isClosed") isClosed: Boolean? = null,
            @Query("ids") ids: String? = null,
            @Query("locationType") @LocationType locationType: String? = null,
            @Query("countryIsoCode") countryIsoCode: String? = null,
            @Query("status") status: String? = null,
            @Query("since") since: Long? = null,
            @Query("order") @LocationOrder order: String ? = null,
            @Query("sort") @Sort sort: String? = null,
            @Query("region") region: String? = null): Call<ResultPage<Location>>

    @GET("search/geo/point")
    fun getLocationsByGeoPoint(
            @Query("lat") latitude: Double,
            @Query("lng") longitude: Double,
            @Query("radius") radius: Int? = null,
            @Query("unit") @DistanceUnit unit: String? = null,
            @Query("withSocialAccounts") withSocialAccounts: Boolean? = null,
            @Query("withGuilds") withGuilds: Boolean? = null,
            @Query("withAlternateNames") withAlternateNames: Boolean? = null): Call<ResultPage<Location>>

}

const val DISTANCE_UNIT_MILES: String = "mi"
const val DISTANCE_UNIT_KILOMETERS: String = "km"

@Retention(AnnotationRetention.SOURCE)
@StringDef(
        DISTANCE_UNIT_MILES,
        DISTANCE_UNIT_KILOMETERS)
annotation class DistanceUnit

const val SORT_ASCENDING: String = "ASC"
const val SORT_DESCENDING: String = "DESC"

@Retention(AnnotationRetention.SOURCE)
@StringDef(
        SORT_ASCENDING,
        SORT_DESCENDING)
annotation class Sort

const val LOCATION_ORDER_NAME: String = "name"
const val LOCATION_ORDER_BREWERY_NAME: String = "breweryName"
const val LOCATION_ORDER_LOCALITY: String = "locality"
const val LOCATION_ORDER_REGION: String = "region"
const val LOCATION_ORDER_POSTAL_CODE: String = "postalCode"
const val LOCATION_ORDER_IS_PRIMARY: String = "isPrimary"
const val LOCATION_ORDER_IS_PLANNING: String = "isPlanning"
const val LOCATION_ORDER_IS_CLOSED: String = "closed"
const val LOCATION_ORDER_IS_LOCATION_TYPE: String = "locationType"
const val LOCATION_ORDER_IS_COUNTRY_ISO_CODE: String = "countryIsoCode"
const val LOCATION_ORDER_STATUS: String = "status"
const val LOCATION_ORDER_CREATE_DATE: String = "createDate"
const val LOCATION_ORDER_UPDATE_DATE: String = "updateDate"

@Retention(AnnotationRetention.SOURCE)
@StringDef(
        LOCATION_ORDER_NAME,
        LOCATION_ORDER_BREWERY_NAME,
        LOCATION_ORDER_LOCALITY,
        LOCATION_ORDER_REGION,
        LOCATION_ORDER_POSTAL_CODE,
        LOCATION_ORDER_IS_PRIMARY,
        LOCATION_ORDER_IS_PLANNING,
        LOCATION_ORDER_IS_CLOSED,
        LOCATION_ORDER_IS_LOCATION_TYPE,
        LOCATION_ORDER_IS_COUNTRY_ISO_CODE,
        LOCATION_ORDER_STATUS,
        LOCATION_ORDER_CREATE_DATE,
        LOCATION_ORDER_UPDATE_DATE)
annotation class LocationOrder

const val LOCATION_TYPE_MICRO: String = "micro"
const val LOCATION_TYPE_MACRO: String = "macro"
const val LOCATION_TYPE_NANO: String = "nano"
const val LOCATION_TYPE_BREWPUB: String = "brewpub"
const val LOCATION_TYPE_PRODUCTION: String = "production"
const val LOCATION_TYPE_OFFICE: String = "office"
const val LOCATION_TYPE_TASTING: String = "tasting"
const val LOCATION_TYPE_RESTAURANT: String = "restaurant"
const val LOCATION_TYPE_CIDERY: String = "cidery"
const val LOCATION_TYPE_MEADERY: String = "meadery"

@Retention(AnnotationRetention.SOURCE)
@StringDef(
        LOCATION_TYPE_MICRO,
        LOCATION_TYPE_MACRO,
        LOCATION_TYPE_NANO,
        LOCATION_TYPE_BREWPUB,
        LOCATION_TYPE_PRODUCTION,
        LOCATION_TYPE_OFFICE,
        LOCATION_TYPE_TASTING,
        LOCATION_TYPE_RESTAURANT,
        LOCATION_TYPE_CIDERY,
        LOCATION_TYPE_MEADERY)
annotation class LocationType