package com.example.rmovies.data.retrofit

import com.example.rmovies.data.api.TMDBapi
import com.example.rmovies.utils.Constants.API_TOKEN
import com.example.rmovies.utils.Constants.API_TOKEN_KEY
import com.example.rmovies.utils.Constants.BASE_URL
import com.example.rmovies.utils.Constants.QUERY_PARAM_LANGUAGE_KEY
import com.example.rmovies.utils.Constants.QUERY_PARAM_LANGUAGE_VALUE
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {

    val tmdbApi: TMDBapi = getTMDBApiClient().create(TMDBapi::class.java)

    fun getTMDBApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getInterceptorClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getInterceptorClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        val interceptor = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val url = chain.request().url.newBuilder()
                    .addQueryParameter(API_TOKEN_KEY, API_TOKEN)
                    .addQueryParameter(QUERY_PARAM_LANGUAGE_KEY, QUERY_PARAM_LANGUAGE_VALUE)
                    .build()
                val newRequest = chain.request().newBuilder().url(url).build()
                chain.proceed(newRequest)
            }
        return interceptor.build()
    }
}