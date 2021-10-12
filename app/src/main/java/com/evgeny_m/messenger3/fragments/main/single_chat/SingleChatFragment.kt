package com.evgeny_m.messenger3.fragments.main.single_chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.evgeny_m.messenger3.databinding.FragmentSingleChatBinding
import com.evgeny_m.messenger3.fragments.main.ContactsFragment.Companion.receivingUserFullName
import com.evgeny_m.messenger3.fragments.main.ContactsFragment.Companion.receivingUserId
import com.evgeny_m.messenger3.model.CommonModel
import com.evgeny_m.messenger3.model.UserModel
import com.evgeny_m.messenger3.utils.*
import com.google.firebase.database.DatabaseReference


class SingleChatFragment : Fragment() {

    private lateinit var binding: FragmentSingleChatBinding
    private lateinit var toolbar: Toolbar

    private lateinit var listenerUserDataToolbar : AppValueEventListener
    private lateinit var messagesListener: AppValueEventListener

    private lateinit var receivingUser : UserModel
    private lateinit var refUser : DatabaseReference
    private lateinit var refMessages: DatabaseReference
    private lateinit var adapter: SingleChatAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var userName: TextView
    private lateinit var userStatus: TextView
    private lateinit var userPhoto : ImageView

    private var listMessages = emptyList<CommonModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initToolbar()
        initBackButton(toolbar)
        initListener()
        initDatabase()
        initRecycleView()

    }

    private fun initRecycleView() {
        recyclerView = binding.singleChatMessagesList
        adapter = SingleChatAdapter()
        recyclerView.adapter = adapter
        refMessages.addValueEventListener(messagesListener)
    }

    private fun initDatabase() {
        refUser = database.child(NODE_USERS).child(receivingUserId)
        refUser.addValueEventListener(listenerUserDataToolbar)
        refMessages = database.child(NODE_MESSAGES).child(currentUserId).child(receivingUserId)

        binding.singleChatBtnSendMessage.setOnClickListener {
            val message = binding.singleChatEditMessage.text.toString()
            if (message.isEmpty()) {
                showToast("message is empty")
            } else {
                sendMassage(message, receivingUserId, TYPE_TEXT) {
                    binding.singleChatEditMessage.setText("")
                }
            }
        }
    }

    private fun initListener() {
        listenerUserDataToolbar = AppValueEventListener {
            receivingUser = it.getUserModel()
            updateDataToolbar()
        }
        messagesListener = AppValueEventListener { dataSnapshot ->
            listMessages = dataSnapshot.children.map {
                it.getCommonModel()
            }
            adapter.setList(listMessages)
            recyclerView.smoothScrollToPosition(adapter.itemCount)
        }

    }

    private fun updateDataToolbar() {
        Glide
            .with(APP_ACTIVITY)
            .load(receivingUser.photoUrl)
            .centerCrop()
            .into(userPhoto)

        userName.text = receivingUserFullName
        userStatus.text = receivingUser.status
    }

    private fun initToolbar() {
        toolbar = binding.singleChatToolbar
        userName = binding.singleChatUserFullName
        userStatus = binding.singleChatUserStatus
        userPhoto = binding.singleChatUserImage
    }

    override fun onPause() {
        super.onPause()
        refUser.removeEventListener(listenerUserDataToolbar)
        refMessages.removeEventListener(messagesListener)
    }

}