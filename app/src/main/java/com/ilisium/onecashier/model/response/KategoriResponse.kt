package com.ilisium.onecashier.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KategoriResponse(
    val status: Boolean,
    val message: String,
    val `data`: List<Data>
):Parcelable {
    @Parcelize
    data class Data(
        val id: String,
        val owner_id: String,
        val outlet_id: String,
        val departement_id: String,
        val name: String,
        val order_number: String,
        val is_active: String,
//        val created_at: String,
//        val updated_at: String,
//        val deleted_at: Any,
//        val created_by: String,
//        val updated_by: String,
//        val deleted_by: Any
    ):Parcelable
}