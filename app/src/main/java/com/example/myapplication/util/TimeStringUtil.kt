package com.example.myapplication.util

import java.text.SimpleDateFormat
import java.util.Locale

fun getISOTimeToString(
    isoTime: String,
    stringFormat: String,
): String {
    val parsedDate =
        ISO_DATE_PATTERNS.firstNotNullOfOrNull { pattern ->
            runCatching {
                SimpleDateFormat(pattern, Locale.US).apply {
                    isLenient = false
                }.parse(isoTime)
            }.getOrNull()
        } ?: return isoTime

    return SimpleDateFormat(stringFormat, Locale.getDefault()).format(parsedDate)
}

private val ISO_DATE_PATTERNS =
    listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
        "yyyy-MM-dd'T'HH:mm:ssXXX",
        "yyyy-MM-dd'T'HH:mm:ss.SSSXX",
        "yyyy-MM-dd'T'HH:mm:ssXX",
        "yyyy-MM-dd'T'HH:mm:ss.SSSX",
        "yyyy-MM-dd'T'HH:mm:ssX",
    )
