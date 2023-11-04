package com.example.wallnut.model

/**
 * Data class representing a message template.
 *
 * @property transactionIdPattern The pattern for matching transaction IDs.
 * @property debitMessageRegex The regular expression for matching debit messages.
 */
data class Template(
    private val transactionIdPattern: String,
    private val debitMessageRegex: String
) {
    /**
     * Get the pattern for matching transaction IDs.
     *
     * @return The transaction ID pattern as a string.
     */
    fun getTransactionIdPattern(): String {
        return transactionIdPattern
    }

    /**
     * Get the regular expression for matching debit messages.
     *
     * @return The debit message regular expression as a string.
     */
    fun getDebitMessageRegex(): String {
        return debitMessageRegex
    }
}
