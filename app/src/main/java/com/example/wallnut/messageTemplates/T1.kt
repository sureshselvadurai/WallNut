package com.example.wallnut.messageTemplates

import com.example.wallnut.model.Transaction

class T1(private val message: String):Templates {

        private val transactionIdPattern = "\\d{10}"
        private val amountPattern = "\\$[0-9]+(\\.[0-9]{2})?"
        private val dateTimePattern = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"
        private val merchantNamePattern = "XYZ Store"

        private val debitMessageRegex = """Dear Customer,\n\nYour account has been debited for a purchase at ($merchantNamePattern).\n\nTransaction ID: ($transactionIdPattern)\nAmount debited: ($amountPattern)\nDate and Time: ($dateTimePattern)\n\nThank you for using our services.\n\nBest regards,\nYour Bank"""

        fun parseDebitMessage() :Transaction{
            val regex = Regex(debitMessageRegex)
            val matchResult = regex.find(message)

//            if (matchResult != null) {
                println("Merchant: ${matchResult?.groups?.get(1)?.value}")
                println("Transaction ID: ${matchResult?.groups?.get(1)?.value}")
                println("Amount: ${matchResult?.groups?.get(1)?.value}")
                println("Date and Time: ${matchResult?.groups?.get(1)?.value}")
                return Transaction("","","","")
//            } else {
//                println("Debit message format not recognized.")
//                return Transaction("FAIL")
//            }
        }

    override fun patternchxeck(string: String): Transaction {
        val parser = T1(string)
        return  parser.parseDebitMessage()
    }


}
