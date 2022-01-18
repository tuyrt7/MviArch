package com.tuyrt.mvi.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuyrt.mvi.data.NewsRepository
import com.tuyrt.mvi.data.bean.NewsItem
import com.tuyrt.mvi.ext.livedata.SingleLiveEvent
import com.tuyrt.mvi.ext.setEvent
import com.tuyrt.mvi.ext.setState
import com.tuyrt.mvi.ext.FetchStatus
import com.tuyrt.mvi.ext.PageState
import com.tuyrt.mvi.ext.asLiveData
import kotlinx.coroutines.launch

/**
 * Created by tuyrt7 on 2022/1/17.
 * 说明：
 */
class MainViewModel : ViewModel() {

    private var count: Int = 0
    private val repository: NewsRepository = NewsRepository.getInstance()

    private val _viewStates = MutableLiveData(MainViewState())
    val viewStates = _viewStates.asLiveData()
    private val _viewEvents: SingleLiveEvent<MainViewEvent> = SingleLiveEvent()
    val viewEvents = _viewEvents.asLiveData()

    init {
        emit(MainViewState(fetchStatus = FetchStatus.NotFetched, newsList = emptyList()))
    }

    fun dispatch(viewAction: MainViewAction) {
        when (viewAction) {
            is MainViewAction.NewsItemClicked -> newsItemClicked(viewAction.newsItem)
            MainViewAction.FabClicked -> fabClicked()
            MainViewAction.OnSwipeRefresh -> fetchNews()
            MainViewAction.FetchNews -> fetchNews()
        }
    }

    private fun newsItemClicked(newsItem: NewsItem) {
        _viewEvents.value = MainViewEvent.ShowToast(message = newsItem.title)
    }

    private fun fabClicked() {
        count++
        emit(MainViewEvent.ShowToast(message = "Fab clicked count $count"))
    }

    private fun fetchNews() {
        // 更新页面状态
        _viewStates.setState {
            copy(fetchStatus = FetchStatus.Fetching)
        }

        viewModelScope.launch {
            when (val result = repository.getMockApiResponse()) {
                is PageState.Error -> {
                    _viewStates.setState {
                        copy(fetchStatus = FetchStatus.Fetched)
                    }
                    _viewEvents.setEvent(MainViewEvent.ShowToast(message = result.message))
                }
                is PageState.Success -> {
                    _viewStates.setState {
                        copy(fetchStatus = FetchStatus.Fetched, newsList = result.data)
                    }
                }
            }
        }
    }

    private fun emit(state: MainViewState) {
        _viewStates.value = state
    }

    private fun emit(event: MainViewEvent) {
        _viewEvents.value = event
    }


}