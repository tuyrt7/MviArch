package com.tuyrt.mvi.ui.network

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.drake.statelayout.StateLayout
import com.hi.dhl.binding.viewbind
import com.tuyrt.mvi.databinding.ActivityNetworkBinding
import com.tuyrt.mvi.ext.observeEvent
import com.tuyrt.mvi.ext.observeState
import com.tuyrt.mvi.ext.toast

/**
 * Created by tuyrt7 on 2022/1/18.
 * 说明：
 */
class FlowActivity : AppCompatActivity() {
    private val TAG = "FlowActivity"

    private val binding: ActivityNetworkBinding by viewbind()
    private val viewModel: FlowViewModel by viewModels()

    private val stateLayout: StateLayout by lazy { binding.stateLayout }
    private val tvContent: TextView by lazy { binding.tvContent }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() {
        binding.run { }
    }

    private fun initViewModel() {
        viewModel.viewStates.let { state ->
            //监听网络请求状态
            state.observeState(this, NetworkViewState::pageState) {
                when (it) {
                    is PageStatus.Success -> stateLayout.showContent()
                    is PageStatus.Loading -> stateLayout.showLoading()
                    is PageStatus.Error -> stateLayout.showError()
                }
            }
            //监听页面数据
            state.observeState(this, NetworkViewState::content) {
                tvContent.text = it
            }
        }

        //监听一次性事件，如Toast,ShowDialog等
        viewModel.viewEvents.observeEvent(this) {
            when (it) {
                is NetworkViewEvent.ShowToast -> toast(it.message)
                is NetworkViewEvent.ShowLoadingDialog -> showLoadingDialog()
                is NetworkViewEvent.DismissLoadingDialog -> dismissLoadingDialog()
            }
        }
    }


    fun simpleRequest(view: View) {
        viewModel.dispatch(NetworkViewAction.PageRequest)
    }

    fun partRequest(view: View) {
        viewModel.dispatch(NetworkViewAction.PartRequest)
    }

    fun multiSource(view: View) {
        viewModel.dispatch(NetworkViewAction.MultiRequest)
    }

    fun errorRequest(view: View) {
        viewModel.dispatch(NetworkViewAction.ErrorRequest)
    }


    private var progressDialog: ProgressDialog? = null

    private fun showLoadingDialog() {
        if (progressDialog == null)
            progressDialog = ProgressDialog(this)
        progressDialog?.show()
    }

    private fun dismissLoadingDialog() {
        progressDialog?.takeIf { it.isShowing }?.dismiss()
    }
}