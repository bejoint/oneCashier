package com.ilisium.onecashier.view.fragment.history

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ilisium.onecashier.R
import com.ilisium.onecashier.adapter.HistoryAdapter
import com.ilisium.onecashier.data.viewmodel.ProdukViewModel
import com.ilisium.onecashier.data.viewmodel.ViewModelFactory
import com.ilisium.onecashier.databinding.FragmentHistoryBinding
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.Status
import com.ilisium.onecashier.model.response.HistoryResponse
import com.ilisium.onecashier.view.activity.HomeActivity
import com.ilisium.onecashier.view.base.BaseFragment

class HistoryFragment : BaseFragment(R.layout.fragment_history), HistoryAdapter.CellClickListener {

    private lateinit var produkViewModel: ProdukViewModel

    private val binding by viewBinding(FragmentHistoryBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        loadHistory()
        setView()
        setListener()
    }

    private fun loadHistory(){
        produkViewModel.getHistory().observe(viewLifecycleOwner){
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        val data = resource.data?.response
//                        Log.d("dataHistory", data.toString())

                        if (data!!.isNotEmpty()){
                            binding.viewFlipper.displayedChild = 0
                            binding.rvHistory.adapter = HistoryAdapter(requireContext(), data, this)
                        } else {
                            binding.viewFlipper.displayedChild = 1
                        }
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

    private fun setView(){
        binding.toolbar.tvTitleToolbar.text = "History Transaksi"
    }

    private fun setListener(){
        binding.toolbar.ivBack.setOnClickListener {
            findNavController().navigate(R.id.actionToHome)
        }
    }

    private fun initViewModel(){
        produkViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(apiInterface)
        ).get(ProdukViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        findNavController().navigate(R.id.actionToHome)
    }

    override fun selectHistory(data: HistoryResponse.Response) {
        val bundle = bundleOf("idHistory" to data.id)
        findNavController().navigate(R.id.detailHistoryFragment, bundle)
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