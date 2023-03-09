package com.shiva.bot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageRvAdapter(private val messageList : ArrayList<MessageRvModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    class UserMessageViewHolder(ItemView:View) : RecyclerView.ViewHolder(ItemView) {
        val useMsgTv : TextView = itemView.findViewById(R.id.right_chat_text_view)
    }

    class BotMessageViewHolder(ItemView:View) : RecyclerView.ViewHolder(ItemView) {
        val botMsgTv : TextView = itemView.findViewById(R.id.left_chat_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view:View
        return if(viewType==0) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.right_chat_item , parent , false)
            UserMessageViewHolder(view)
        }
        else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.left_chat_item , parent , false)
            BotMessageViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sender =  messageList.get(position).sentBy
        when(sender) {
            "user" -> (holder as UserMessageViewHolder).useMsgTv.setText(messageList.get(position).message)
            "bot" -> (holder as BotMessageViewHolder).botMsgTv.setText(messageList.get(position).message)
        }
    }
    override fun getItemViewType(position: Int): Int {
        return when(messageList.get(position).sentBy) {
            "user" -> 0
            "bot" -> 1
            else -> 1
        }
    }
}