package com.ilisium.onecashier.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Kasir(
    val nomor_transaksi: String,
    val harga_total: String,
    val harga_diskon: String,
    val harga_setelah_diskon: String,
    val nama_warung: String?,
    val alamat_warung: String?,
    val nama_kasir: String?,
    val nama_pelanggan: String?,
    val hp_pelanggan: String?,
    val alamat_pengiriman: String?,
    val potongan_voucher : String
) : Parcelable