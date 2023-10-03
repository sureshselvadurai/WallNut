package com.example.wallnut.model

data class Report(
    val totalSpends: String,
    val totalIncome: String,
    var netBalence:String
){

    override fun toString(): String {
        return "{totalSpends='$totalSpends', totalIncome='$totalIncome', netBalence='$netBalence'}"
    }
}
