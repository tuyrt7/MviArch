package com.tuyrt.mvi.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tuyrt.mvi.R
import com.tuyrt.mvi.ui.main.MainActivity
import com.tuyrt.mvi.ui.network.FlowActivity
import com.tuyrt.mvi.ui.network.NetworkActivity

/**
 * Created by tuyrt7 on 2022/1/18.
 * 说明：
 */
class RouterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_router)
    }

    fun simple(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun network(view: View) {
        startActivity(Intent(this, NetworkActivity::class.java))
    }

    fun flow(view: View) {
        startActivity(Intent(this, FlowActivity::class.java))
    }
}