package com.example.wallnut.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * A data class representing a bill reminder item.
 *
 * @property date The date of the bill reminder.
 * @property type The type of the bill reminder.
 * @property amount The amount of the bill reminder.
 */
data class BillReminder(
    private var date: String,
    private var type: String,
    private var amount: String
) {
    // A date formatter to convert date strings to Date objects.
    private val dateFormatter = SimpleDateFormat("MMMdd", Locale.US)

    /**
     * Converts the date string to a Date object using the date formatter.
     *
     * @return The Date object representing the date of the bill reminder.
     */
    fun getDateAsDate(): Date {
        return dateFormatter.parse(date)
    }

    /**
     * Get the date of the bill reminder.
     *
     * @return The date as a string.
     */
    fun getDate(): String {
        return date
    }

    /**
     * Get the type of the bill reminder.
     *
     * @return The type as a string.
     */
    fun getType(): String {
        return type
    }

    /**
     * Get the amount of the bill reminder.
     *
     * @return The amount as a string.
     */
    fun getAmount(): String {
        return amount
    }
}
