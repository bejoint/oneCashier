package com.ilisium.onecashier.view.fragment.history

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ilisium.onecashier.R
import com.ilisium.onecashier.adapter.ItemHistoryAdapter
import com.ilisium.onecashier.data.viewmodel.ProdukViewModel
import com.ilisium.onecashier.data.viewmodel.ViewModelFactory
import com.ilisium.onecashier.databinding.FragmentDetailHistoryBinding
import com.ilisium.onecashier.helper.Tools
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.Status
import com.ilisium.onecashier.model.response.DetailHistoryResponse
import com.ilisium.onecashier.view.activity.HomeActivity
import com.ilisium.onecashier.view.base.BaseFragment
import com.ilisium.onecashier.view.widget.PrintSheetDialogFragment

class DetailHistoryFragment : BaseFragment(R.layout.fragment_detail_history){

    private lateinit var produkViewModel: ProdukViewModel

    private val binding by viewBinding(FragmentDetailHistoryBinding::bind)
private  lateinit var detailHistoryResponse: DetailHistoryResponse
    private var idHistory = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idHistory = arguments?.getInt("idHistory")?: 0

        initViewModel()
        loadProduk()
        setView()
        setListener()
    }

    private fun loadProduk(){
        produkViewModel.showHistory(idHistory.toString()).observe(viewLifecycleOwner){
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        detailHistoryResponse = resource.data!!
                        val data = resource.data.response

                        if (data!!.transaction_detail.isNotEmpty()){
                            binding.viewFlipper.displayedChild = 0
                            binding.rvItem.adapter = ItemHistoryAdapter(requireContext(), data.transaction_detail)

                            binding.tax.text = Tools.intToRupiah(data.total_tax_price.toDouble().toInt().toString())
                            binding.diskon.text = Tools.intToRupiah(data.total_discount_price.toDouble().toInt().toString())

                            binding.totalPrice.text = Tools.intToRupiah(data.total_product_price.toDouble().toInt().toString())

                            binding.moneyChange.text = Tools.intToRupiah(data.money_change.toDouble().toInt().toString())

                            binding.metodeBayar.text = data.payment_type.toUpperCase()

                            when(data.payment_type){
                                "cash" -> {
                                    if (data.money_received.isNotEmpty()){
                                        binding.edtJumlahBayar.setText(Tools.intToRupiah(data.money_received.toDouble().toInt().toString()))
                                    }

                                    binding.lyTunai.visibility = View.VISIBLE
                                }
                                "edc" -> {
                                    if (data.trx_number.isNotEmpty()){
                                        binding.edtBank.setText(data.bank_name)
                                        binding.edtNoEdc.setText(data.trx_number)
                                    }

                                    binding.lyEdc.visibility = View.VISIBLE
                                }
                                else -> {
                                    binding.metodeBayar.text = "Metode pembayaran belum dipilih"
                                }
                            }

                        } else {
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
//                        pLoading.showLoading()
                    }
                }
            }
        }
    }

    private fun initViewModel(){
        produkViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(apiInterface)
        ).get(ProdukViewModel::class.java)
    }

    private fun setView(){
        binding.toolbar.tvTitleToolbar.text = "Detail Pesanan"
    }

    private fun setListener(){
        binding.toolbar.ivBack.setOnClickListener {
            findNavController().navigate(R.id.historyFragment)
        }

        binding.btnCetak.setOnClickListener {

            val printSheetDialogFragment = PrintSheetDialogFragment()
            val bundle = bundleOf(
                PrintSheetDialogFragment.KEY_CART_REPONSE to detailHistoryResponse.response
            )
            printSheetDialogFragment.arguments = bundle

            printSheetDialogFragment.show(childFragmentManager, PrintSheetDialogFragment.TAG)
        }

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
}