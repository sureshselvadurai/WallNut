package com.example.wallnut.messageTemplates

import com.example.wallnut.model.Transaction

interface Templates {
    fun patternchxeck(string: String): Transaction
}