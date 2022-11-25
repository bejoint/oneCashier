package com.ilisium.onecashier.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilisium.onecashier.databinding.ItemHistoryBinding
import com.ilisium.onecashier.helper.Tools
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.response.HistoryResponse

class HistoryAdapter(
    private val context: Context,
    var data: List<HistoryResponse.Response>,
    val cellClickListener: CellClickListener
) :
    RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var nameOutlet = binding.nameOutlet
        var date = binding.date
        var totalBayar = binding.totalBayar
        var jumlahItem = binding.jumlahItem
        var moneyChange = binding.moneyChange

        var parentLayout = binding.parentLayout

        fun bind(data: HistoryResponse.Response, pos: Int) {
            nameOutlet.text = data.outlet.name
            date.text = data.created_at

            totalBayar.text = Tools.intToRupiah(data.total_product_price.toDouble().toInt().toString())

//            moneyChange.text = Tools.intToRupiah("0")

            moneyChange.text = Tools.intToRupiah(data.money_change.toDouble().toInt().toString())

            jumlahItem.text = data.quantity.toString()

            parentLayout.setOnClickListener {
                cellClickListener.selectHistory(data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.MyViewHolder {
        return MyViewHolder(parent.viewBinding(ItemHistoryBinding::inflate))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data!![position]
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    interface CellClickListener {
        fun selectHistory(data: HistoryResponse.Response)
    }
}