package com.saschahuth.brewy.domain

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.saschahuth.brewy.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class DomainModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
                .create()
    }

    @Provides
    @Singleton
    fun provideCache(): Cache {
        val cacheSize: Long = 10 * 1024 * 1024; // 10 MiB
        return Cache(context.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient {

        with(OkHttpClient.Builder()) {
            cache(cache)
            readTimeout(1, TimeUnit.HOURS);
            connectTimeout(1, TimeUnit.HOURS);

            with(HttpLoggingInterceptor()) {
                level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(this)
            }

            addInterceptor({ chain ->
                val request = chain.request()
                request.header("Accept:application/json")
                val maxStale = TimeUnit.HOURS.toMillis(48)
                request.header("Cache-Control:public, only-if-cached, max-stale=$maxStale");

                val url = request
                        .url()
                        .newBuilder()
                        .addQueryParameter("key", BuildConfig.BREWERY_DB_API_KEY)
                        .build()
                chain.proceed(request.newBuilder().url(url).build())
            })
            return build()
        }
    }

    @Provides
    @Singleton
    fun provideBreweryDbService(okHttpClient: OkHttpClient, gson: Gson): BreweryDbService {

        return Retrofit.Builder()
                .baseUrl("http://api.brewerydb.com/v2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(BreweryDbService::class.java)
    }

}