package com.example.wallnut.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallnut.R
import com.example.wallnut.adapter.MessagesRecyclerViewAdapter
import com.example.wallnut.helper.MessagesHelper
import com.example.wallnut.model.Message
import org.json.JSONArray
import org.json.JSONObject

class messagesDisplay : Fragment() {

    private lateinit var messagesRecyclerViewAdapter: MessagesRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var messagesList : ArrayList<Message>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages_display, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInit()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recycler_view_messages)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        messagesRecyclerViewAdapter = MessagesRecyclerViewAdapter(messagesList)
        recyclerView.adapter = messagesRecyclerViewAdapter


    }



    private fun dataInit(){
        messagesList = arrayListOf()
        val messagesHelper = MessagesHelper
        val jsonString = messagesHelper.readFile("/data/data/com.example.wallnut/files/messages.json")
        val messagesArray = JSONArray(jsonString)

        if (messagesArray != null||messagesArray.length()!=0){

            for (i in 0 until messagesArray.length()) {
                val jsonObject: JSONObject = messagesArray.getJSONObject(i)
                val address = jsonObject.get("address").toString();
                val body = jsonObject.get("body").toString()
                val date = jsonObject.get("date").toString()
                val messageId = jsonObject.get("messageId").toString()
                val type = jsonObject.get("type").toString()
                val message = Message(address, body, date, messageId, type)
                messagesList.add(message)
            }
        }

    }

}