package com.tuyrt.mvi.ui.main


import com.tuyrt.mvi.data.bean.NewsItem
import com.tuyrt.mvi.ext.FetchStatus

/**
 * Created by tuyrt7 on 2022/1/17.
 * 说明：
 */
data class MainViewState(
    val fetchStatus: FetchStatus = FetchStatus.NotFetched,
    val newsList: List<NewsItem> = emptyList()
)



sealed class MainViewEvent {
    data class ShowSnackBar(val message: String) : MainViewEvent()
    data class ShowToast(val message: String) : MainViewEvent()
}

sealed class MainViewAction {
    data class NewsItemClicked(val newsItem: NewsItem) : MainViewAction()
    object FabClicked : MainViewAction()
    object OnSwipeRefresh : MainViewAction()
    object FetchNews : MainViewAction()
}