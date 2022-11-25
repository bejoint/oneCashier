package com.ilisium.onecashier.data.api

import com.ilisium.onecashier.model.request.*
import com.ilisium.onecashier.model.response.UpdateQtyRequest

class ApiRepository (private val apiInterface: ApiInterface) {

    suspend fun getEmployees(outlet_id: String) = apiInterface.getEmployees(outlet_id)

    suspend fun getProduk(
        category_product_id: Int
    ) = apiInterface.getProduk(
        ProdukRequest(
            category_product_id
        )
    )

    suspend fun detailProduk(idProduk: String) = apiInterface.detailProduk(idProduk)

    suspend fun getCategoryProduk() = apiInterface.getCategoryProduk()

    suspend fun login(username: String, password: String) = apiInterface.login(
        LoginRequest(
            username, password
        )
    )

    suspend fun getUserMe() = apiInterface.getUserMe()

    suspend fun openCloseCashier(
        type: String,
        money: Int
    ) = apiInterface.openCashier(
        CloseOpenCashierRequest(
            type, money
        )
    )

    suspend fun addPesanan(
        product_id: Int,
        variant_id: List<Int>,
        quantity: Int,
        note: String,
        tax_id: Int,
        discount_id: Int
    ) = apiInterface.addPesanan(
        AddPesananRequest(
            product_id, variant_id, quantity, note, tax_id, discount_id
        )
    )

    suspend fun indexCart() = apiInterface.indexCart()

    suspend fun updateQtyCart(
        idProduk: String,
        quantity: Int
    ) = apiInterface.updateQtyCart(
        idProduk,
        UpdateQtyRequest(
            quantity, "put"
        )
    )

    suspend fun updateProduk(
        product_id: Int,
        variant_id: List<Int>,
        quantity: Int,
        note: String,
        tax_id: Int,
        discount_id: Int,
        _method: String
    ) = apiInterface.updateProduk(
        product_id.toString(),
        UpdateProdukRequest(
            product_id, variant_id, quantity, note, tax_id, discount_id, _method
        )
    )

    suspend fun getTax() = apiInterface.getTax()

    suspend fun getDiscount() = apiInterface.getDiscount()

    suspend fun checkOut(
        money_received: Int,
        payment_type: String,
        trx_number: String,
        bank_name: String
    ) = apiInterface.checkOut(
        CheckOutRequest(
            money_received, payment_type, trx_number, bank_name
        )
    )

    suspend fun deleteItemCart(
        idItem: Int,
    ) = apiInterface.deleteItemCart(
        idItem.toString(),
    )

    suspend fun getHistory() = apiInterface.getHistory()

    suspend fun showHistory(
        idHistory: String,
    ) = apiInterface.showHistory(  idHistory  )

    suspend fun historyKasir(
        start_date: String,
        end_date: String
    ) = apiInterface.historyKasir(
        HistoryOpenCloseKasirRequest(
            start_date, end_date
        )
    )

    suspend fun getOutlet(
        idOutlet: String,
    ) = apiInterface.getOutlet(idOutlet)
}