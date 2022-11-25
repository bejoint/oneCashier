package com.ilisium.onecashier.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ilisium.onecashier.model.response.ProdukResponse
import com.ilisium.onecashier.view.fragment.produk.TabKategoriFragment

class TabKategoriAdapter(fm: FragmentManager?, var mNumOfTabs: Int, var data: List<ProdukResponse.Response>, var searchProduk: String) : FragmentStatePagerAdapter(fm!!) {

    override fun getCount(): Int {
        return mNumOfTabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
//            0 -> {
//                TabKategoriFragment.newInstance("1", "")
//            }
//            1 -> {
//                TabKategoriFragment.newInstance("2", searchProduk)
//            }
//            2 -> {
//                TabKategoriFragment.newInstance("3", searchProduk)
//            }

            else -> TabKategoriFragment()

        }
    }
}
