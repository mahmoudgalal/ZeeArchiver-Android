package com.mg.zeearchiver.dialogs

import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.mg.zeearchiver.R
import java.util.concurrent.Callable

class ExtractProgressDialog(private val con: Context, cancelAction: Callable<*>) {
    private val currItem: TextView
    private val percentage: TextView
    val root: View
    private lateinit var pd: AlertDialog
    private val cancelBtn: Button
    fun showDialog(title: String?) {
        val builder = AlertDialog.Builder(con)
        pd = builder.setView(root).setTitle(title)
                .setCancelable(false).create()
        pd.window?.setWindowAnimations(R.style.moving_dialog)
        pd.show()
    }

    fun dismiss() {
        if (pd.isShowing) pd.dismiss()
    }

    fun setDialogTitle(title: String?) {
        pd.setTitle(title)
    }

    fun setDialogTitle(resid: Int) {
        pd.setTitle(resid)
    }

    fun setCurrentItemText(st: String?) {
        currItem.text = st
    }

    fun setPercentage(st: String?) {
        percentage.text = st
    }

    fun setPercentage(percent: Long) {
        //"Compression Ratio:"+ratio+"%"
        percentage.text = "${con.getString(R.string.compression_ratio)}: $percent %"
    }

    init {
        root = LayoutInflater.from(con).inflate(R.layout.progress_dialog, null)
        currItem = root.findViewById(R.id.current_file)
        currItem.ellipsize = TextUtils.TruncateAt.MARQUEE
        currItem.isSelected = true
        percentage = root.findViewById(R.id.comp_ratio)
        cancelBtn = root.findViewById(R.id.cancel_extraction_btn)
        cancelBtn.visibility = View.VISIBLE
        cancelBtn.setOnClickListener {
            it.isEnabled = false
            try {
                cancelAction.call()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}