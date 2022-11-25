package com.ilisium.onecashier.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ProdukResponse(
    val response: List<Response>,
    val metadata: Metadata
) {
    @Parcelize
    data class Response(
        val id: Int,
        val name: String,
        val sku: String,
        val category_product_id: Int,
        val category_product: CategoryProduct,
        val description: String,
        val stock: String,
        val stock_notif: String,
        val uom: String,
        val price: String,
        val image: String,
        val outlet_id: Int,
        val created_by_user_spv: Int
    ): Parcelable {
        @Parcelize
        data class CategoryProduct(
            val id: Int,
            val name: String
        ): Parcelable
    }

    data class Metadata(
        val message: String,
        val http_code: Int,
        val error_code: Int,
        val error_data: List<Any>
    )
}