package com.example.wallnut.model

data class Template (
    val transactionIdPattern: String,
    val amountPattern: String,
    val dateTimePattern: String,
    val merchantNamePattern: String,
    val debitMessageRegex: String
)