package com.josecernu.rickandmortyapp.data.domain.util.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal object Date {
    fun Date.toFormattedDate(
        outputFormat: String,
        locale: Locale = Locale.getDefault(),
    ): String =
        try {
            val outputFormatter = SimpleDateFormat(outputFormat, locale)
            outputFormatter.format(this)
        } catch (e: Exception) {
            String()
        }
}
