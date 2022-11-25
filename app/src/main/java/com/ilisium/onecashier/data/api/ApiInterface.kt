package com.ilisium.onecashier.data.api

import com.ilisium.onecashier.model.request.*
import com.ilisium.onecashier.model.response.*
import retrofit2.http.*

interface ApiInterface {

    @POST("employees")
    @FormUrlEncoded
    suspend fun getEmployees(
        @Field("outlet_id") outlet_id: String
    ): EmployeesResponse

    @POST("cashier/product/index")
    suspend fun getProduk(
        @Body request: ProdukRequest
    ): ProdukResponse

//    @GET("cashier/categoryproduct/index")
    @GET("cashier/categoryproductbyfilter/index")
    suspend fun getCategoryProduk(
    ): ProdukCategoryResponse

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("cashier/user/me")
    suspend fun getUserMe(
    ): UserMeResponse

    @POST("cashier/cashierhistory/store")
    suspend fun openCashier(
        @Body request: CloseOpenCashierRequest
    ): BaseResponse

    @GET("cashier/product/show/{idProduk}")
    suspend fun detailProduk(
        @Path("idProduk") idProduk: String
    ): ProductDetailResponse

    @POST("cashier/cart/store")
    suspend fun addPesanan(
        @Body request: AddPesananRequest
    ): BaseResponse

    @GET("cashier/cart/index")
    suspend fun indexCart(
    ): IndexCartResponse

    @POST("cashier/cart/update_qty/{idProduk}")
    suspend fun updateQtyCart(
        @Path("idProduk") idProduk: String,
        @Body request: UpdateQtyRequest
    ): BaseResponse

    @POST("cashier/cart/update/{idProduk}")
    suspend fun updateProduk(
        @Path("idProduk") idProduk: String,
        @Body request: UpdateProdukRequest
    ): BaseResponse

    @GET("cashier/tax/index")
    suspend fun getTax(
    ): TaxResponse

    @GET("cashier/discount/index")
    suspend fun getDiscount(
    ): DiscountResponse

    @POST("cashier/checkout/store")
    suspend fun checkOut(
        @Body request: CheckOutRequest
    ): DetailHistoryResponse

    @DELETE("cashier/cart/destroy/{idItem}")
    suspend fun deleteItemCart(
        @Path("idItem") idItem: String,
    ): BaseResponse

    @POST("cashier/transaction/index")
    suspend fun getHistory(
    ): HistoryResponse

    @GET("cashier/transaction/show/{idHistory}")
    suspend fun showHistory(
        @Path("idHistory") idHistory: String
    ): DetailHistoryResponse

    @POST("cashier/cashierhistory/index")
    suspend fun historyKasir(
        @Body request: HistoryOpenCloseKasirRequest
    ): OpenCloseKasirResponse

    @GET("cashier/outlet/show/{idOutlet}")
    suspend fun getOutlet(
        @Path("idOutlet") idOutlet: String
    ): OutletResponse
}