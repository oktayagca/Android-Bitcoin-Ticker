package com.example.bitcointicker.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


abstract class BaseFragment<VBinding : ViewBinding> : Fragment() {

    private var _binding: VBinding? = null
    protected val binding get() = _binding!!
    protected abstract fun getViewBinding(): VBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        onFragmentCreate()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyFragment()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onFragmentCreated()
    }

    open fun onFragmentCreate() {}

    open fun onFragmentCreated() {}

    open fun onDestroyFragment() {}

    private fun init() {
        _binding = getViewBinding()

    }

}