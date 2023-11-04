package com.example.wallnut.model

/**
 * Data class representing a transaction.
 *
 * @property date The date of the transaction.
 * @property transactionID The unique transaction ID.
 * @property amount The transaction amount.
 * @property id The ID of the transaction.
 * @property transactionInfo Additional information about the transaction.
 */
data class Transaction(
    private var date: String,
    private val transactionID: String,
    private var amount: String,
    private var id: String,
    private var transactionInfo: Map<String, String>
) {
    /**
     * Sets the ID of the transaction.
     *
     * @param s The ID to be set.
     */
    fun putID(s: String) {
        this.id = s
    }

    /**
     * Get the date of the transaction.
     *
     * @return The transaction date as a string.
     */
    fun getDate(): String {
        return date
    }

    /**
     * Get the transaction amount.
     *
     * @return The transaction amount as a string.
     */
    fun getAmount(): String {
        return amount
    }

    /**
     * Get the bill type from the transaction information.
     *
     * @return The bill type as a string or null if not present in the transaction information.
     */
    fun getTransactionInfoBillType(): String? {
        return transactionInfo["type"]
    }

    /**
     * Get the spend type from the transaction information.
     *
     * @return The spend type as a string or null if not present in the transaction information.
     */
    fun getTransactionInfoSpendType(): String? {
        return transactionInfo["spendType"]
    }

    /**
     * Set the transaction information.
     *
     * @param transactionInfo The new transaction information as a map of key-value pairs.
     */
    fun setTransactionInfo(transactionInfo: Map<String, String>) {
        this.transactionInfo = transactionInfo
    }

    /**
     * Set the transaction amount.
     *
     * @param value The new transaction amount as a string.
     */
    fun setAmount(value: String) {
        amount = value
    }

    /**
     * Get the ID of the transaction.
     *
     * @return The transaction ID as a string.
     */
    fun getID(): String {
        return id
    }

    /**
     * Set the date of the transaction.
     *
     * @param value The new transaction date as a string.
     */
    fun setDate(value: String) {
        date = value
    }
}
