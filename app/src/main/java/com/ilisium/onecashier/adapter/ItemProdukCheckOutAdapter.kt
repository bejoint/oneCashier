package com.ilisium.onecashier.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilisium.onecashier.databinding.ItemProductHistoryBinding
import com.ilisium.onecashier.helper.Tools
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.response.DetailHistoryResponse
import com.ilisium.onecashier.view.fragment.history.DetailHistoryFragment

class ItemProdukCheckOutAdapter(
    private val context: Context,
    var data: List<DetailHistoryResponse.Response.TransactionDetail>
) :
    RecyclerView.Adapter<ItemProdukCheckOutAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemProductHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var priceProduk = binding.priceProduk
        var nameProduk = binding.nameProduk
        var valueVarian = binding.valueVarian
        var imgProduk = binding.imgProduk
        var countProduk = binding.countProduk

        var parentLy = binding.parentLayout

        fun bind(data: DetailHistoryResponse.Response.TransactionDetail, pos: Int) {
            nameProduk.text = data.product.name
            priceProduk.text = Tools.intToRupiah(data.product.price.toDouble().toInt().toString())

            valueVarian.text = data.item_final_name

            countProduk.text = "(" + data.quantity.toString() + ")"

            Glide.with(context)
                .load(data.product.image)
                .into(imgProduk)

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemProdukCheckOutAdapter.MyViewHolder {
        return MyViewHolder(parent.viewBinding(ItemProductHistoryBinding::inflate))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data!![position]
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }
}