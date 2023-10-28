package com.example.wallnut.model

data class Report(
    var totalSpends: String,
    var totalIncome: String,
    var netBalence:String,
    var budget:String
){

    override fun toString(): String {
        return "{totalSpends='$totalSpends', totalIncome='$totalIncome', budget='$budget', netBalence='$netBalence'}"
    }
}
