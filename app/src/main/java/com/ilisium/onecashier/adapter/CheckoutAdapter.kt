package com.ilisium.onecashier.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilisium.onecashier.databinding.ItemCheckoutBinding
import com.ilisium.onecashier.helper.Tools
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.response.IndexCartResponse

class CheckoutAdapter(
    private val context: Context,
    var data: List<IndexCartResponse.Response>,
    val cellClickListener: CellClickListener
) :
    RecyclerView.Adapter<CheckoutAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemCheckoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var priceProduk = binding.priceProduk
        var nameProduk = binding.nameProduk
        var valueVarian = binding.valueVarian
        var imgProduk = binding.imgProduk

        var countProduk = binding.countProduk
        var plus = binding.imgPlus
        var min = binding.imgMin

        var imgEdit = binding.imgEdit
        var imgDelete = binding.imgDelete

        var parentLy = binding.parentLayout

        fun bind(data: IndexCartResponse.Response, pos: Int) {
            nameProduk.text = data.product.name
            priceProduk.text = Tools.intToRupiah(data.sub_total_price_final.toDouble().toInt().toString())

            valueVarian.text = data.item_final_name

            countProduk.text = data.quantity.toString()

            Glide.with(context)
                .load(data.product.image)
                .into(imgProduk)

            plus.setOnClickListener {
                cellClickListener.updateQty(1, data)
            }

            min.setOnClickListener {
                if (countProduk.text.toString().toInt() <= 1){

                } else {
                    cellClickListener.updateQty(-1, data)
                }
            }

            imgEdit.setOnClickListener {
                cellClickListener.editCart(data)
            }

            imgDelete.setOnClickListener {
                cellClickListener.deleteItem(data)
            }

//            parentLy.setOnClickListener {
//                cellClickListener.selectVarian(data)
//            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheckoutAdapter.MyViewHolder {
        return MyViewHolder(parent.viewBinding(ItemCheckoutBinding::inflate))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data!![position]
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    interface CellClickListener {
        fun selectVarian(data: IndexCartResponse.Response)
        fun editCart(data: IndexCartResponse.Response)
        fun deleteItem(data: IndexCartResponse.Response)
        fun updateQty(qty: Int, data: IndexCartResponse.Response)
    }
}