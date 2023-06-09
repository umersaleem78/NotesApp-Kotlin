package com.mus.mynotes.view.adapters

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.mus.mynotes.utils.DateTimeUtils


@BindingAdapter("setDate")
fun TextView.setDate(timeStamp: String?) {
    if (timeStamp.isNullOrEmpty()) {
        return
    }
    text = DateTimeUtils.convertTimeStampToDate(timeStamp, DateTimeUtils.dateFormat)
}

@BindingAdapter("setTime")
fun TextView.setTime(timeStamp: String?) {
    if (timeStamp.isNullOrEmpty()) {
        return
    }
    text = DateTimeUtils.convertTimeStampToDate(timeStamp, DateTimeUtils.timeFormat)
}

@BindingAdapter("setBgColor")
fun View.setBgColor(color: String?) {
    if (color.isNullOrEmpty()) {
        return
    }
    if (this is CardView) {
        setCardBackgroundColor(Color.parseColor(color))
    } else {
        setBackgroundColor(Color.parseColor(color))
    }
}

@BindingAdapter("setLineThrough")
fun TextView.setLineThrough(isChecked: Int) {
    if (isChecked == 1) {
        paintFlags = Paint.STRIKE_THRU_TEXT_FLAG;
    }
}