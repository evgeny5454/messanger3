package com.evgeny_m.messenger3.fragments.main.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.evgeny_m.messenger3.R
import com.evgeny_m.messenger3.databinding.ItemMessageBinding
import com.evgeny_m.messenger3.model.CommonModel
import com.evgeny_m.messenger3.utils.DiffUtilCallback
import com.evgeny_m.messenger3.utils.asTime
import com.evgeny_m.messenger3.utils.currentUserId
import com.evgeny_m.messenger3.utils.showToast

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var listMessagesCache = mutableListOf<CommonModel>()
    private lateinit var diffResult: DiffUtil.DiffResult

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMessageBinding.bind(view)

        val blockUserMessage = binding.itemUserMessageView
        val userMassageText = binding.itemUserMessageText
        val userMassageTime = binding.itemUserMessageTime


        val blockReceiveMessage = binding.itemReceivedMessageView
        val receivedMassageText: TextView = binding.itemReceivedMessageText
        val receivedMessageTime: TextView = binding.itemReceivedMessageTime

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return SingleChatHolder(view)
    }

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {

        if (listMessagesCache[position].from == currentUserId) {

            holder.blockUserMessage.visibility = View.VISIBLE
            holder.blockReceiveMessage.visibility = View.GONE
            holder.userMassageText.text = listMessagesCache[position].text
            holder.userMassageTime.text = listMessagesCache[position].time_stamp.toString().asTime()
            holder.blockUserMessage.setOnClickListener {
                showToast("жмяк")
            }

        } else {

            holder.blockReceiveMessage.visibility = View.VISIBLE
            holder.blockUserMessage.visibility = View.GONE
            holder.receivedMassageText.text = listMessagesCache[position].text
            holder.receivedMessageTime.text =
                listMessagesCache[position].time_stamp.toString().asTime()
            holder.blockUserMessage.setOnClickListener {
                showToast("жмяк")
            }
        }

    }

    override fun getItemCount(): Int {
        return listMessagesCache.size
    }

    fun addItemToBottom(
        item: CommonModel,
        onSuccess: () -> Unit
    ) {
        if (!listMessagesCache.contains(item)) {
            listMessagesCache.add(item)
            notifyItemInserted(listMessagesCache.size)
        }
        onSuccess()
    }

    fun addItemToTop(
        item: CommonModel,
        onSuccess: () -> Unit
    ) {
        if (!listMessagesCache.contains(item)) {
            listMessagesCache.add(item)
            listMessagesCache.sortBy { it.time_stamp.toString() }
            notifyItemInserted(0)
        }
        onSuccess()
    }
}





