package com.ilisium.onecashier.view.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ilisium.onecashier.R
import com.ilisium.onecashier.adapter.SectionsPagerAdapter
import com.ilisium.onecashier.data.viewmodel.ProdukViewModel
import com.ilisium.onecashier.data.viewmodel.ProfileViewModel
import com.ilisium.onecashier.data.viewmodel.SearchViewModel
import com.ilisium.onecashier.data.viewmodel.ViewModelFactory
import com.ilisium.onecashier.databinding.DialogOpenKasirBinding
import com.ilisium.onecashier.databinding.FragmentHomeBinding
import com.ilisium.onecashier.helper.GlobalVar
import com.ilisium.onecashier.helper.Tools
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.Status
import com.ilisium.onecashier.model.response.ProdukCategoryResponse
import com.ilisium.onecashier.view.activity.HomeActivity
import com.ilisium.onecashier.view.base.BaseFragment

class HomeFragment : BaseFragment(R.layout.fragment_home){

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var produkViewModel: ProdukViewModel
    private lateinit var profileViewModel: ProfileViewModel

    private var searchProduk = ""
    private var isSearch = false

    private val searchViewModel: SearchViewModel by viewModels()

    private var listCategoryProduk: List<ProdukCategoryResponse.Response>?= mutableListOf()

    private var totalPrice = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        getMeProfile()
        listProduct()
        indexCart()
        setListener()
    }

    private fun indexCart(){
        produkViewModel.indexCart().observe(viewLifecycleOwner){
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        val data = resource.data?.response!!
                        totalPrice = 0

                        if (data.isNotEmpty()){
                            binding.cardCart.visibility = View.VISIBLE

                            binding.countProduckSelect.text = data.size.toString() + " Item"

                            for (i in data){
                                totalPrice += (i.sub_total_price_final.toDouble().toInt())
                            }
                            binding.totalPrice.text = Tools.intToRupiah(totalPrice.toString())
                        }

//                        toast(resource.data?.metadata?.message.toString())
//                        pLoading.dismissDialog()
                    }
                    Status.ERROR -> {
                        toast(resource.data?.metadata?.message.toString())
//                        pLoading.dismissDialog()
                    }
                    Status.LOADING -> {
//                        toast(resource.data?.metadata?.message.toString())
//                        pLoading.showLoading()
                    }
                }
            }
        }
    }

    private fun listProduct(){
        produkViewModel.getProduk(0).observe(viewLifecycleOwner){
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        GlobalVar.dataProduk = resource.data?.response
//                        setTab("")

                        categrotyProduct()

//                        toast(resource.data?.metadata?.message.toString())
                        pLoading.dismissDialog()
                    }
                    Status.ERROR -> {
                        toast(resource.data?.metadata?.message.toString())
                        pLoading.dismissDialog()
                    }
                    Status.LOADING -> {
//                        toast(resource.data?.metadata?.message.toString())
                        pLoading.showLoading()
                    }
                }
            }
        }
    }

    private fun categrotyProduct(){
        produkViewModel.getCategoryProduk().observe(viewLifecycleOwner){
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        val data = resource.data!!.response
                        listCategoryProduk = data

//                        toast(resource.data?.metadata?.message.toString())
//                        pLoading.dismissDialog()

                        setTab("")
                    }
                    Status.ERROR -> {
                        toast(resource.data?.metadata?.message.toString())
//                        pLoading.dismissDialog()
                    }
                    Status.LOADING -> {
//                        toast(resource.data?.metadata?.message.toString())
//                        pLoading.showLoading()
                    }
                }
            }
        }
    }

    private fun setTab(search: String){
        val sectionsPagerAdapter = SectionsPagerAdapter((activity as HomeActivity), listCategoryProduk!! ,GlobalVar.dataProduk!!, search)

        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position){
                0 -> tab.text = "ALL"
//                1 -> tab.text = "Donat"
                else -> tab.text = listCategoryProduk?.get(position - 1)!!.name
            }

        }.attach()

//        when(GlobalVar.posTabKategori){
//            0 -> {
//                setTab(0)
//            } else -> {
//                setTab(GlobalVar.posTabKategori)
//            }
//        }
    }

    fun setTab(index: Int){
        val tab: TabLayout.Tab = binding.tabLayout.getTabAt(index)!!
        tab.select()

        Log.d("indexTab", index.toString())
        binding.viewPager.setCurrentItem(index, false)
    }


    private fun initViewModel(){
        produkViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(apiInterface)
        ).get(ProdukViewModel::class.java)

        profileViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(apiInterface)
        ).get(ProfileViewModel::class.java)
    }

    private fun getMeProfile() {
        profileViewModel.getUserMe().observe(viewLifecycleOwner){
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        val data = resource.data!!.response

//                        toast(resource.data?.metadata?.message.toString())
                    }
                    Status.ERROR -> {
                        toast(resource.data?.metadata?.message.toString())
//                        pLoading.dismissDialog()
                    }
                    Status.LOADING -> {
//                        toast(resource.data?.metadata?.message.toString())
//                        pLoading.showLoading()
                    }
                }
            }
        }
    }

    private fun showDialogOpenCashier(){
        try {

            val binding2 = DialogOpenKasirBinding.inflate(LayoutInflater.from(requireContext()))
            val bitmap: Bitmap

            val dialog = android.app.Dialog(requireContext(), R.style.DialogThemeFullScreen)
//            val dialog = android.app.Dialog(requireContext(), R.style.Theme_AppCompat_Dialog_MinWidth)

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(binding2.root)
            dialog.setCancelable(false)

            val size = Point()
            val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            display.getSize(size)

            val width: Int = size.x
            val height: Int = size.y
            var smallerDimension: Int = if (width < height) width else height
            smallerDimension = smallerDimension * 5 / 6

            binding2.btnOpenCashier.setOnClickListener {
                findNavController().navigate(R.id.actionToOpenCashier)
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

    private fun setListener() {
        binding.searchProduk.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                isSearch = true

                GlobalVar.searchProduk = s!!
                searchViewModel.changeSearch()

                setTab("")

//                GlobalVar.dataProduk!!.filter { it.name == s }

                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                if (s!!.isEmpty()){
                    isSearch = false

                    GlobalVar.searchProduk = ""
                    searchViewModel.changeSearch()

                    setTab("")
                }
//                searchProduk = s.toString()
//                setTab()
                return false
            }
        })

        binding.cardCart.setOnClickListener {
            findNavController().navigate(R.id.checkoutFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).supportActionBar!!.show()
    }
}