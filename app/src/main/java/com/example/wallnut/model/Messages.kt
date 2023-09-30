package com.example.wallnut.model

import org.json.JSONObject

object Messages {
    private val messageData: MutableList<JSONObject> = mutableListOf()

    fun addMessage(address: String, body: String, date: Long) {
        val messageJson = JSONObject().apply {
            put("address", address)
            put("body", body)
            put("date", date)
        }
        messageData.add(messageJson)
    }

    fun getMessageData(): List<JSONObject> {
        return messageData
    }
}
