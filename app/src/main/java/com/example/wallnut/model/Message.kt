package com.example.wallnut.model

/**
 * Data class representing an SMS message.
 *
 * @property address The address (phone number) associated with the message.
 * @property body The content of the SMS message.
 * @property date The date and time when the message was sent or received.
 * @property messageId The unique identifier for the message.
 * @property type The type of the message (e.g., sent or received).
 */
data class Message(
    private val address: String,
    private val body: String,
    private val date: String,
    private val messageId: String,
    private val type: String
) {
    /**
     * Get the content of the SMS message.
     *
     * @return The message body as a string.
     */
    fun getBody(): String {
        return body
    }

    /**
     * Get the address (phone number) associated with the message.
     *
     * @return The message address as a CharSequence.
     */
    fun getAddress(): CharSequence {
        return address
    }

    /**
     * Get the date and time when the message was sent or received.
     *
     * @return The message date as a CharSequence.
     */
    fun getDate(): CharSequence {
        return date
    }
}
