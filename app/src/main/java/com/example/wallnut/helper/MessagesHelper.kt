package com.example.wallnut.helper

import android.content.Context
import android.widget.Toast
import com.example.wallnut.model.Messages
import com.google.gson.Gson
import java.io.FileOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class MessagesHelper {

    private fun readFile(filePath: String,context: Context): String? {
        var fileContents: String? = null
        val file = File(filePath)

        try {
            val inputStream = FileInputStream(file)
            val length = file.length().toInt()
            val buffer = ByteArray(length)

            inputStream.read(buffer)
            fileContents = String(buffer)

            inputStream.close()
        } catch (e: IOException) {
            val c = 1;
        }

        return fileContents
    }

    companion object {
        fun writeToFile(messageManager: Messages, context: Context) {

            val gson = Gson()
            var json = gson.toJson(messageManager.getMessageData())
            json = json.toString()

            try {
                val fileOutputStream: FileOutputStream = context.openFileOutput("messages.json", Context.MODE_PRIVATE)
                fileOutputStream.write(json.toByteArray())

            }catch (e:Exception){
                Toast.makeText(context, "Saving messages failed. Please try again", Toast.LENGTH_SHORT).show()
            }

            val configState = context.getSharedPreferences( "configState", Context.MODE_PRIVATE)
            val editor = configState?.edit()
            editor?.putString("messages",json.toString());
            editor?.commit();
        }
    }

}