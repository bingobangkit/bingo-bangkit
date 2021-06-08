package com.bingo.gobin

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    private val months = mapOf(
        1 to "January",
        2 to "February",
        3 to "March",
        4 to "April",
        5 to "May",
        6 to "June",
        7 to "July",
        8 to "August",
        9 to "September",
        10 to "October",
        11 to "November",
        12 to "December",
    )


    fun formatDate(date: String?): String {
        return if (date != null) {
            if (date != "") {
                val sq = date.toCharArray()
                val year =
                    (sq[0].toString() + sq[1].toString() + sq[2].toString() + sq[3].toString())
                val month = (sq[5].toString() + sq[6].toString()).toInt()
                val day = (sq[8].toString() + sq[9].toString()).toInt().toString()
                "${months[month]} $day, $year"
            } else {
                ""
            }

        } else {
            ""
        }

    }

    @SuppressLint("SimpleDateFormat")
    fun generateInvoice(type: String): String {
        val cal = Calendar.getInstance().time
        val date = SimpleDateFormat("yyyyMMddHHmmss").format(cal)
        return StringBuilder(type)
            .append('/')
            .append(date)
            .toString()
    }


}