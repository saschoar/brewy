package com.saschahuth.brewy.domain.brewerydb

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


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

            val restAdapter = Retrofit.Builder()
                    .baseUrl("http://api.brewerydb.com/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()

            return restAdapter.create(Api::class.java)
        }
    }

    @GET("brewery/{id}/?key=mykey&format=json")
    fun getBrewery(@Path("id") id: String): Call<Model.Result<Model.Brewery>>
}