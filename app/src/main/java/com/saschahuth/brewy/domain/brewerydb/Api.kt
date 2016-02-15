package com.saschahuth.brewy.domain.brewerydb

import android.util.Log
import com.saschahuth.brewy.BuildConfig
import com.saschahuth.brewy.domain.brewerydb.model.Brewery
import com.saschahuth.brewy.domain.brewerydb.model.Result
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

            httpClient.addInterceptor({ chain ->
                val request = chain.request()
                request.header("Accept:application/json")
                val url = request
                        .url()
                        .newBuilder()
                        .addQueryParameter("key", BuildConfig.BREWERY_DB_API_KEY)
                        .build()
                Log.d("Test", url.toString())
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
    fun getBrewery(@Path("id") id: String): Call<Result<Brewery>>
}