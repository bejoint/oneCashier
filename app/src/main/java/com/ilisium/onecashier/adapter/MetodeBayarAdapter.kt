package com.ilisium.onecashier.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ilisium.onecashier.R
import com.ilisium.onecashier.databinding.ItemTunaiBinding
import com.ilisium.onecashier.helper.viewBinding

class MetodeBayarAdapter(
    private val context: Context,
    var data: List<String>,
    val cellClickListener: CellClickListener
) :
    RecyclerView.Adapter<MetodeBayarAdapter.MyViewHolder>() {

    var posSelect = 999
    inner class MyViewHolder(binding: ItemTunaiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var metodeBayar = binding.metodeBayar
        var parentLy = binding.parentLayout
        var imgColor = binding.imgColor

        fun bind(data: String, pos: Int) {
            metodeBayar.text = data

            parentLy.setOnClickListener {
                posSelect = pos
                cellClickListener.selectMetode(data)
                notifyDataSetChanged()
            }

            if (posSelect == pos){
                imgColor.setImageResource(R.color.colorPrimary)
                metodeBayar.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                imgColor.setImageResource(R.color.white)
                metodeBayar.setTextColor(ContextCompat.getColor(context, R.color.lightGray))
            }

//            parentLy.setOnClickListener {
//                cellClickListener.selectMetode(data)
////                notifyDataSetChanged()
//            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MetodeBayarAdapter.MyViewHolder {
        return MyViewHolder(parent.viewBinding(ItemTunaiBinding::inflate))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data!![position]
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    interface CellClickListener {
        fun selectMetode(data: String)
    }
}
