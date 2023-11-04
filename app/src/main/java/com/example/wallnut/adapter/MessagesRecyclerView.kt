package com.example.wallnut.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallnut.R
import com.example.wallnut.model.Message

/**
 * MessagesRecyclerViewAdapter is an adapter for displaying a list of messages in a RecyclerView.
 *
 * @param messageList The list of messages to be displayed.
 */
class MessagesRecyclerViewAdapter(private val messageList: List<Message>) :
    RecyclerView.Adapter<MessagesRecyclerViewAdapter.MyViewHolder>() {

    /**
     * ViewHolder class for representing individual items in the RecyclerView.
     *
     * @param itemView The view for each item in the RecyclerView.
     */
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleAddress: TextView = itemView.findViewById(R.id.smsHeading)
        val smsBody: TextView = itemView.findViewById(R.id.smsmessage)
        val smsDate: TextView = itemView.findViewById(R.id.smsdate)
    }

    /**
     * Create a new ViewHolder.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.messages_list, parent, false)
        return MyViewHolder(itemView)
    }

    /**
     * Get the total number of items in the dataset held by the adapter.
     *
     * @return The total number of items.
     */
    override fun getItemCount(): Int {
        return messageList.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = messageList[position]
        holder.titleAddress.text = currentItem.getAddress()
        holder.smsBody.text = currentItem.getBody()
        holder.smsDate.text = currentItem.getDate()
    }
}
