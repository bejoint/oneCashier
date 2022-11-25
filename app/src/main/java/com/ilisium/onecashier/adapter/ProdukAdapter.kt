package com.ilisium.onecashier.adapter

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilisium.onecashier.databinding.ItemProdukDiggoBinding
import com.ilisium.onecashier.helper.Tools
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.response.ProdukResponse

class ProdukAdapter(
    private val context: Context,
    var data: List<ProdukResponse.Response>,
    val cellClickListener: CellClickListener
) :
    RecyclerView.Adapter<ProdukAdapter.MyViewHolder>(), Filterable {

    var dataFilterList: List<ProdukResponse.Response>?

    init {
        dataFilterList = data
    }

    inner class MyViewHolder(binding: ItemProdukDiggoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var nameProduk = binding.nameProduk
        var priceProduk = binding.priceProduk
        var qtyWarning = binding.qtyWarning
        var imgProduk = binding.imgProduk

        var parentLy = binding.parentLayout
        var btnAddCart = binding.btnAddCart

        fun bind(data: ProdukResponse.Response) {
            nameProduk.text = data.name

            qtyWarning.text = data.stock.toDouble().toInt().toString()
//
            priceProduk.text = Tools.intToRupiah(data.price.toDouble().toInt().toString())
//
            Glide.with(context)
                .load(data.image)
                .into(imgProduk)

            btnAddCart.setOnClickListener {
//                cellClickListener.selectProduk(data)
                cellClickListener.addToChart(data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProdukAdapter.MyViewHolder {
        return MyViewHolder(parent.viewBinding(ItemProdukDiggoBinding::inflate))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataFilterList!![position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return dataFilterList!!.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                Log.d("charSearch", charSearch)

                if (charSearch.isEmpty()) {
                    dataFilterList = data
                } else {
                    val resultList = mutableListOf<ProdukResponse.Response>()
                    for (row in data){
                        resultList.add(row)
                    }
                    dataFilterList = resultList
                }

                val filterResults = FilterResults()
                filterResults.values = dataFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataFilterList = results?.values as List<ProdukResponse.Response>
                notifyDataSetChanged()
            }

        }
    }

    interface CellClickListener {
        fun selectProduk(data: ProdukResponse.Response)
        fun addToChart(data: ProdukResponse.Response)
    }


}
