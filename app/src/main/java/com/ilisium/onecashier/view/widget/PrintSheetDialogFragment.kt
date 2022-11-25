package com.ilisium.onecashier.view.widget

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ilisium.onecashier.R
import com.ilisium.onecashier.databinding.LayoutModalBottomSheetBinding
import com.ilisium.onecashier.helper.SessionManager
import com.ilisium.onecashier.helper.StrukTemplate
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.response.DetailHistoryResponse
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback


class PrintSheetDialogFragment : BottomSheetDialogFragment() {

    private var printing: Printing? = null
    private lateinit var cartResponse: DetailHistoryResponse.Response
    private var dataValid = false

    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
        val KEY_CART_REPONSE = "cart_response"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (dialog as? BottomSheetDialog)?.behavior?.state = STATE_EXPANDED

        return inflater.inflate(R.layout.layout_modal_bottom_sheet, container, false)
    }

    val binding by viewBinding(LayoutModalBottomSheetBinding::bind)
    lateinit var session: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Printooth.hasPairedPrinter()) {
            printing = Printooth.printer()
        } else {
            startActivityForResult(
                Intent(requireContext(), ScanningActivity::class.java),
                ScanningActivity.SCANNING_FOR_PRINTER
            )
        }

        cartResponse = arguments?.getParcelable(KEY_CART_REPONSE)!!

        binding.nomorTransaksi.text = cartResponse?.invoice_number

        initViews()
        setListener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        session = SessionManager(requireContext())
    }

    private fun printSomePrintable() {

        if (cartResponse != null) {
            val printables = StrukTemplate.Builder()
                .setContext(requireContext())
                .setData(cartResponse!!)
                .build()
            printing?.print(printables)

        } else {
            Toast.makeText(
                requireContext(),
                "Terjadi kesalahan pengolahan data",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initViews() {
        binding.pairingBluetooth.text =
            if (Printooth.hasPairedPrinter()) "Un-pair ${Printooth.getPairedPrinter()?.name}" else "Pair with printer"

        if (Printooth.hasPairedPrinter()) {
            binding.printStruk.background = resources.getDrawable(R.drawable.bg_btn_green)
            binding.printStruk.text = "PRINT STRUK"
        } else {
            binding.printStruk.background = resources.getDrawable(R.drawable.bg_btn_red)
            binding.printStruk.text = "PRINTER OFFLINE"
        }
    }

    private fun setListener() {
        binding.printStruk.setOnClickListener {
            if (!Printooth.hasPairedPrinter()) startActivityForResult(
                Intent(
                    requireContext(),
                    ScanningActivity::class.java
                ),
                ScanningActivity.SCANNING_FOR_PRINTER
            )
            else printSomePrintable()
        }


        binding.pairingBluetooth.setOnClickListener {
            if (Printooth.hasPairedPrinter()) Printooth.removeCurrentPrinter()
            else startActivityForResult(
                Intent(requireContext(), ScanningActivity::class.java),
                ScanningActivity.SCANNING_FOR_PRINTER
            )

            initViews()
        }

        printing?.printingCallback = object : PrintingCallback {
            override fun connectingWithPrinter() {
                Toast.makeText(
                    requireContext(),
                    "Connecting with printer",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun printingOrderSentSuccessfully() {
                Toast.makeText(
                    requireContext(),
                    "Order sent to printer",
                    Toast.LENGTH_SHORT
                ).show()

//                startActivity(Intent(this@PrintStrukActivity, KasirCatalogActivity::class.java))
//                finish()
                dismiss()
            }

            override fun connectionFailed(error: String) {
                Toast.makeText(
                    requireContext(),
                    "Failed to connect printer",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun disconnected() {

            }

            override fun onError(error: String) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }

            override fun onMessage(message: String) {
                Toast.makeText(
                    requireContext(),
                    "Message: $message",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK)
//            printSomePrintable()
            initViews()

        printSample()
    }

    private fun printSample() {
        var printables = ArrayList<Printable>()
        var printable = TextPrintable.Builder()
            .setText("Tes Print OneCashier") //The text you want to print
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
            .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD) //Bold or normal
            .setFontSize(DefaultPrinter.FONT_SIZE_NORMAL)
            .setUnderlined(DefaultPrinter.UNDERLINED_MODE_ON) // Underline on/off
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
            .setNewLinesAfter(1) // To provide n lines after sentence
            .build()
        printables.add(printable)
        Printooth.printer().print(printables)
    }


    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Toast.makeText(requireContext(), "CANCEL", Toast.LENGTH_SHORT).show()
    }

}