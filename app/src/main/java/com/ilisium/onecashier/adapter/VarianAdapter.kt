package com.ilisium.onecashier.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilisium.onecashier.databinding.ItemVarianBinding
import com.ilisium.onecashier.helper.Tools
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.response.ProductDetailResponse

class VarianAdapter(
    private val context: Context,
    var data: List<
            ProductDetailResponse.Response.ProductVariant>,
    val cellClickListener: CellClickListener
) :
    RecyclerView.Adapter<VarianAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemVarianBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var varian = binding.varian
        var price = binding.price

        var cbPrice = binding.cbPrice
        var parentLy = binding.parentLayout

        fun bind(data: ProductDetailResponse.Response.ProductVariant, pos: Int) {
            varian.text = data.name
            price.text = Tools.intToRupiah(data.price.toDouble().toInt().toString())

            cbPrice.setOnClickListener {
                if (cbPrice.isChecked){
                    cellClickListener.selectVarian(data, true)
                } else {
                    cellClickListener.selectVarian(data, false)
                }
            }

            parentLy.setOnClickListener {
//                cellClickListener.selectVarian(data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VarianAdapter.MyViewHolder {
        return MyViewHolder(parent.viewBinding(ItemVarianBinding::inflate))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data!![position]
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    interface CellClickListener {
        fun selectVarian(data: ProductDetailResponse.Response.ProductVariant, checked: Boolean)
    }
}