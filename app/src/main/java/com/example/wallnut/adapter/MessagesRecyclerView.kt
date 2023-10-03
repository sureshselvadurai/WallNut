package com.example.wallnut.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextClock
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallnut.R
import com.example.wallnut.model.Message
import com.google.android.material.imageview.ShapeableImageView

class MessagesRecyclerViewAdapter(private val messageList: List<Message>):
    RecyclerView.Adapter<MessagesRecyclerViewAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val titleAddress : TextView = itemView.findViewById(R.id.smsHeading)
        val smsbody : TextView = itemView.findViewById(R.id.smsmessage)
        val smsDate : TextView = itemView.findViewById(R.id.smsdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.messages_list,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = messageList[position]
        holder.titleAddress.text = currentItem.address
        holder.smsbody.text = currentItem.body
        holder.smsDate.text = currentItem.date.toString()
    }
}

