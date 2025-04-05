package com.josecernu.rickandmortyapp.data.domain.util.extension

import com.josecernu.rickandmortyapp.data.domain.util.extension.Date.toFormattedDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val YYYY_MM_DD = "yyyy-MM-dd"
const val DATE_FORMAT = "dd MMM yyyy"

fun String.toDateFormat(
    inputFormat: String = YYYY_MM_DD,
    inputLocale: Locale = Locale.ENGLISH,
    outputLocale: Locale = Locale.getDefault(),
): String {
    val outputFormat = DATE_FORMAT
    val date = this.toDate(inputFormat, inputLocale) ?: return this
    return date.toFormattedDate(outputFormat, outputLocale).uppercase()
}

fun String?.toDate(
    inputFormat: String = YYYY_MM_DD,
    locale: Locale = Locale.getDefault(),
): Date? =
    try {
        if (isNullOrBlank()) throw Exception("invalid date")
        val inputFormatter = SimpleDateFormat(inputFormat, locale)
        inputFormatter.parse(this) ?: throw Exception()
    } catch (e: Exception) {
        null
    }

fun String.capitalizeDate(): String {
    return this.split(" ")
        .joinToString(" ") { if (it[0].isLetter()) it.capitalizeFirstLetter() else it }
}

fun String.capitalizeFirstLetter(): String {
    return if (this.isNotEmpty()) this[0].uppercaseChar() + this.substring(1).lowercase() else this
}
