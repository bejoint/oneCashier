package com.ilisium.onecashier.view.fragment.cashier

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ilisium.onecashier.R
import com.ilisium.onecashier.databinding.FragmentDetailHistoryCashierBinding
import com.ilisium.onecashier.helper.Tools
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.response.OpenCloseKasirResponse
import com.ilisium.onecashier.view.activity.HomeActivity
import com.ilisium.onecashier.view.base.BaseFragment

class DetailHistoryCashierFragment : BaseFragment(R.layout.fragment_detail_history_cashier){

    private val binding by viewBinding(FragmentDetailHistoryCashierBinding::bind)

    private var dataCashier: OpenCloseKasirResponse.Response?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataCashier = arguments?.getParcelable("dataCashier")

        setView()
        setListener()
    }

    private fun setView(){
        binding.toolbar.tvTitleToolbar.text = "Detail Kasir"

        if (dataCashier != null){
            binding.dateOpen.text = dataCashier!!.open_datetime
            binding.dateClose.text = dataCashier!!.close_datetime

            binding.totalOpen.text = Tools.intToRupiah(dataCashier!!.open_balance.toDouble().toInt().toString())
            binding.totalClose.text = Tools.intToRupiah(dataCashier!!.close_balance.toDouble().toInt().toString())
            binding.totalExpeceted.text = Tools.intToRupiah(dataCashier!!.total_expected.toDouble().toInt().toString())
            binding.totalActual.text = Tools.intToRupiah(dataCashier!!.total_actual.toDouble().toInt().toString())

        }
    }

    private fun setListener(){
        binding.toolbar.ivBack.setOnClickListener {
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