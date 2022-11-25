package com.ilisium.onecashier.view.fragment.produk

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ilisium.onecashier.R
import com.ilisium.onecashier.adapter.ProdukAdapter
import com.ilisium.onecashier.data.viewmodel.ProdukViewModel
import com.ilisium.onecashier.data.viewmodel.SearchViewModel
import com.ilisium.onecashier.data.viewmodel.ViewModelFactory
import com.ilisium.onecashier.databinding.DialogOpenKasirBinding
import com.ilisium.onecashier.databinding.FragmentTabKategoriBinding
import com.ilisium.onecashier.helper.GlobalVar
import com.ilisium.onecashier.helper.HelperInterfaces
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.response.ProdukCategoryResponse
import com.ilisium.onecashier.model.response.ProdukResponse
import com.ilisium.onecashier.view.activity.HomeActivity
import com.ilisium.onecashier.view.base.BaseFragment

class TabKategoriFragment : BaseFragment(R.layout.fragment_tab_kategori),
    ProdukAdapter.CellClickListener, HelperInterfaces.ProdukHelper {

    private lateinit var produkViewModel: ProdukViewModel

    private var listProduct: List<ProdukResponse.Response> = mutableListOf()

    private var idCategory = 0
    private var searchProduk = ""

    private val searchViewModel: SearchViewModel by viewModels()

    private val binding by viewBinding(FragmentTabKategoriBinding::bind)

    companion object {
        val listProduk = "listProduk"

        val searchProduks = "searchProduk"
        val categoryId = "categoryId"
        val categoryProduct = "categoryProduk"

        val CATEGORY_ALL_PRODUCT = -100
        val CATEGORY_FAV_PRODUCT = -200

        fun newInstance(
            categirysId: Int,
            listCategory: List<ProdukCategoryResponse.Response>,
            listProduks: List<ProdukResponse.Response>,
            search: String
        ): TabKategoriFragment {
            val args = Bundle()

            args.putParcelableArrayList(categoryProduct, listCategory as ArrayList)
            args.putParcelableArrayList(listProduk, listProduks as ArrayList)
            args.putInt(categoryId, categirysId)
            args.putString(searchProduks, search)

            val fragment = TabKategoriFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as HomeActivity).produkHelperInterface = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idCategory = arguments?.getInt(categoryId) ?: 0
//        listProduct = arguments?.getParcelableArrayList(listProduk)!!

        listProduct = GlobalVar.dataProduk!!

        searchProduk("")
        setListener()
    }

    override fun searchProduk(keyword: String) {
//        toast("keyword == " + keyword)
        if (GlobalVar.searchProduk.isNotEmpty()){
            searchProduk = GlobalVar.searchProduk
        } else {
            searchProduk = keyword
        }

//        toast(searchProduk)
        if (searchProduk.isNotEmpty()) {
            val listSearch: List<ProdukResponse.Response>
            val listSearchSku = listProduct.filter { it.sku.contains(searchProduk, true) }
            val listSearchName = listProduct.filter { it.name.contains(searchProduk, true) }
//            Log.d("listSearchSku", listSearchSku.toString())

            if (listSearchSku.isEmpty()){
                listSearch = listSearchName
            } else {
                listSearch = listSearchSku
            }
            
            val filterProduct = when (idCategory) {
                CATEGORY_ALL_PRODUCT -> listSearch
                else -> listSearch.filter { it.category_product_id == idCategory }
            }

            binding.rvProduk.adapter = ProdukAdapter(requireContext(), filterProduct, this)

        } else {
            val filterProduct = when (idCategory) {
                CATEGORY_ALL_PRODUCT -> listProduct
                else -> listProduct.filter { it.category_product_id == idCategory }
            }

            binding.rvProduk.adapter = ProdukAdapter(requireContext(), filterProduct, this)
        }
    }

    private fun initViewModel() {
        produkViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(apiInterface)
        ).get(ProdukViewModel::class.java)
    }

    private fun setListener() {

    }

    private fun showDialogOpenCashier() {
        try {

            val binding2 = DialogOpenKasirBinding.inflate(LayoutInflater.from(requireContext()))
            val bitmap: Bitmap

            val dialog = android.app.Dialog(requireContext(), R.style.DialogThemeFullScreen)
//            val dialog = android.app.Dialog(requireContext(), R.style.Theme_AppCompat_Dialog_MinWidth)

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(binding2.root)
            dialog.setCancelable(true)

            val size = Point()
            val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            display.getSize(size)

            val width: Int = size.x
            val height: Int = size.y
            var smallerDimension: Int = if (width < height) width else height
            smallerDimension = smallerDimension * 5 / 6

            binding2.btnOpenCashier.setOnClickListener {
                session.isOpenCashier = true

                val bundle = bundleOf("isOpen" to true)
                findNavController().navigate(R.id.actionToOpenCashier, bundle)
                dialog.dismiss()
            }

            val window = dialog.window
            val wlp = window!!.attributes as WindowManager.LayoutParams

            window.setBackgroundDrawableResource(android.R.color.transparent)

            wlp.gravity = Gravity.CENTER
            wlp.x = 0
            wlp.y = 0
            window.attributes = wlp
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }

    override fun selectProduk(data: ProdukResponse.Response) {
        var dataSelect = GlobalVar.selectProduk!!
        if (dataSelect.isEmpty()) {
            dataSelect.add(data.name)
        } else {
            if (!dataSelect.equals(data)) {
                dataSelect.add(data.name)
            }
        }

        findNavController().navigate(R.id.selectVarianFragment)
    }

    override fun addToChart(data: ProdukResponse.Response) {
        if (!session.isOpenCashier) {
            toast("Silahkan buka kasir terlebih dahulu!")
            showDialogOpenCashier()

        } else {
            val bundle = bundleOf(
                "idProduct" to data.id,
                "isEdit" to false
            )
            findNavController().navigate(R.id.selectVarianFragment, bundle)
        }
    }

}