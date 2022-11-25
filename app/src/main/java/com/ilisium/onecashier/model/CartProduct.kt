package com.ilisium.onecashier.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Eko S. Purnomo on 3/1/2020.
 * Email me at ekosetyopurnomo@gmail.com
 * Visit me on ekosp.com
 */
@Parcelize
data class CartProduct (
    val id_keranjang : String,
    val kode_barang : String,
    val nama_barang : String,
    val harga_jual : String,
    val jumlah_belanjan : Int,
    val sub_total : String,
    val gambar : String,
    val diskon : String,
    val harga_diskon : String,
    val harga_setelah_diskon : String
):Parcelable