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
import com.example.wallnut.utils.Constants
import com.example.wallnut.utils.Utils
import com.example.wallnut.model.Message
import org.json.JSONArray
import org.json.JSONObject

/**
 * A fragment to display a list of messages.
 */
class MessagesDisplay : Fragment() {

    private lateinit var messagesRecyclerViewAdapter: MessagesRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var messagesList: ArrayList<Message>

    /**
     * Inflates the layout for this fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views.
     * @param container The parent view that this fragment's UI should be attached to.
     * @param savedInstanceState The saved instance state if available.
     * @return The root view for this fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_messages_display, container, false)
    }

    /**
     * Called when the view hierarchy associated with this fragment is created.
     * Sets up the RecyclerView for displaying messages.
     *
     * @param view The root view for this fragment.
     * @param savedInstanceState The saved instance state if available.
     */
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

    /**
     * Initialize the data for the messages list by reading messages from a JSON file.
     */
    private fun dataInit() {
        messagesList = arrayListOf()
        val jsonString = Utils.readFile(Constants.MESSAGES_PATH)
        val messagesArray = JSONArray(jsonString)

        for (i in 0 until messagesArray.length()) {
            val jsonObject: JSONObject = messagesArray.getJSONObject(i)
            val address = jsonObject.get("address").toString()
            val body = jsonObject.get("body").toString()
            val date = jsonObject.get("date").toString()
            val messageId = jsonObject.get("messageId").toString()
            val type = jsonObject.get("type").toString()
            val message = Message(address, body, date, messageId, type)
            messagesList.add(message)
        }
    }
}
