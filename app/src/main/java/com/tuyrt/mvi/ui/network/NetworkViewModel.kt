package com.tuyrt.mvi.ui.network

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuyrt.mvi.capcity.net.base.launchUI
import com.tuyrt.mvi.ext.asLiveData
import com.tuyrt.mvi.ext.livedata.SingleLiveEvents
import com.tuyrt.mvi.ext.setEvent
import com.tuyrt.mvi.ext.setState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import java.lang.NullPointerException

/**
 * Created by tuyrt7 on 2022/1/18.
 * 说明：
 */
class NetworkViewModel : ViewModel() {

    private val _viewStates = MutableLiveData(NetworkViewState())
    val viewStates = _viewStates.asLiveData()
    private val _viewEvents: SingleLiveEvents<NetworkViewEvent> = SingleLiveEvents()
    val viewEvents = _viewEvents.asLiveData()

    fun dispatch(viewAction: NetworkViewAction) {
        when (viewAction) {
            NetworkViewAction.PageRequest -> pageRequest()
            NetworkViewAction.PartRequest -> partRequest()
            NetworkViewAction.MultiRequest -> multiRequest()
            NetworkViewAction.ErrorRequest -> errorRequest()
        }
    }

    /**
     *  页面请求，通常包括刷新页面loading状态等
     */
    private fun pageRequest() {
        viewModelScope.launchUI<String> {
            onRequest = {
                _viewStates.setState { copy(pageState = PageStatus.Loading) }
                delay(2000)
                "页面请求成功"
            }

            onSuccess = {
                _viewStates.setState { copy(content = it, pageState = PageStatus.Success) }
                _viewEvents.setEvent(NetworkViewEvent.ShowToast("请求成功"))
            }

            onError = {
                _viewStates.setState { copy(pageState = PageStatus.Error(it)) }
            }
        }
    }

    /**
     * 页面局部请求，例如点赞收藏等，通常需要弹dialog或toast
     */
    private fun partRequest() {
        viewModelScope.launchUI<String> {
            onRequest = {
                _viewEvents.setEvent(NetworkViewEvent.ShowLoadingDialog)
                delay(2000)
                "点赞成功"
            }
            onSuccess = {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
                _viewEvents.setEvent(NetworkViewEvent.ShowToast(it))
                _viewStates.setState { copy(content = it) }
            }
            onError = {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
            }
        }
    }

    /**
     * 多数据源请求
     */
    private fun multiRequest() {
        viewModelScope.launchUI<String> {
            onRequest = {
                _viewEvents.setEvent(NetworkViewEvent.ShowLoadingDialog)

                coroutineScope {
                    val source1 = async { source1() }
                    val source2 = async { source2() }

                    val result = source1.await() + "," + source2.await()
                    result
                }
            }
            onSuccess = {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog, NetworkViewEvent.ShowToast(it))
                _viewStates.setState { copy(content = it) }
            }
            onError = {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
            }
        }
    }

    private fun errorRequest() {
        viewModelScope.launchUI<String> {
            onRequest = {
                _viewEvents.setEvent(NetworkViewEvent.ShowLoadingDialog)
                delay(2000)
                throw NullPointerException("")
                "请求失败"
            }
            onSuccess = {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog, NetworkViewEvent.ShowToast(it))
                _viewStates.setState { copy(content = it) }
            }
            onError = {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
            }
        }
    }

    private suspend fun source1(): String {
        delay(1000)
        return "数据源1"
    }

    private suspend fun source2(): String {
        delay(2000)
        return "数据源2"
    }
}