package com.ilisium.onecashier.model.response

data class HistoryResponse(
    val response: List<Response>,
    val metadata: Metadata
) {
    data class Response(
        val id: Int,
        val invoice_number: String,
        val quantity: Int,
        val total_hpp_price: String,
        val total_discount_price: String,
        val total_tax_price: String,
        val total_product_price_without_tax: String,
        val total_product_price: String,
        val money_received: String,
        val money_change: String,
        val outlet_id: Int,
        val payment_type: String,
        val trx_number: String,
        val bank_name: String,
        val outlet: Outlet,
        val transaction_detail: List<TransactionDetail>,
        val created_by_user_cashier: Int,
        val created_by: CreatedBy,
        val created_at: String
    ) {
        data class Outlet(
            val id: Int,
            val name: String,
            val address: String,
            val description: String
        )

        data class TransactionDetail(
            val id: Int,
            val transaction_id: Int,
            val product_id: Int,
            val item_final_name: String,
            val product: Product,
            val product_variant_id: String?= "",
            val product_variant: List<ProductVariant>?= mutableListOf(),
            val price_pcs: String,
            val tax_pcs: String,
            val hpp_price: String,
            val variant_price: String,
            val quantity: Int,
            val sub_total_price: String,
            val sub_total_hpp: String,
            val tax_price: String,
            val discount_price: String,
            val sub_total_price_final: String,
            val note: String?= "",
//            val outlet_id: Int?= 0,
            val created_by_user_cashier: Int,
            val created_at: String,
            val updated_at: String
        ) {
            data class Product(
                val id: Int,
                val name: String,
                val sku: String,
                val category_product_id: Int?= 0,
                val description: String,
                val stock: String,
                val stock_notif: String,
                val uom: String,
                val price: String,
                val hpp: String?= "",
                val image: String,
//                val outlet_id: Int,
                val created_by_user_spv: Int
            )

            data class ProductVariant(
                val id: Int,
                val product_id: Int,
                val raw_material_id: Int,
                val name: String,
                val uom: String,
                val price: String,
                val used_material: String,
                val outlet_id: Int,
                val created_by_user_spv: Int
            )
        }

        data class CreatedBy(
            val id: Int,
            val username: String,
            val name: String,
            val image: String,
            val outlet_id: Int
        )
    }

    data class Metadata(
        val message: String,
//        val detailed_message: Any,
        val http_code: Int,
        val error_code: Int,
//        val error_data: List<Any>
    )
}