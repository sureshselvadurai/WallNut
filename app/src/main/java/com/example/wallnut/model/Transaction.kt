package com.example.wallnut.model
data class Transaction(
    var date: String,
    val TransactionID: String,
    var amount: String,
    var id: String
) {
    fun putID(s: String) {
        this.id = s
    }
}