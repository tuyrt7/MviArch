package com.tuyrt.mvi

import android.app.Application
import com.drake.statelayout.StateConfig

/**
 * Created by tuyrt7 on 2022/1/18.
 * 说明：
 */
class MyApp : Application() {

    companion object {
        private lateinit var instance: MyApp
        fun get(): MyApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        statusConfig()
    }

    private fun statusConfig() {
        StateConfig.apply {
            loadingLayout = R.layout.layout_loading
            errorLayout = R.layout.layout_error
        }
    }
}