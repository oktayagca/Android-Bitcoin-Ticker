package com.example.bitcointicker.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bitcointicker.R
import com.example.bitcointicker.data.entities.local.CoinEntity
import com.example.bitcointicker.databinding.ItemCoinBinding
import com.example.bitcointicker.utils.loadImagesWithGlide

class CoinsRecyclerViewAdapter :
    RecyclerView.Adapter<CoinsRecyclerViewAdapter.CoinsRecyclerViewAdapterViewHolder>() {

    private lateinit var binding: ItemCoinBinding
    private var items: List<CoinEntity> = listOf()
    private lateinit var onClickListener: ICoinsListClickListener

    class CoinsRecyclerViewAdapterViewHolder(private val binding: ItemCoinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CoinEntity?, onClickListener: ICoinsListClickListener) {
            binding.apply {
                item?.let {
                    rootLayout.setOnClickListener {
                        onClickListener.onClick(item)
                    }
                    it.image?.let { it1 -> shapeAbleCoinImageView.loadImagesWithGlide(it1) }
                    coinNameTextView.text = item.name
                    coinSymbolTextView.text = item.symbol
                    coinPriceTextView.text = binding.root.context.getString(
                        R.string.currentPrice,
                        item.currentPrice.toString()
                    )
                    coinPriceChangeTextView.text = binding.root.context.getString(
                        R.string.priceChange,
                        item.priceChangePercentage24h.toString()
                    )
                    if (item.priceChangePercentage24h!! <= 0) {
                        coinPriceChangeImageView.setImageDrawable(
                            ContextCompat.getDrawable(
                                binding.root.context,
                                R.drawable.ic_baseline_arrow_drop_down_24
                            )
                        )
                        coinPriceChangeLayout.background = ContextCompat.getDrawable(
                            binding.root.context,
                            R.drawable.red_rectangle_rounded8_box
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: List<CoinEntity>?, onClickListener: ICoinsListClickListener) {
        if (items != null) {
            this.items = items
        }
        this.onClickListener = onClickListener
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoinsRecyclerViewAdapterViewHolder {
        binding = ItemCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinsRecyclerViewAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinsRecyclerViewAdapterViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onClickListener)
    }

    override fun getItemCount(): Int = items.size
}