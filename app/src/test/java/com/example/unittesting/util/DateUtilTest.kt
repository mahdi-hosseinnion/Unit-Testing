package com.example.unittesting.util

import com.example.unittesting.util.DateUtil.Companion.GET_MONTH_ERROR
import com.example.unittesting.util.DateUtil.Companion.monthNumbers
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.random.Random
import com.example.unittesting.util.DateUtil.Companion as DateUtil1

class DateUtilTest {
    private val TODAY_TIME = "10-2020"

    @Test
    fun testGetCurrentTimeStamp_returnTimeStamp() {
        assertEquals(TODAY_TIME, DateUtil1.getCurrentTimeStamp())
        print("TimeStamp generated correctly")
    }

    @ParameterizedTest
    @ValueSource(ints = (intArrayOf(0,1,2,3,4,5,6,7,8,9,10,11)))
    fun testGetMonthFromNumber_returnSuccess(number: Int){
        assertEquals(DateUtil1.months[number],DateUtil1.getMonthFromNumber(DateUtil1.monthNumbers[number]))
        print("${DateUtil1.months[number]} : ${DateUtil1.monthNumbers[number]} ")
    }
    @ParameterizedTest
    @ValueSource(ints = (intArrayOf(0,1,2,3,4,5,6,7,8,9,10,11)))
    fun testGetMonthFromNumber_returnFailure(number: Int){
        val randomNumber= Random.nextInt(13,100)
        assertEquals(GET_MONTH_ERROR,DateUtil1.getMonthFromNumber((randomNumber + number).toString()))
        print("${(randomNumber + number)} : $GET_MONTH_ERROR ")
    }

}