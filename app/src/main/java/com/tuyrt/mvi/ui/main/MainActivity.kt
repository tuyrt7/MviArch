package com.tuyrt.mvi.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.hi.dhl.binding.viewbind
import com.tuyrt.mvi.data.bean.NewsItem
import com.tuyrt.mvi.databinding.ActivityMainBinding
import com.tuyrt.mvi.ext.observeState
import com.tuyrt.mvi.ui.main.*
import com.tuyrt.mvi.ext.FetchStatus
import com.tuyrt.mvi.ext.toast

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val binding: ActivityMainBinding by viewbind()

    private val newsRvAdapter by lazy {
        NewsRvAdapter {
            viewModel.dispatch(MainViewAction.NewsItemClicked(it.tag as NewsItem))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() {
        binding.run {
            rvNewsHome.adapter = newsRvAdapter

            srlNewsHome.setOnRefreshListener {
                viewModel.dispatch(MainViewAction.OnSwipeRefresh)
            }

            fabStar.setOnClickListener {
                viewModel.dispatch(MainViewAction.FabClicked)
            }
        }
    }

    private fun initViewModel() {
        viewModel.viewStates.run {
            observeState(this@MainActivity, MainViewState::newsList) {
                newsRvAdapter.submitList(it)
                newsRvAdapter.notifyDataSetChanged()
            }

            observeState(this@MainActivity, MainViewState::fetchStatus) {
                renderViewState(it)
            }
        }

        viewModel.viewEvents.observe(this) {
            renderViewEvent(it)
        }
    }

    private fun renderViewState(viewState: FetchStatus) {
        when (viewState) {
            is FetchStatus.Fetched -> {
                binding.srlNewsHome.isRefreshing = false
            }
            is FetchStatus.NotFetched -> {
                viewModel.dispatch(MainViewAction.FetchNews)
                binding.srlNewsHome.isRefreshing = false
            }
            is FetchStatus.Fetching ->  {
                binding.srlNewsHome.isRefreshing = true
            }
        }
    }

    private fun renderViewEvent(viewEvent: MainViewEvent) {
        when (viewEvent) {
            is MainViewEvent.ShowSnackBar -> {
                Snackbar.make(binding.coordinatorLayoutRoot, viewEvent.message, Snackbar.LENGTH_SHORT).show()
            }
            is MainViewEvent.ShowToast -> {
                toast(message = viewEvent.message)
            }
        }
    }

}