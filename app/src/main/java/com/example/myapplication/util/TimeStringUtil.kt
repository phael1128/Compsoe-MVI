package com.example.myapplication.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun getISOTimeToString(
    isoTime: String,
    stringFormat: String,
): String {
    val zonedDateTime = ZonedDateTime.parse(isoTime)
    return zonedDateTime.format(DateTimeFormatter.ofPattern(stringFormat))
}
