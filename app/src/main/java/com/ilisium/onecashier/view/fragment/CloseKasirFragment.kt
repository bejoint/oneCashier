package com.ilisium.onecashier.view.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ilisium.onecashier.R
import com.ilisium.onecashier.data.viewmodel.ProdukViewModel
import com.ilisium.onecashier.databinding.FragmentCloseKasirBinding
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.view.base.BaseFragment

class CloseKasirFragment : BaseFragment(R.layout.fragment_close_kasir){

    private val binding by viewBinding(FragmentCloseKasirBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        initViewModel()
//        loadProduk()
//        setView()
//        setListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        findNavController().navigate(R.id.actionToHome)
    }
}