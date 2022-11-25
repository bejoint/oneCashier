package com.ilisium.onecashier.view.base

import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.ilisium.onecashier.R
import com.ilisium.onecashier.data.api.ApiClient
import com.ilisium.onecashier.data.api.ApiInterface
import com.ilisium.onecashier.helper.DialogHelper
import com.ilisium.onecashier.helper.Loading
import com.ilisium.onecashier.helper.SessionManager
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment(fragment: Int) : Fragment(fragment) {

    lateinit var session: SessionManager
    lateinit var apiInterface: ApiInterface
    var disposable: CompositeDisposable? = null

    private var toast: Toast? = null
    lateinit var pLoading: Loading
    lateinit var dialogHelper: DialogHelper
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pLoading = Loading(requireContext())
        disposable = CompositeDisposable()
        session = SessionManager(requireContext())
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        dialogHelper = DialogHelper(requireContext(), apiInterface, session)

        progressBar = ProgressBar(requireContext())
    }

    fun toast(@StringRes message: Int) {
        toast(getString(message))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.clear()
    }

    fun toast(toastMessage: String?) {
        if (toastMessage != null && toastMessage.isNotEmpty()) {
            if (toast != null) toast!!.cancel()
            toast = Toast.makeText(activity, toastMessage, Toast.LENGTH_LONG)
            toast!!.show()

        }
    }

    fun showProgress(status: Boolean) {
        if (status) {
            pLoading.showLoading(resources.getString(R.string.label_loading_title_dialog), false)
        } else {
            pLoading.dismissDialog()
        }
    }

    fun calculateNoOfColumns(
        context: Context,
        columnWidthDp: Float
    ): Int { // For example columnWidthdp=180
        val displayMetrics: DisplayMetrics = context.getResources().getDisplayMetrics()
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }



    val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("test006", "${it.key} = ${it.value}")
            }
        }

}