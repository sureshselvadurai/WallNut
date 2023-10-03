package com.example.wallnut.helper

import com.example.wallnut.model.Report
import com.example.wallnut.model.Transaction

class ReportHelper {


    fun reportCreator(transactionList: MutableList<Transaction>): Report {

        var netBalence = 0.0;
        var report = Report("","","")

        for(i:Transaction in transactionList){
            netBalence += i.amount.toFloat();
        }
        report.netBalence = netBalence.toString()
        return  report
    }


}

