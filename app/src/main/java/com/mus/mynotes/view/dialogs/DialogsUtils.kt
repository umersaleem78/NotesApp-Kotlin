package com.mus.mynotes.view.dialogs

import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources.Theme
import androidx.appcompat.app.AlertDialog
import com.mus.mynotes.R
import com.mus.mynotes.application.MyNotesApp.Companion.appContext
import com.mus.mynotes.utils.AppUtils


object DialogsUtils {

    fun showConfirmationDialog(context: Context,callback: (Boolean) -> Unit?) {
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> callback(true)
                    DialogInterface.BUTTON_NEGATIVE -> callback(false)
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage(AppUtils.getString(R.string.delete_confirmation))
            .setPositiveButton(AppUtils.getString(R.string.yes), dialogClickListener)
            .setNegativeButton(AppUtils.getString(R.string.no), dialogClickListener).show()

    }

}