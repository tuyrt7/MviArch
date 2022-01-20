package com.tuyrt.mvi.ui.network

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuyrt.mvi.capcity.net.base.commonCatch
import com.tuyrt.mvi.capcity.net.base.launchUI
import com.tuyrt.mvi.ext.asLiveData
import com.tuyrt.mvi.ext.livedata.SingleLiveEvents
import com.tuyrt.mvi.ext.setEvent
import com.tuyrt.mvi.ext.setState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by tuyrt7 on 2022/1/18.
 * 说明：
 */
class FlowViewModel : ViewModel() {

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
        viewModelScope.launch {
            flow {
                delay(2000)
                emit("页面请求成功")
            }.onStart {
                _viewStates.setState { copy(pageState = PageStatus.Loading) }
            }.onEach {
                _viewStates.setState { copy(content = it, pageState = PageStatus.Success) }
                _viewEvents.setEvent(NetworkViewEvent.ShowToast("请求成功"))
            }.commonCatch {
                _viewStates.setState { copy(pageState = PageStatus.Error(it)) }
            }.collect()
        }
    }

    /**
     * 页面局部请求，例如点赞收藏等，通常需要弹dialog或toast
     */
    private fun partRequest() {
        viewModelScope.launch {
            flow {
                delay(2000)
                emit("点赞成功")
            }.onStart {
                _viewEvents.setEvent(NetworkViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog, NetworkViewEvent.ShowToast(it))
                _viewStates.setState { copy(content = it) }
            }.commonCatch {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
            }.collect()
        }
    }

    /**
     * 多数据源请求
     */
    private fun multiRequest() {
        viewModelScope.launch {
            val flow1 = flow {
                delay(1000)
                emit("数据源1")
            }

            val flow2 = flow {
                delay(2000)
                emit("数据源2")
            }

            flow1.zip(flow2) { a, b ->
                "$a,$b"
            }.onStart {
                _viewEvents.setEvent(NetworkViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog, NetworkViewEvent.ShowToast(it))
                _viewStates.setState { copy(content = it) }
            }.commonCatch {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
            }.collect()
        }
    }

    private fun errorRequest() {
        viewModelScope.launch {
            flow {
                delay(2000)
                throw NullPointerException("测试 Null Error")
                emit("请求失败")
            }.onStart {
                _viewEvents.setEvent(NetworkViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog, NetworkViewEvent.ShowToast(it))
                _viewStates.setState { copy(content = it) }
            }.commonCatch {
                _viewEvents.setEvent(NetworkViewEvent.DismissLoadingDialog)
            }.collect()
        }
    }
}