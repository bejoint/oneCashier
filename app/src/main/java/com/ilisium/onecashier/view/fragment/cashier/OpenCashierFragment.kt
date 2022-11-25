package com.ilisium.onecashier.view.fragment.cashier

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ilisium.onecashier.R
import com.ilisium.onecashier.data.viewmodel.ProdukViewModel
import com.ilisium.onecashier.data.viewmodel.ViewModelFactory
import com.ilisium.onecashier.databinding.FragmentOpenCashierBinding
import com.ilisium.onecashier.helper.GlobalVar
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.Status
import com.ilisium.onecashier.view.activity.HomeActivity
import com.ilisium.onecashier.view.base.BaseFragment

class OpenCashierFragment : BaseFragment(R.layout.fragment_open_cashier){

    private val binding by viewBinding(FragmentOpenCashierBinding::bind)

    private lateinit var produkViewModel: ProdukViewModel

    private var isOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isOpen = arguments?.getBoolean("isOpen")?: true

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
        if (!isOpen){
            binding.txtKas.text = "Kas akhir"
            binding.btnSimpan.text = "Tutup Kasir"

            binding.toolbar.tvTitleToolbar.text = "Tutup Kasir"
        } else {
            binding.toolbar.tvTitleToolbar.text = "Buka Kasir"
        }

//        toast(isOpen.toString())

        binding.toolbar.ivHistoryKasir.visibility = View.VISIBLE
    }

    private fun setListener(){
        binding.btnSimpan.setOnClickListener {
            val saldoAwal = binding.edtKasAwal.text.toString()
            if (saldoAwal.isEmpty()){
                toast("Saldo harus diisi")
                return@setOnClickListener
            }

            val type = if (isOpen){
                GlobalVar.TYPE_OPEN
            } else {
                GlobalVar.TYPE_CLOSE
            }

            produkViewModel.openCloseCashier(
                type,
                saldoAwal.toInt()
            ).observe(viewLifecycleOwner){
                it?.let { resource ->
                    when(resource.status){
                        Status.SUCCESS -> {
//                            if (session.isOpenCashier){
////                                session.isOpenCashier = false
//                            } else {
//                                session.isOpenCashier = true
//                            }

                            toast(resource.data?.metadata?.message.toString())
                            pLoading.dismissDialog()

                            findNavController().navigate(R.id.actionToHome)
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

        binding.toolbar.ivBack.setOnClickListener {
            findNavController().navigate(R.id.actionToHome)
        }

        binding.toolbar.ivHistoryKasir.setOnClickListener {
            findNavController().navigate(R.id.historyKasirFragment)
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