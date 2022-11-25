package com.ilisium.onecashier.view.fragment.checkout

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ilisium.onecashier.R
import com.ilisium.onecashier.adapter.ItemProdukCheckOutAdapter
import com.ilisium.onecashier.data.viewmodel.ProdukViewModel
import com.ilisium.onecashier.data.viewmodel.ViewModelFactory
import com.ilisium.onecashier.databinding.FragmentSuccessCheckoutBinding
import com.ilisium.onecashier.helper.Tools
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.response.DetailHistoryResponse
import com.ilisium.onecashier.view.activity.HomeActivity
import com.ilisium.onecashier.view.base.BaseFragment
import com.ilisium.onecashier.view.widget.PrintSheetDialogFragment

class SuccessCheckoutFragment : BaseFragment(R.layout.fragment_success_checkout){

    private lateinit var produkViewModel: ProdukViewModel

    private val binding by viewBinding(FragmentSuccessCheckoutBinding::bind)

    private lateinit var detailPembayaran : DetailHistoryResponse.Response

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailPembayaran = arguments?.getParcelable<DetailHistoryResponse.Response>("detailPembayaran")!!

        initViewModel()
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
        binding.toolbar.tvTitleToolbar.text = "Pembayaran Berhasil"

        val data = detailPembayaran

        if (data != null) {
            binding.viewFlipper.displayedChild = 0
            binding.rvItem.adapter = ItemProdukCheckOutAdapter(requireContext(), data.transaction_detail)

            binding.tax.text = Tools.intToRupiah(data.total_tax_price.toDouble().toInt().toString())
            binding.diskon.text =
                Tools.intToRupiah(data.total_discount_price.toDouble().toInt().toString())

            binding.totalPrice.text =
                Tools.intToRupiah(data.total_product_price.toDouble().toInt().toString())

            binding.moneyChange.text =
                Tools.intToRupiah(data.money_change.toDouble().toInt().toString())

//                            binding.metodeBayar.text = data.payment_type.toUpperCase()

//                            when(data.payment_type){
//                                "cash" -> {
//                                    if (data.money_received.isNotEmpty()){
//                                        binding.edtJumlahBayar.setText(Tools.intToRupiah(data.money_received.toDouble().toInt().toString()))
//                                    }
//
//                                    binding.lyTunai.visibility = View.VISIBLE
//                                }
//                                "edc" -> {
//                                    if (data.trx_number.isNotEmpty()){
//                                        binding.edtBank.setText(data.bank_name)
//                                        binding.edtNoEdc.setText(data.trx_number)
//                                    }
//
//                                    binding.lyEdc.visibility = View.VISIBLE
//                                }
//                                else -> {
//                                    binding.metodeBayar.text = "Metode pembayaran belum dipilih"
//                                }
//                            }

        }
    }

    private fun setListener(){
        binding.toolbar.ivBack.setOnClickListener {
            findNavController().navigate(R.id.actionToHome)
        }

        binding.btnCetak.setOnClickListener {


            val printSheetDialogFragment = PrintSheetDialogFragment()
            val bundle = bundleOf(
                PrintSheetDialogFragment.KEY_CART_REPONSE to detailPembayaran
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