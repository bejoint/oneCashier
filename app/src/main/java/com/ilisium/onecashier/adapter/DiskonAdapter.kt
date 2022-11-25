package com.ilisium.onecashier.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilisium.onecashier.databinding.ItemDiskonBinding
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.response.DiscountResponse

class DiskonAdapter(
    private val context: Context,
    var data: List<DiscountResponse.Response>,
    val cellClickListener: CellClickListener
) :
    RecyclerView.Adapter<DiskonAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemDiskonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var name = binding.name
        var code = binding.code

        var container = binding.container

        fun bind(data: DiscountResponse.Response, pos: Int) {
            name.text = data.name
            code.text = data.code

            container.setOnClickListener {
                cellClickListener.selectDiskon(data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiskonAdapter.MyViewHolder {
        return MyViewHolder(parent.viewBinding(ItemDiskonBinding::inflate))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data!![position]
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    interface CellClickListener {
        fun selectDiskon(data: DiscountResponse.Response)
    }
}