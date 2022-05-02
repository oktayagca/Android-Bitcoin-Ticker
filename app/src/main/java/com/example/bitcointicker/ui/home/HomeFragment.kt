package com.example.bitcointicker.ui.home

import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitcointicker.R
import com.example.bitcointicker.data.entities.local.CoinEntity
import com.example.bitcointicker.databinding.FragmentHomeBinding
import com.example.bitcointicker.ui.MainActivity
import com.example.bitcointicker.ui.base.BaseFragment
import com.example.bitcointicker.utils.handleResponseData
import com.example.bitcointicker.utils.navigateFragment
import com.example.bitcointicker.utils.showToastMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()
    private var adapter: CoinsRecyclerViewAdapter = CoinsRecyclerViewAdapter()

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onFragmentCreate() {
        super.onFragmentCreate()
        initViews()
    }

    override fun onResume() {
        super.onResume()
        initLoginLogoutButton()
    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        handleResponseData(
            loadingState = {
                (requireActivity() as MainActivity).showLoading()
            },
            successState = { response ->
                (requireActivity() as MainActivity).stopLoading()
                setRecyclerViewData(response)
            },
            errorState = {
                (requireActivity() as MainActivity).stopLoading()
            },
            liveData = viewModel.observeCoinList()
        )
    }

    private fun initLoginLogoutButton() {
        if(viewModel.isAuthUser()){
            binding.loginLogoutButton.apply {
                setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_logout_24))
                setOnClickListener{
                    viewModel.logOut()
                    requireContext().showToastMessage("Session Closed")
                    initLoginLogoutButton()
                }
            }
        }
        else{
            binding.loginLogoutButton.apply {
                setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_login_24))
                setOnClickListener {
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                }
            }
        }
    }

    private fun initViews() {
        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)

            searchView.isSubmitButtonEnabled = true
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    p0?.let {
                        viewModel.searchCoin(it)
                    }
                    return true
                }

            })
        }

    }

    private fun setRecyclerViewData(items: List<CoinEntity>?) {
        adapter.setData(
            items,
            object : ICoinsListClickListener {
                override fun onClick(item: CoinEntity) {
                    navigateFragment(
                        navDirection = HomeFragmentDirections.actionHomeFragmentToCoinDetailFragment(
                            item.id,
                            item.currentPrice.toString(),
                            item.marketCapChangePercentage24h.toString()
                        )
                    )
                }
            })
    }
}