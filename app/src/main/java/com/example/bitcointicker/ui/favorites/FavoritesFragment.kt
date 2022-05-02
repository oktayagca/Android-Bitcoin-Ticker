package com.example.bitcointicker.ui.favorites

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitcointicker.R
import com.example.bitcointicker.data.entities.local.CoinEntity
import com.example.bitcointicker.databinding.FragmentFavoritesBinding
import com.example.bitcointicker.ui.MainActivity
import com.example.bitcointicker.ui.base.BaseFragment
import com.example.bitcointicker.ui.home.CoinsRecyclerViewAdapter
import com.example.bitcointicker.ui.home.ICoinsListClickListener
import com.example.bitcointicker.utils.gone
import com.example.bitcointicker.utils.handleResponseData
import com.example.bitcointicker.utils.navigateFragment
import com.example.bitcointicker.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>() {

    private val viewModel: FavoritesViewModel by viewModels()
    private var adapter: CoinsRecyclerViewAdapter = CoinsRecyclerViewAdapter()

    override fun getViewBinding(): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(layoutInflater)
    }

    override fun onFragmentCreate() {
        super.onFragmentCreate()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(binding){
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onResume() {
        super.onResume()
        initViewsByLoginState()
    }

    private fun initViewsByLoginState() {
        if (!viewModel.isAuthUser()) {
            binding.apply {
                goLoginLayout.show()
                goLoginButton.setOnClickListener {
                    navigateFragment(actionId = R.id.action_favoritesFragment_to_loginFragment)
                }
            }
        } else {
            binding.apply {
                goLoginLayout.gone()
                getFavoriteList()
            }
        }
    }

    private fun getFavoriteList(){
        handleResponseData(
            loadingState = {
                (requireActivity() as MainActivity).showLoading()
            },
            successState = { response->
                (requireActivity() as MainActivity).stopLoading()
                setRecyclerViewData(response?.toList())
            },
            errorState = {
                (requireActivity() as MainActivity).stopLoading()
            },
            viewModel.getFavoritesList())
    }

    private fun setRecyclerViewData(items: List<CoinEntity>?) {
        items?.forEach {
            Log.v("coinEntity",it.id)
        }
        adapter.setData(
            items,
            object : ICoinsListClickListener {
                override fun onClick(item: CoinEntity) {
                    navigateFragment(
                        navDirection = FavoritesFragmentDirections.actionFavoriteFragmentToCoinDetailFragment(
                            item.id,
                            item.currentPrice.toString(),
                            item.marketCapChangePercentage24h.toString()
                        )
                    )
                }
            })
    }
}
