package com.ilisium.onecashier.model.response

data class ProductDetailResponse(
    val response: Response,
    val metadata: Metadata
) {
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
        val product_variants: List<ProductVariant>,
        val product_ingredients: List<Any>,
        val created_by_user_spv: Int
    ) {
        data class CategoryProduct(
            val id: Int,
            val name: String
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

    data class Metadata(
        val message: String,
        val http_code: Int,
        val error_code: Int,
        val error_data: List<Any>
    )
}