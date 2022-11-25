package com.ilisium.onecashier.model.request

data class CheckOutRequest(
    val money_received: Int,
    var payment_type: String,
    var trx_number: String,
    var bank_name: String
)