package com.tuyrt.mvi.capcity.net.base

import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by tuyrt7 on 2022/1/17.
 * 说明：
 */
abstract class BaseRetrofitClient {

    private val kkHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient().newBuilder()
        handleBuilder(builder)
        builder.build()
    }

    abstract fun handleBuilder(builder: Builder)

    fun <API> create(apiClazz: Class<API>, baseUrl: String): API {
        return Retrofit.Builder()
            .client(kkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
            .create(apiClazz)
    }
}