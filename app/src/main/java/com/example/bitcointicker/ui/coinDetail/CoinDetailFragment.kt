package com.example.bitcointicker.ui.coinDetail

import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bitcointicker.R
import com.example.bitcointicker.data.entities.remote.CoinChartResponse
import com.example.bitcointicker.data.entities.remote.CoinDetailResponse
import com.example.bitcointicker.databinding.FragmentCoinDetailBinding
import com.example.bitcointicker.ui.MainActivity
import com.example.bitcointicker.ui.base.BaseFragment
import com.example.bitcointicker.utils.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CoinDetailFragment : BaseFragment<FragmentCoinDetailBinding>() {

    private val viewModel: CoinDetailViewModel by viewModels()
    private val args: CoinDetailFragmentArgs by navArgs()
    private var handler: Handler? = null
    private val refreshRunnable = Runnable {
        getCoinDetail()
        getCoinChart()
    }

    override fun getViewBinding(): FragmentCoinDetailBinding {
        return FragmentCoinDetailBinding.inflate(layoutInflater)
    }

    override fun onFragmentCreate() {
        binding.apply {
            backButton.setOnClickListener {
                goBack()
            }
            defineRefreshIntervalButton.setOnClickListener {
                navigateFragment(actionId = R.id.action_coinDetailFragment_to_refreshTimeBottomSheet)
            }

        }
        handler = Handler(Looper.getMainLooper())
    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        getCoinDetail()
        getCoinChart()
    }

    override fun onDestroyFragment() {
        super.onDestroyFragment()
        stopRunnable()
    }


    override fun onResume() {
        super.onResume()
        setBackStackEntryObserver()
    }

    private fun getCoinDetail() {
        handleResponseData(
            loadingState = {
                (requireActivity() as MainActivity).showLoading()
            },
            successState = { response ->
                binding.rootLayout.show()
                (requireActivity() as MainActivity).stopLoading()
                initViews(response)
            },
            errorState = {
                (requireActivity() as MainActivity).stopLoading()
            },
            viewModel.getCoinDetailFromNetwork(args.coinId)
        )
    }

    private fun getCoinChart() {
        handleResponseData(
            loadingState = {
            },
            successState = { response ->
                startRunnable()
                binding.rootLayout.show()
                (requireActivity() as MainActivity).stopLoading()
                initChart(response)
            },
            errorState = {
                startRunnable()
            },
            viewModel.getCoinChart(args.coinId)
        )
    }

    private fun initChart(chartData: CoinChartResponse?) {
        if (chartData != null) {
            val entries = arrayListOf<Entry>()
            chartData.prices.forEachIndexed { index, doubles ->
                entries.add(Entry(index.toFloat(), doubles[1].toFloat()))
            }
            val line = LineDataSet(entries, "Price")
            line.apply {
                color = ContextCompat.getColor(requireContext(), R.color.green)
                setDrawCircles(false)
            }
            binding.lineChart.data = LineData(line)
            binding.lineChart.invalidate()

        }

    }

    private fun initViews(response: CoinDetailResponse?) {
        binding.apply {
            if (response != null) {
                coinImageView.loadImagesWithGlide(response.image.small)
                coinNameTextView.text = response.name
                descTextView.text =
                    HtmlCompat.fromHtml(response.description.en, HtmlCompat.FROM_HTML_MODE_LEGACY)
                priceTextView.text = getString(R.string.currentPrice, args.currentPrice)
                coinPriceChangeTextView.text = getString(R.string.priceChange, args.priceChange)
                if (args.priceChange.contains("-")) {
                    coinPriceChangeTextView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                }
            }
        }
        observeIsFavoriteAndInitFavoriteButton(args.coinId)
    }

    private fun observeIsFavoriteAndInitFavoriteButton(coinId: String) {
        if (!viewModel.isAuthUser()) {
            requireContext().showToastMessage("Please login")
            binding.favoriteButton.setOnClickListener{
                navigateFragment(actionId = R.id.action_favoritesFragment_to_loginFragment)
            }
        } else{
            viewModel.observeIsFavorite(coinId).observe(viewLifecycleOwner) { isFavorite ->
                binding.favoriteButton.apply {
                    isChecked = isFavorite
                    setOnClickListener {
                        if (isFavorite) {
                            handleResponseData(
                                loadingState = {
                                    (requireActivity() as MainActivity).showLoading()
                                },
                                successState = {
                                    requireContext().showToastMessage("Removed from favorites")
                                    (requireActivity() as MainActivity).stopLoading()
                                },
                                errorState = {
                                    requireContext().showToastMessage(it)
                                    (requireActivity() as MainActivity).stopLoading()

                                },
                                viewModel.removeCoinToFavorite(coinId)
                            )
                        } else {
                            handleResponseData(
                                loadingState = {
                                    (requireActivity() as MainActivity).showLoading()
                                },
                                successState = {
                                    requireContext().showToastMessage("Added to favorites")
                                    (requireActivity() as MainActivity).stopLoading()
                                },
                                errorState = {
                                    requireContext().showToastMessage(it)
                                    (requireActivity() as MainActivity).stopLoading()

                                },
                                viewModel.saveCoinToFavorite(coinId)
                            )

                        }
                    }
                }
            }
        }

    }

    private fun stopRunnable() {
        handler!!.removeCallbacks(refreshRunnable)
        handler = null
    }

    private fun startRunnable() {
        if (handler != null) {
            if (viewModel.getRefreshTime().equals("")) {
                viewModel.getRefreshTime()?.let { handler!!.postDelayed(refreshRunnable, 10000) }
            } else {
                viewModel.getRefreshTime()
                    ?.let { handler!!.postDelayed(refreshRunnable, it.toLong()) }
            }
        }


    }

    private fun setBackStackEntryObserver() {
        val savedStateHandle = findNavController().currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<String>("time")?.observe(viewLifecycleOwner) { time ->
            if (time != null) {
                viewModel.setRefreshTime(time)
                savedStateHandle.remove<String>("time")
            }
        }
    }


}