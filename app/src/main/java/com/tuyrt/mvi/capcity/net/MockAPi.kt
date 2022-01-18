package com.tuyrt.mvi.capcity.net

import com.tuyrt.mvi.data.bean.MockApiResponse
import retrofit2.http.GET

/**
 * Created by tuyrt7 on 2022/1/17.
 * 说明：
 */
interface MockAPi {

    companion object {
        const val BASE_URL = "https://your_api_endpoint.com/"
    }

    @GET("mock")
    suspend fun getLatestNews(): MockApiResponse
}