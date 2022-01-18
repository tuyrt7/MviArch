package com.tuyrt.mvi.capcity.net

import com.tuyrt.mvi.capcity.net.base.BaseRetrofitClient
import okhttp3.OkHttpClient.Builder

/**
 * Created by tuyrt7 on 2022/1/14.
 * 说明：
 */
object RetrofitClient : BaseRetrofitClient() {

    override fun handleBuilder(builder: Builder) {
        builder.addInterceptor(MockInterceptor())
    }
}