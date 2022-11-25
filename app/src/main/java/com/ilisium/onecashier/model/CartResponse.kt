package com.ilisium.onecashier.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartResponse(
    val data: List<CartProduct>,
    val kasir: Kasir
) : Parcelable