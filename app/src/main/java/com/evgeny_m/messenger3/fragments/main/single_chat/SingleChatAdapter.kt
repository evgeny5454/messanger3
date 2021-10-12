package com.evgeny_m.messenger3.fragments.main.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.evgeny_m.messenger3.R
import com.evgeny_m.messenger3.databinding.ItemMessageBinding
import com.evgeny_m.messenger3.model.CommonModel
import com.evgeny_m.messenger3.utils.asTime
import com.evgeny_m.messenger3.utils.currentUserId
import com.evgeny_m.messenger3.utils.showToast

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var listMessagesCache = emptyList<CommonModel>()

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageBinding.bind(view)

        val blockUserMessage = binding.itemMessageUserMessageView
        val userMassageText = binding.itemMessageSendUserMessageText
        val userMassageTime = binding.itemMessageSendUserMessageTime
        val userMessageView = binding.itemUserMessageView

        val blockReceiveMessage = binding.itemMessageReceivedMessageView
        val receivedMassageText:TextView = binding.itemMessageReceivedMessageText
        val receivedMessageTime:TextView = binding.itemMessageReceivedMessageTime
        val receivedMessageView = binding.itemReceivedMessageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message,parent,false)
        return SingleChatHolder(view)
    }

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {

        if (listMessagesCache[position].from == currentUserId) {

            holder.blockUserMessage.visibility = View.VISIBLE
            holder.blockReceiveMessage.visibility = View.GONE
            holder.userMassageText.text = listMessagesCache[position].text
            holder.userMassageTime.text = listMessagesCache[position].time_stamp.toString().asTime()
            holder.userMessageView.setOnClickListener {
                showToast("жмяк")
            }

        } else {

            holder.blockReceiveMessage.visibility = View.VISIBLE
            holder.blockUserMessage.visibility = View.GONE
            holder.receivedMassageText.text = listMessagesCache[position].text
            holder.receivedMessageTime.text = listMessagesCache[position].time_stamp.toString().asTime()
            holder.receivedMessageView.setOnClickListener {
                showToast("жмяк")
            }
        }

    }

    override fun getItemCount(): Int {
        return listMessagesCache.size
    }

    fun setList(list: List<CommonModel>) {
        listMessagesCache = list
        notifyDataSetChanged()
    }
}


