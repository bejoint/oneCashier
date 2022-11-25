package com.ilisium.onecashier.helper

import android.view.View
import androidx.core.widget.NestedScrollView
import java.text.NumberFormat
import java.util.*

class Tools {
    companion object {
        fun nestedScrollTo(nested: NestedScrollView, targetView: View) {
            nested.post { nested.scrollTo(500, targetView.bottom) }
        }

        fun intToRupiah(number: String): String {
            val cf1 =
                NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            cf1.maximumFractionDigits = 0
            return cf1.format(Integer.valueOf(number))
        }

        fun rupiahStruk(number: String): String {

//            val split = number.split("\\.")
//            val num1 = split[0]
//            val num2 = if (split.size == 2) split[1] else "00"

//            val cf1 =
//                NumberFormat.getCurrencyInstance(Locale("id", "ID"))
//            cf1.maximumFractionDigits = 0
//            return cf1.format(number.toDouble().toInt().toString())

            return number
        }

    }
}
