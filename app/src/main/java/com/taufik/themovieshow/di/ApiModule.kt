package com.taufik.themovieshow.di

import android.content.Context
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.data.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val NETWORK_TIMEOUT = 60L
    private const val READ_TIMEOUT = 30
    private const val WRITE_TIMEOUT = 30
    private const val CONNECTION_TIMEOUT = 10
    private const val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB
    private const val MAX_CONTENT_LENGTH = 250_000L

    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        if (BuildConfig.BASE_URL.isBlank()) {
            throw IllegalStateException("BASE_URL is missing. Please define it in your build.gradle file.")
        }
        return BuildConfig.BASE_URL
    }


    @Provides
    @Singleton
    fun connectionTimeout() = NETWORK_TIMEOUT

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideHttpClient(@ApplicationContext context: Context, cache: Cache): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val requestInterceptor = Interceptor { chain ->
            val url = chain.request().url.newBuilder().build()
            val request = chain.request().newBuilder().url(url).build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor(requestInterceptor)
            .apply {
                if (BuildConfig.ENABLE_CHUCKER) {
                    addInterceptor(loggingInterceptor)
                    addInterceptor(
                        ChuckerInterceptor.Builder(context.applicationContext)
                            .collector(
                                ChuckerCollector(
                                    context.applicationContext,
                                    showNotification = true
                                )
                            )
                            .maxContentLength(MAX_CONTENT_LENGTH)
                            .alwaysReadResponseBody(true)
                            .build()
                    )
                }
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    internal fun provideCache(context: Context): Cache {
        val httpCacheDirectory = File(context.cacheDir.absolutePath, "HttpCache")
        return Cache(httpCacheDirectory, CACHE_SIZE_BYTES)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        baseUrl: String,
        gson: Gson,
        client: OkHttpClient
    ): ApiService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client.also { Log.d("OkHttpClient", "Retrofit is using provided OkHttpClient") })
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)
}