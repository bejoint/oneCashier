package com.ilisium.onecashier.view.fragment.checkout

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ilisium.onecashier.R
import com.ilisium.onecashier.adapter.CheckoutAdapter
import com.ilisium.onecashier.adapter.MetodeBayarAdapter
import com.ilisium.onecashier.data.viewmodel.ProdukViewModel
import com.ilisium.onecashier.data.viewmodel.ViewModelFactory
import com.ilisium.onecashier.databinding.FragmentCheckoutBinding
import com.ilisium.onecashier.helper.Tools
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.Status
import com.ilisium.onecashier.model.response.IndexCartResponse
import com.ilisium.onecashier.view.activity.HomeActivity
import com.ilisium.onecashier.view.base.BaseFragment

class CheckoutFragment : BaseFragment(R.layout.fragment_checkout),
    CheckoutAdapter.CellClickListener, MetodeBayarAdapter.CellClickListener {

    private val binding by viewBinding(FragmentCheckoutBinding::bind)
    private lateinit var produkViewModel: ProdukViewModel

    private var totalPrice = 0
    private var totalPajak = 0
    private var totalDiskon = 0

    private var taxId = 0
    private var diskonId = 0
    private var noEdc = ""
    private var bankName = ""

    companion object {
        val METODE_TUNAI = "Tunai"
        val METODE_EDC = "EDC"

        var METODE_BAYAR = ""
    }

    private var totalBayar = "0"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        indexCart()
        setView()
        setListener()
    }

    private fun initViewModel(){
        produkViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(apiInterface)
        ).get(ProdukViewModel::class.java)
    }

    private fun setView(){
        binding.toolbar.tvTitleToolbar.text = "Keranjang"
//        binding.toolbar.ivDelete.visibility = View.GONE

        binding.rvMetode.adapter = MetodeBayarAdapter(requireContext(), dataMetodePembayaran(), this)
    }

    private fun indexCart(){
        produkViewModel.indexCart().observe(viewLifecycleOwner){
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        val data = resource.data?.response!!
                        totalPrice = 0
                        totalPajak = 0
                        totalDiskon = 0

                        if (data.isNotEmpty()){
                            binding.viewFlipper.displayedChild = 0
//                            binding.countProduckSelect.text = data.size.toString() + " Item"
                            binding.rvCheckout.adapter = CheckoutAdapter(requireContext(), data, this)

                            for (i in data){
                                totalPrice += (i.sub_total_price_final.toDouble().toInt())
                                totalPajak += (i.tax_price.toDouble().toInt())
                                totalDiskon += (i.discount_price.toDouble().toInt())
                            }

                            //total Tax
                            binding.totalTax.text = Tools.intToRupiah(totalPajak.toString())

                            //total Diskon
                            binding.totalDiskon.text = Tools.intToRupiah(totalDiskon.toString())

                            binding.totalPrice.text = Tools.intToRupiah(totalPrice.toString())
                        } else {
                            binding.btnCetak.visibility = View.GONE
                            binding.viewFlipper.displayedChild = 1
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

    private fun setListener(){
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.actionToHome)
        }

        binding.toolbar.ivBack.setOnClickListener {
            findNavController().navigate(R.id.actionToHome)
        }

        binding.btnCetak.setOnClickListener {
            if (METODE_BAYAR.isEmpty()){
                toast("Silahkan pilih dahulu metode pembayarannya!")
            } else {
                when(METODE_BAYAR){
                    METODE_TUNAI -> {
                        totalBayar = binding.edtJumlahBayar.text.toString()
                        if (totalBayar.isEmpty()){
                            toast("Jumlah bayar harus diisi!")
                        } else {
                            checkOut()
                        }
                    }
                    METODE_EDC -> {
                        noEdc = binding.edtNoEdc.text.toString()
                        if (noEdc.isEmpty()){
                            toast("No Transaksi EDC harus diisi")
                        } else {
                            checkOut()
                        }
                    }
                }
            }

        }
    }

    private fun checkOut(){
        val type = if (METODE_BAYAR == METODE_TUNAI){
            "cash"
        } else {
            "edc"
        }

        if (METODE_BAYAR == METODE_EDC){
            totalBayar = "0"
            bankName = binding.spEdcBank.selectedItem.toString()

        } else {
            bankName = ""
            noEdc = ""
        }

        produkViewModel.checkOut(
            totalBayar.toInt(),
            type,
            noEdc,
            bankName
        ).observe(viewLifecycleOwner){
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        pLoading.dismissDialog()

                      resource.data?.response?.let {
                          val bundle = bundleOf(
                              "detailPembayaran" to resource.data!!.response
                          )
                          findNavController().navigate(R.id.successCheckoutFragment, bundle)
                      } ?: toast(resource.data?.metadata?.message!!)


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

    override fun selectVarian(data: IndexCartResponse.Response) {

    }

    override fun editCart(data: IndexCartResponse.Response) {
        val bundle = bundleOf(
            "idProduct" to data.product.id,
            "isEdit" to true
        )
        findNavController().navigate(R.id.selectVarianFragment, bundle)
    }

    override fun deleteItem(data: IndexCartResponse.Response) {
        val builder1: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder1.setMessage("Yakin ingin hapus item dari keranjang?")
        builder1.setCancelable(true)
        builder1.setPositiveButton(
            "Ya"
        ) { dialog, id ->
            produkViewModel.deleteItemCart(
                data.id
            ).observe(viewLifecycleOwner){
                it?.let { resource ->
                    when(resource.status){
                        Status.SUCCESS -> {
                            toast(resource.data?.metadata?.message.toString())
                            pLoading.dismissDialog()

                            indexCart()
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
        builder1.setNegativeButton(
            "Tidak"
        ) { dialog, id -> dialog.cancel() }
        val alert11: AlertDialog = builder1.create()
        alert11.show()
    }

    override fun updateQty(qty: Int, data: IndexCartResponse.Response) {
        produkViewModel.updateQtyCart(data.id.toString(), qty).observe(viewLifecycleOwner){
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        toast(resource.data?.metadata?.message.toString())
                        pLoading.dismissDialog()

                        indexCart()
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

    private fun dataMetodePembayaran(): List<String>{
        val data = mutableListOf<String>()
        data.add(METODE_TUNAI)
        data.add(METODE_EDC)
        return data
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).supportActionBar!!.hide()
        (activity as HomeActivity).hiddenBottombar()
    }

    override fun onStop() {
        super.onStop()
        (activity as HomeActivity).supportActionBar!!.show()
    }

    override fun selectMetode(data: String) {
        METODE_BAYAR = data

        when(data){
            METODE_TUNAI -> {
                binding.lyTunai.visibility = View.VISIBLE
                binding.lyEdc.visibility = View.GONE
            }

            METODE_EDC -> {
                binding.lyTunai.visibility = View.GONE
                binding.lyEdc.visibility = View.VISIBLE
            }
        }
    }
}