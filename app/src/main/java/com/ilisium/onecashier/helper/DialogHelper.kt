package com.ilisium.onecashier.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import com.ilisium.onecashier.MyApplication
import com.ilisium.onecashier.R
import com.ilisium.onecashier.data.api.ApiInterface
import com.ilisium.onecashier.databinding.DialogKoneksiBinding
import com.ilisium.onecashier.databinding.DialogOpenKasirBinding

class DialogHelper(
    private val ctx: Context,
    val apiInterface: ApiInterface,
    val session: SessionManager
) {

    private val TAG = "DialogHelper"



}