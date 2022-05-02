package com.example.bitcointicker.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<viewBinding : ViewBinding> :
    AppCompatActivity() {

    private var _binding: viewBinding? = null
    val binding get() = _binding!!
    protected abstract fun getViewBinding(): viewBinding
    private fun init() {
        _binding = getViewBinding()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        themeSet()
        super.onCreate(savedInstanceState)
        init()
        setContentView(binding.root)
        onCreateActivity()
    }

    abstract fun themeSet()

    override fun onDestroy() {
        super.onDestroy()
        onDestroyActivity()
        _binding = null
    }

    abstract fun onCreateActivity()

    open fun onDestroyActivity() {}
}