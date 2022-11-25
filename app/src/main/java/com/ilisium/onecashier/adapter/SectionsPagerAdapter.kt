package com.ilisium.onecashier.adapter

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ilisium.onecashier.helper.GlobalVar
import com.ilisium.onecashier.model.response.ProdukCategoryResponse
import com.ilisium.onecashier.model.response.ProdukResponse
import com.ilisium.onecashier.view.fragment.produk.TabKategoriFragment
import com.ilisium.onecashier.view.fragment.produk.TabKategoriFragment.Companion.CATEGORY_ALL_PRODUCT

class SectionsPagerAdapter(activity: AppCompatActivity, var categoryProduct: List<ProdukCategoryResponse.Response>, var listProduct: List<ProdukResponse.Response>, var search: String) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null

        Log.d("positionTab", position.toString())
        GlobalVar.posTabKategori = position

        when(position){
            0 -> fragment = TabKategoriFragment.newInstance(CATEGORY_ALL_PRODUCT,categoryProduct,  listProduct, search)
//            1 -> fragment = TabKategoriFragment.newInstance(CATEGORY_FAV_PRODUCT,categoryProduct, listProduct, search)
            else -> fragment = TabKategoriFragment.newInstance(categoryProduct[position-1].id.toInt(), categoryProduct, listProduct, search)

        }

        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return categoryProduct.size + 1
    }
}