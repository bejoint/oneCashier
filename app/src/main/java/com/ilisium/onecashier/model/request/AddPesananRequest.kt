package com.ilisium.onecashier.model.request

data class AddPesananRequest(
    val product_id: Int,
    val variant_id: List<Int>,
    val quantity: Int,
    val note: String,
    val tax_id: Int,
    val discount_id: Int
)