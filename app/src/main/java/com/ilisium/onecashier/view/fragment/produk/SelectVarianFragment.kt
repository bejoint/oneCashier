package com.ilisium.onecashier.view.fragment.produk

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ilisium.onecashier.R
import com.ilisium.onecashier.adapter.DiskonAdapter
import com.ilisium.onecashier.adapter.TaxAdapter
import com.ilisium.onecashier.adapter.VarianAdapter
import com.ilisium.onecashier.data.viewmodel.ProdukViewModel
import com.ilisium.onecashier.data.viewmodel.ViewModelFactory
import com.ilisium.onecashier.databinding.FragmentSelectVarianBinding
import com.ilisium.onecashier.helper.GlobalVar
import com.ilisium.onecashier.helper.Tools
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.DataDiskon
import com.ilisium.onecashier.model.DataTax
import com.ilisium.onecashier.model.Status
import com.ilisium.onecashier.model.response.DiscountResponse
import com.ilisium.onecashier.model.response.ProductDetailResponse
import com.ilisium.onecashier.model.response.TaxResponse
import com.ilisium.onecashier.view.activity.HomeActivity
import com.ilisium.onecashier.view.base.BaseFragment
import kotlinx.android.synthetic.main.dialog_diskon.view.*
import kotlinx.android.synthetic.main.dialog_tax.view.*
import kotlinx.android.synthetic.main.dialog_tax.view.btnClose

class SelectVarianFragment : BaseFragment(R.layout.fragment_select_varian),
    VarianAdapter.CellClickListener, TaxAdapter.CellClickListener, DiskonAdapter.CellClickListener {

    private val binding by viewBinding(FragmentSelectVarianBinding::bind)

    private lateinit var produkViewModel: ProdukViewModel
    private var idProduct = 0

    private var dataProduk : ProductDetailResponse.Response?= null

    var jumlahBeli = 1
    var varianIdSelect : MutableList<Int> = mutableListOf()

    var isEdit = false

    private var taxAdapter : TaxAdapter?= null
    private var diskonAdapter : DiskonAdapter?= null

    private var taxId = 0
    private var diskonId = 0

    private var dialogTax: Dialog?= null
    private var dialogDiskon: Dialog?= null

    private var totalPrice = 0
    private var priceProduk = 0

    var listTaxSelect : MutableList<DataTax>?= mutableListOf()
    var listDiskonSelect : MutableList<DataDiskon>?= mutableListOf()

    var valueTax = 0
    var valueDiskon = 0
    var valueVarian = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idProduct = arguments?.getInt("idProduct")?: 0
        isEdit = arguments?.getBoolean("isEdit")?: false

        initViewModel()
        loadProduk()
        setView()
        setListener()
    }

    private fun initViewModel(){
        produkViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(apiInterface)
        ).get(ProdukViewModel::class.java)
    }

    private fun setTotalPrice(){
        binding.totalPrice.text = Tools.intToRupiah(totalPrice.toString())
    }

    private fun loadProduk(){
        produkViewModel.detailProduk(idProduct.toString()).observe(viewLifecycleOwner){
            it.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        dataProduk = resource.data?.response

                        binding.nameProduk.text = dataProduk!!.name
                        binding.priceProduk.text = Tools.intToRupiah(dataProduk!!.price.toDouble().toInt().toString())

                        priceProduk = dataProduk!!.price.toDouble().toInt()
                        totalPrice = dataProduk!!.price.toDouble().toInt()

                        setTotalPrice()
                        binding.rvVarian.adapter = VarianAdapter(requireContext(), dataProduk!!.product_variants, this)

//                        toast(resource.data?.metadata?.message.toString())
                        pLoading.dismissDialog()
                    }
                    Status.ERROR -> {
                        toast(resource.data?.metadata?.message.toString())
                        pLoading.dismissDialog()
                    }
                    Status.LOADING -> {
                        pLoading.showLoading()
                    }
                }
            }
        }
    }

    private fun setView(){
        binding.toolbar.tvTitleToolbar.text = "Pesanan"

        if (isEdit){
            binding.btnAddPesanan.text = "Update"
        }
    }

    private fun setListener(){
        binding.toolbar.ivBack.setOnClickListener {
//            findNavController().navigate(R.id.actionToHome)
//            activity?.onBackPressed()

            if (isEdit){
                findNavController().navigate(R.id.checkoutFragment)
            } else {
                findNavController().navigate(R.id.actionToHome)
            }
        }

        binding.clPlus.setOnClickListener {
            jumlahBeli += 1
            totalPrice += ((priceProduk + valueTax - valueDiskon + valueVarian))
            setTotalPrice()

            binding.txtTotal.text = jumlahBeli.toString()
        }

        binding.clMin.setOnClickListener {
            if (jumlahBeli <= 0){
                toast("Jumlah tidak boleh kurang dari 0")
            } else {
                jumlahBeli -= 1
                totalPrice -= (priceProduk + valueVarian + valueTax - valueDiskon)
                setTotalPrice()
            }
            binding.txtTotal.text = jumlahBeli.toString()
        }

        binding.btnAddPesanan.setOnClickListener {
            val listVarian = varianIdSelect

//            if (taxId == 0){
//                toast("Tax harus dipilih")
//                return@setOnClickListener
//            }
//            if (diskonId == 0){
//                toast("Diskon harus dipilih")
//                return@setOnClickListener
//            }

            if (isEdit){
                produkViewModel.updateProduk(
                    idProduct,
                    listVarian,
                    jumlahBeli,
                    "",
                    taxId,
                    diskonId,
                    "put"
                ).observe(viewLifecycleOwner){
                    it?.let {resource ->
                        when(resource.status){
                            Status.SUCCESS -> {
                                toast(resource.data?.metadata?.message.toString())
                                pLoading.dismissDialog()

                                findNavController().navigate(R.id.actionToHome)
                            }
                            Status.ERROR -> {
                                toast(resource.data?.metadata?.message.toString())
                                pLoading.dismissDialog()
                            }
                            Status.LOADING -> {
//                            toast(resource.data?.metadata?.message.toString())
                                pLoading.showLoading()
                            }
                        }
                    }
                }
            } else {
                produkViewModel.addPesanan(
                    idProduct,
                    listVarian,
                    jumlahBeli,
                    "",
                    taxId,
                    diskonId
                ).observe(viewLifecycleOwner){
                    it?.let {resource ->
                        when(resource.status){
                            Status.SUCCESS -> {
                                GlobalVar.listTaxSelect = listTaxSelect
                                GlobalVar.listDiskonSelect = listDiskonSelect

                                toast(resource.data?.metadata?.message.toString())
                                pLoading.dismissDialog()

                                findNavController().navigate(R.id.actionToHome)
                            }
                            Status.ERROR -> {
                                toast(resource.data?.metadata?.message.toString())
                                pLoading.dismissDialog()
                            }
                            Status.LOADING -> {
//                            toast(resource.data?.metadata?.message.toString())
                                pLoading.showLoading()
                            }
                        }
                    }
                }
            }

        }

        binding.btnAddTax.setOnClickListener {
            loadTax()
        }

        binding.btnAddDiskon.setOnClickListener {
            loadDiskon()
        }
    }

    private fun showDialogTax(){
        try {
            val binding2 = View.inflate(requireContext(), R.layout.dialog_tax, null)

            dialogTax = android.app.Dialog(requireContext(), R.style.Theme_AppCompat_Dialog_MinWidth)
            dialogTax!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogTax!!.setContentView(binding2)
            dialogTax!!.setCancelable(false)

            val size = Point()
            val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            display.getSize(size)

            val width: Int = size.x
            val height: Int = size.y
            var smallerDimension: Int = if (width < height) width else height
            smallerDimension = smallerDimension * 5 / 6

            binding2.rvTax.adapter = taxAdapter
            binding2.btnClose.setOnClickListener {
                dialogTax!!.dismiss()
            }

//            binding2.pin_view.visibility = View.GONE

            val window = dialogTax!!.window
            val wlp = window!!.attributes as WindowManager.LayoutParams

            window.setBackgroundDrawableResource(android.R.color.transparent)

            wlp.gravity = Gravity.CENTER
            wlp.x = 0
            wlp.y = 0
            window.attributes = wlp
            dialogTax!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showDialogDiskon(){
        try {
            val binding2 = View.inflate(requireContext(), R.layout.dialog_diskon, null)

            dialogDiskon = Dialog(requireContext(), R.style.Theme_AppCompat_Dialog_MinWidth)
            dialogDiskon!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogDiskon!!.setContentView(binding2)
            dialogDiskon!!.setCancelable(false)

            val size = Point()
            val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            display.getSize(size)

            val width: Int = size.x
            val height: Int = size.y
            var smallerDimension: Int = if (width < height) width else height
            smallerDimension = smallerDimension * 5 / 6

            binding2.rvDiskon.adapter = diskonAdapter
            binding2.btnClose.setOnClickListener {
                dialogDiskon!!.dismiss()
            }

//            binding2.pin_view.visibility = View.GONE

            val window = dialogDiskon!!.window
            val wlp = window!!.attributes as WindowManager.LayoutParams

            window.setBackgroundDrawableResource(android.R.color.transparent)

            wlp.gravity = Gravity.CENTER
            wlp.x = 0
            wlp.y = 0
            window.attributes = wlp
            dialogDiskon!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadTax(){
        produkViewModel.getTax().observe(viewLifecycleOwner){
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        val data = resource.data?.response
                        taxAdapter = TaxAdapter(requireContext(), data!!, this)
//                        toast(resource.data?.metadata?.message.toString())
                        pLoading.dismissDialog()

                        showDialogTax()
                    }
                    Status.ERROR -> {
                        toast(resource.data?.metadata?.message.toString())
                        pLoading.dismissDialog()
                    }
                    Status.LOADING -> {
                        pLoading.showLoading()
                    }
                }
            }
        }
    }

    private fun loadDiskon(){
        produkViewModel.getDiscount().observe(viewLifecycleOwner){
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        val data = resource.data?.response
//                        Log.d("dataDiskon", data.toString())
                        diskonAdapter = DiskonAdapter(requireContext(), data!!, this)
                        pLoading.dismissDialog()

                        showDialogDiskon()
                    }
                    Status.ERROR -> {
                        toast(resource.data?.metadata?.message.toString())
                        pLoading.dismissDialog()
                    }
                    Status.LOADING -> {
                        pLoading.showLoading()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as HomeActivity).supportActionBar!!.show()
    }

    override fun selectVarian(data: ProductDetailResponse.Response.ProductVariant,checked: Boolean){
        valueVarian = data.price.toDouble().toInt()

        if (checked){
            if (varianIdSelect.equals(data.id)){

            } else {
                totalPrice += valueVarian
                varianIdSelect.add(data.id)
            }
        } else {
            if (varianIdSelect.equals(data.id)){
                totalPrice -= valueVarian
                valueVarian = 0
                varianIdSelect.remove(data.id)
            } else {
                totalPrice -= valueVarian
                valueVarian = 0
            }
        }

        setTotalPrice()
//        toast(checked.toString())
    }

    override fun selectTax(data: TaxResponse.Response) {
        taxId = data.id
        binding.txtTax.text = data.name

        if (data.percentage.toDouble().toInt() != 0){

            if (GlobalVar.listTaxSelect!!.contains(DataTax(idProduct, data.percentage.toDouble().toInt()))){

            } else {
                valueTax = ((priceProduk + valueVarian) / data.percentage.toDouble().toInt())
                totalPrice += valueTax

                listTaxSelect!!.add(
                    DataTax(
                        idProduct,
                        data.percentage.toDouble().toInt()
                    )
                )
            }
        }

        setTotalPrice()
        dialogTax!!.dismiss()
    }

    override fun selectDiskon(data: DiscountResponse.Response) {
        diskonId = data.id
//        Log.d("diskonId", diskonId.toString())
        binding.txtDiskon.text = data.name

        if (data.value != 0){
            if (GlobalVar.listDiskonSelect!!.contains(DataDiskon(idProduct, data.value.toDouble().toInt()))){

            } else {
                valueDiskon = ((priceProduk + valueVarian) / data.value.toDouble().toInt())
                totalPrice -= valueDiskon

                listDiskonSelect!!.add(
                    DataDiskon(
                        idProduct,
                        data.value.toDouble().toInt()
                    )
                )
            }

        }

        setTotalPrice()
        dialogDiskon!!.dismiss()
    }

}