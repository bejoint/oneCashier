package com.ilisium.onecashier.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilisium.onecashier.databinding.ItemTaxBinding
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.response.TaxResponse

class TaxAdapter(
    private val context: Context,
    var data: List<TaxResponse.Response>,
    val cellClickListener: CellClickListener
) :
    RecyclerView.Adapter<TaxAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemTaxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var name = binding.name
        var percentage = binding.percentage

        var container = binding.container

        fun bind(data: TaxResponse.Response, pos: Int) {
            name.text = data.name
            percentage.text = data.percentage

            container.setOnClickListener {
                cellClickListener.selectTax(data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaxAdapter.MyViewHolder {
        return MyViewHolder(parent.viewBinding(ItemTaxBinding::inflate))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data!![position]
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    interface CellClickListener {
        fun selectTax(data: TaxResponse.Response)
    }
}