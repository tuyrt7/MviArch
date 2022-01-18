package com.tuyrt.mvi.capcity.net.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by tuyrt7 on 2022/1/18.
 * 说明：协程封装
 */
class CoroutineScopeHelper<T>(private val coroutineScope: CoroutineScope) {
    fun rxLaunch(init: LaunchBuilder<T>.() -> Unit): Job {
        val result = LaunchBuilder<T>().apply(init)
        val handler = NetworkExceptionHandler {
            result.onError?.invoke(it)
        }

        return coroutineScope.launch(handler) {
            val response: T = result.onRequest()
            result.onSuccess?.invoke(response)
        }
    }
}

class LaunchBuilder<T> {
    lateinit var onRequest: (suspend () -> T)
    var onSuccess: ((data: T) -> Unit)? = null
    var onError: ((Throwable) -> Unit)? = null
}

fun <T> CoroutineScope.launchUI(init: LaunchBuilder<T>.() -> Unit) {
    val scopeHelper = CoroutineScopeHelper<T>(this)
    scopeHelper.rxLaunch(init)
}