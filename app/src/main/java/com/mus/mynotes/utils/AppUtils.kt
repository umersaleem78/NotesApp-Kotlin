package com.mus.mynotes.utils

import android.view.Gravity
import android.widget.Toast
import com.mus.mynotes.application.MyNotesApp.Companion.appContext
import kotlin.random.Random

object AppUtils {

    fun showToast(message: String?) {
        if (message.isNullOrEmpty()) {
            return
        }
        appContext?.let { context ->
            val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    fun getString(resourceId: Int): String {
        return appContext?.getString(resourceId) ?: ""
    }

    private fun fetchColorsList(): List<String> {
        val list = ArrayList<String>()
        list.add("#37a2f4")
        list.add("#fea31a")
        list.add("#d55f70")
        list.add("#04798d")
        list.add("#4682B4")
        return list
    }

    fun getRandomColor(): String {
        val colorsList = fetchColorsList()
        val random = Random.nextInt(colorsList.size - 1)
        return colorsList[random]
    }

}