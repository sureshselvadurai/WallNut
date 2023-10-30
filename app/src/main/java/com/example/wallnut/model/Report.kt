package com.example.wallnut.model

data class Report(
    var totalSpends: String,
    var totalIncome: String,
    var netBalence:String,
    var budget:String,
    var billReminder: String
){
    var foodSpend: String = "0"
    var utilitiesSpend: String = "0"
    var loanSpend: String = "0"
    var EntertainmentSpend: String = "0"
    var previousMonthSpend: String = ""

    override fun toString(): String {
        return "{totalSpends='$totalSpends',previousMonthSpend='$previousMonthSpend',EntertainmentSpend='$EntertainmentSpend',loanSpend='$loanSpend',foodSpend='$foodSpend',utilitiesSpend='$utilitiesSpend' ,billReminder= '$billReminder' , totalIncome='$totalIncome', budget='$budget', netBalence='$netBalence'}"
    }
}
