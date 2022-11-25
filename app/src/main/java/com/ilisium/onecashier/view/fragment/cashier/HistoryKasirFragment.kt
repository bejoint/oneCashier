package com.ilisium.onecashier.view.fragment.cashier

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.ilisium.onecashier.R
import com.ilisium.onecashier.adapter.HistoryKasirAdapter
import com.ilisium.onecashier.data.viewmodel.ProfileViewModel
import com.ilisium.onecashier.data.viewmodel.ViewModelFactory
import com.ilisium.onecashier.databinding.FragmentHistoryKasirBinding
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.Status
import com.ilisium.onecashier.model.response.OpenCloseKasirResponse
import com.ilisium.onecashier.view.activity.HomeActivity
import com.ilisium.onecashier.view.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

class HistoryKasirFragment : BaseFragment(R.layout.fragment_history_kasir),
    HistoryKasirAdapter.CellClickListener {

    private lateinit var profileViewModel: ProfileViewModel

    private val binding by viewBinding(FragmentHistoryKasirBinding::bind)

    private var dateStart = ""
    private var dateEnd = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        loadHistoryKasir()
        setView()
        setListener()
    }

    private fun setView(){
        binding.toolbar.tvTitleToolbar.text = "History Kasir"
    }

    private fun setListener() {
        binding.filterDate.setOnClickListener {
            showDate()
        }

        binding.toolbar.ivBack.setOnClickListener {
            findNavController().navigate(R.id.openCashierFragment)
        }
    }

    private fun initViewModel(){
        profileViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(apiInterface)
        ).get(ProfileViewModel::class.java)
    }

    private fun loadHistoryKasir(){
        profileViewModel.historyKasir(
            dateStart,
            dateEnd
        ).observe(viewLifecycleOwner){
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        val data = resource.data?.response

                        if (data!!.isNotEmpty()){
                            binding.viewFlipper.displayedChild = 0
                            binding.rvHistory.adapter = HistoryKasirAdapter(requireContext(), data, this)
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
                        pLoading.showLoading()
                    }
                }
            }
        }
    }

    private fun showDate() {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dates: ArrayList<Long> = ArrayList() // the list to store all the dates
        val builder: MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> =
            MaterialDatePicker.Builder.dateRangePicker()
        builder.setTitleText("Select range")
        val materialDatePicker: MaterialDatePicker<androidx.core.util.Pair<Long, Long>> = builder.build()
        materialDatePicker.show(requireFragmentManager(), "Test")

        materialDatePicker.addOnPositiveButtonClickListener { selection ->
            if (selection!!.first != null && selection!!.second != null) {
                // start date
                val start: Calendar = Calendar.getInstance()
                start.timeInMillis = selection.first!!
                // end date
                val end: Calendar = Calendar.getInstance()
                end.timeInMillis = selection.second!!

                Log.d(
                    "startDate",
                    dateFormatter.format(start.getTime())
                        .toString() + " == " + dateFormatter.format(end.getTime())
                )
                dateStart = dateFormatter.format(start.getTime())
                dateEnd = dateFormatter.format(end.getTime())

                loadHistoryKasir()

                binding.filterDate.text = "$dateStart - $dateEnd"

                while (start.before(end)) {
                    start.add(Calendar.DAY_OF_MONTH, 1) // add one day
                    dates.add(start.getTimeInMillis())
                }
            }
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

    override fun selectHistory(data: OpenCloseKasirResponse.Response) {
        val bundle = bundleOf(
            "dataCashier" to data
        )
        findNavController().navigate(R.id.detailHistoryCashierFragment, bundle)
    }
}