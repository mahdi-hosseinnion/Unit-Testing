package com.example.unittesting.util

import java.text.SimpleDateFormat
import java.util.*

public class DateUtil {

    companion object {
        private val monthNumbers =
            arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
        private val months = arrayOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )
        private val GET_MONTH_ERROR = "Error. Invalid month number."
        private val DATE_FORMAT = "MM-yyyy"

        public fun getCurrentTimeStamp(): String {
            try {
                val dataFormat: SimpleDateFormat =
                    SimpleDateFormat(DATE_FORMAT) //MUST USE LOWERCASE 'y'. API 23- can't use uppercase
                return dataFormat.format(Date())//find today date
            } catch (e: Exception) {
                e.printStackTrace()
                throw Exception("Couldn't format the data into MM-yyyy")
            }
        }

        public fun getMonthFromNumber(monthNumber: String) = when (monthNumber) {
            "01" -> months[0];

            "02" -> months[1];

            "03" -> months[2];

            "04" -> months[3];

            "05" -> months[4];

            "06" -> months[5];

            "07" -> months[6];

            "08" -> months[7];

            "09" -> months[8];

            "10" -> months[9];

            "11" -> months[10];

            "12" -> months[11];

            else -> GET_MONTH_ERROR;

        }
    }
}