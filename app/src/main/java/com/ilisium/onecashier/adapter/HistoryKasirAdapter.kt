package com.ilisium.onecashier.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilisium.onecashier.databinding.ItemHistoryKasirBinding
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.response.OpenCloseKasirResponse

class HistoryKasirAdapter(
    private val context: Context,
    var data: List<OpenCloseKasirResponse.Response>,
    val cellClickListener: CellClickListener
) :
    RecyclerView.Adapter<HistoryKasirAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemHistoryKasirBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var dateOpen = binding.dateOpen
        var dateClose = binding.dateClose

        var parentLayout = binding.parentLayout

        fun bind(data: OpenCloseKasirResponse.Response, pos: Int) {
            dateOpen.text = data.open_datetime
            dateClose.text = data.close_datetime

            parentLayout.setOnClickListener {
                cellClickListener.selectHistory(data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryKasirAdapter.MyViewHolder {
        return MyViewHolder(parent.viewBinding(ItemHistoryKasirBinding::inflate))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data!![position]
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    interface CellClickListener {
        fun selectHistory(data: OpenCloseKasirResponse.Response)
    }
}