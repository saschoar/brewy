package com.saschahuth.brewy.domain.brewerydb

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
            @Query("locationType") locationType: String? = null,
            @Query("countryIsoCode") countryIsoCode: String? = null,
            @Query("status") status: String? = null,
            @Query("since") since: Long? = null,
            @Query("order") order: String? = null,
            @Query("sort") sort: String? = null,
            @Query("region") region: String? = null): Call<ResultPage<Location>>

    @GET("search/geo/point")
    fun getBreweriesByGeoPoint(
            @Query("lat") latitude: Double,
            @Query("lng") longitude: Double,
            @Query("radius") radius: Int? = null,
            @Query("unit") unit: String? = null,
            @Query("withSocialAccounts") withSocialAccounts: Boolean? = null,
            @Query("withGuilds") withGuilds: Boolean? = null,
            @Query("withAlternateNames") withAlternateNames: Boolean? = null): Call<ResultPage<Brewery>>
}