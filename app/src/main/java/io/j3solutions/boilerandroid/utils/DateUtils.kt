package io.j3solutions.boilerandroid.utils

import java.util.Calendar
import java.util.Date

fun Date.plus(value: Int, unit: Int = Calendar.HOUR): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(unit, value)
    return cal.time
}

fun Date.minus(value: Int, unit: Int = Calendar.HOUR) = plus(-value, unit)

fun getYesterday() = Date().minus(1, Calendar.DATE)

fun getEpoch() = System.currentTimeMillis()
