package com.tuyrt.mvi.capcity.net.base

import com.tuyrt.mvi.MyApp
import com.tuyrt.mvi.ext.toast
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext


/**
 * 通用异常处理
 * 在这里处理一些页面通用的异常逻辑
 * 可以根据业务需求自定义
 */
class NetworkExceptionHandler(
    private val onException: (e: Throwable) -> Unit = {}
) : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        onException.invoke(exception)
        if (exception is UnknownHostException || exception is SocketTimeoutException) {
            MyApp.get().toast("发生网络错误，请稍后重试")
        } else {
            MyApp.get().toast("请求失败，请重试")
        }
    }
}

fun <T> Flow<T>.commonCatch(action: FlowCollector<T>.(Throwable) -> Unit): Flow<T> {
    return this.catch {
        if (it is UnknownHostException || it is SocketTimeoutException) {
            MyApp.get().toast("发生网络错误，请稍后重试")
        } else {
            MyApp.get().toast("请求失败，请重试")
        }
        action(it)
    }
}