package com.example.wallnut.model

object Messages {
    private val messageData: MutableList<Message> = mutableListOf()

    fun addMessage(address: String, body: String, date: String, messageId: String, type: String) {
        val messageJson = Message(address,body,date,messageId,type)
        messageData.add(messageJson)
    }


    fun getMessageData(): List<Message> {
        return messageData
    }
}
