package com.tuyrt.mvi.data

import com.tuyrt.mvi.capcity.net.MockAPi
import com.tuyrt.mvi.capcity.net.RetrofitClient
import com.tuyrt.mvi.data.bean.NewsItem
import com.tuyrt.mvi.ext.PageState
import kotlinx.coroutines.delay

/**
 * Created by tuyrt7 on 2022/1/14.
 * 说明：
 */
class NewsRepository private constructor(){

    private val mockApi = RetrofitClient.create(MockAPi::class.java, MockAPi.BASE_URL)

    companion object {
        @Volatile
        private var instance: NewsRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: NewsRepository().also { instance = it }
        }
    }

    suspend fun getMockApiResponse(): PageState<List<NewsItem>> {
        val articlesApiResult = try {
            delay(200)
            mockApi.getLatestNews()
        } catch (e: Exception) {
            return PageState.Error(e)
        }

        articlesApiResult.articles?.let { list ->
            return PageState.Success(data = list)
        } ?: run {
            return PageState.Error("Failed to get News")
        }
    }
}