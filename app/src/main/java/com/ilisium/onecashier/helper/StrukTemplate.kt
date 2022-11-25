package com.ilisium.onecashier.helper

import android.content.Context
import com.ilisium.onecashier.helper.Tools.Companion.rupiahStruk
import com.ilisium.onecashier.model.response.DetailHistoryResponse
import com.mazenrashed.printooth.data.printable.ImagePrintable
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Eko S. Purnomo on 3/27/2022.
 * Email me at ekosetyopurnomo@gmail.com
 * Visit me on ekosp.com
 */

class StrukTemplate {

    class Builder() {
        private lateinit var data: DetailHistoryResponse.Response

        val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault())
        val dfDate = SimpleDateFormat("dd/MM/yyyy", Locale("in_ID"))
        val dfTime = SimpleDateFormat("HH:mm", Locale("in_ID"))
        var mDrawableImage: Int = -1

        lateinit var context: Context

        fun setContext(ctx: Context): Builder {
            this.context = ctx
            return this
        }

        fun setStrukLogo(imgScr: Int): Builder {
            this.mDrawableImage = imgScr
            return this
        }

        fun setData(data: DetailHistoryResponse.Response): Builder {
            this.data = data
            return this
        }

        fun build() = ArrayList<Printable>().apply {

            if (mDrawableImage > 0) {
                add(ImagePrintable.Builder(mDrawableImage, context.resources).build())
            }

            add(
                TextPrintable.Builder()
                    .setText("\n")
                    .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                    .build()
            )


            add(
                TextPrintable.Builder()
                    .setText(data.outlet.name + "\n")
                    .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                    .build()
            )

            add(
                TextPrintable.Builder()
                    .setText(data.outlet.address + "\n")
                    .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                    .setNewLinesAfter(1)
                    .build()
            )

//            add(
//                TextPrintable.Builder()
//                    .setText("-".repeat(32) + "\n") // total 32 char on single line
//                    .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
//                    .build()
//            )

            // info nomor transaksi
            add(
                TextPrintable.Builder()
                    .setText(
                        String.format(
                            "%1$-16s %2$15s",
                            formatter.parse(data.created_at)?.let { dfDate.format(it) },
                            formatter.parse(data.created_at)?.let { dfTime.format(it) },
                        )
                    )
                    .setNewLinesAfter(0)
                    .build()
            )

            add(
                TextPrintable.Builder()
                    .setText(
                        String.format(
                            "%1$-12s %2$19s",
                            "Order ID",
                            data.invoice_number
                        )
                    )
                    .setNewLinesAfter(0)
                    .build()
            )

            // info kasir
            add(
                TextPrintable.Builder()
                    .setText(
                        String.format(
                            "%1$-12s %2$19s",
                            "Kasir",
                            data.created_by.name
                        )
                    )
                    .setNewLinesAfter(0)
                    .build()
            )


            add(
                TextPrintable.Builder()
                    .setText("-".repeat(32) + "\n") // total 32 char on single line
                    .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                    .build()
            )

//             tampilkan list belanjaan
            for (product in data.transaction_detail) {
                // nama produk
                add(
                    TextPrintable.Builder()
                        .setText(
                            product.item_final_name.take(25) + "\n",
                        )
                        .build()
                )

                // jumlah dan harga
                val harga : Float = product.price_pcs.toFloat() + product.variant_price.toFloat()
                add(
                    TextPrintable.Builder()
                        .setText(
                            String.format(
                                "%1$-21s %2$10s",
                                "${product.quantity}x@${rupiahStruk(harga.toString())}",
                                "${rupiahStruk(product.sub_total_price)}\n"
                            )
                        )
                        .build()
                )
            }

            add(
                TextPrintable.Builder()
                    .setText("-".repeat(29) + "(+)\n")
                    .setAlignment(DefaultPrinter.ALIGNMENT_RIGHT)
                    .build()
            )

            add(
                TextPrintable.Builder()
                    .setText(
                        String.format(
                            "%1$-16s %2$15s",
                            "Subtotal",
                            "${rupiahStruk(data.total_product_price_without_tax)}\n"
                        )
                    )
                    .setAlignment(DefaultPrinter.ALIGNMENT_RIGHT)
                    .build()
            )

            add(
                TextPrintable.Builder()
                    .setText(
                        String.format(
                            "%1$-16s %2$15s",
                            "PPN",
                            "${data.total_tax_price}\n" // rupiah struk
                        )
                    )
                    .setAlignment(DefaultPrinter.ALIGNMENT_RIGHT)
                    .build()
            )

            add(
                TextPrintable.Builder()
                    .setText(
                        String.format(
                            "%1$-16s %2$15s",
                            "Diskon",
                            "${rupiahStruk(data.total_discount_price)}\n"
                        )
                    )
                    .setAlignment(DefaultPrinter.ALIGNMENT_RIGHT)
                    .build()
            )

            add(
                TextPrintable.Builder()
                    .setText("-".repeat(32)+"\n")
                    .setAlignment(DefaultPrinter.ALIGNMENT_RIGHT)
                    .build()
            )

            add(
                TextPrintable.Builder()
                    .setText(
                        String.format(
                            "%1$-16s %2$15s",
                            "Total",
                            "Rp ${rupiahStruk(data.total_product_price)}\n"
                        )
                    )
                    .setAlignment(DefaultPrinter.ALIGNMENT_RIGHT)
                    .build()
            )

            if (data.payment_type.equals("cash",true)){
                add(
                    TextPrintable.Builder()
                        .setText(
                            String.format(
                                "%1$-10s %2$21s",
                                "Bayar",
                                "Rp ${rupiahStruk(data.money_received)}\n"
                            )
                        )
                        .setAlignment(DefaultPrinter.ALIGNMENT_RIGHT)
                        .build()
                )

                add(
                    TextPrintable.Builder()
                        .setText("-".repeat(32) + "\n")
                        .setAlignment(DefaultPrinter.ALIGNMENT_RIGHT)
                        .build()
                )

                add(
                    TextPrintable.Builder()
                        .setText(
                            String.format(
                                "%1$-15s %2$16s",
                                "Kembali",
                                "Rp ${rupiahStruk(data.money_change)}"
                            )
                        )
                        .setAlignment(DefaultPrinter.ALIGNMENT_RIGHT)
                        .build()
                )

            } else {
                add(
                    TextPrintable.Builder()
                        .setText(
                            String.format(
                                "%1$-11s %2$20s",
                                "EDC ${data.bank_name}",
                                "${data.trx_number}\n"
                            )
                        )
                        .setAlignment(DefaultPrinter.ALIGNMENT_RIGHT)
                        .build()
                )
            }


            add(
                TextPrintable.Builder()
                    .setText("-".repeat(32) + "\n") // total 32 char on single line
                    .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                    .setNewLinesAfter(1)
                    .build()
            )



//            add(
//                TextPrintable.Builder()
//                    .setText("-".repeat(32)) // total 32 char on single line
//                    .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
//                    .build()
//            )

            add(
                TextPrintable.Builder()
                    .setText(
                        "Maaf, barang yang sudah dibeli\n" +
                                "tidak bisa dikembalikan.\nTerima kasih."
                    )
                    .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                    .setNewLinesAfter(2)
                    .build()
            )
        }
    }

}