package com.evgeny_m.messenger3.fragments.main.single_chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.evgeny_m.messenger3.databinding.FragmentSingleChatBinding
import com.evgeny_m.messenger3.fragments.main.ContactsFragment.Companion.receivingUserFullName
import com.evgeny_m.messenger3.fragments.main.ContactsFragment.Companion.receivingUserId
import com.evgeny_m.messenger3.model.CommonModel
import com.evgeny_m.messenger3.model.UserModel
import com.evgeny_m.messenger3.utils.*
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference


class SingleChatFragment : Fragment() {

    private lateinit var binding: FragmentSingleChatBinding
    private lateinit var toolbar: Toolbar

    private lateinit var listenerUserDataToolbar: AppValueEventListener
    private lateinit var messagesListener: AppChildEventListener
    private lateinit var receivingUser: UserModel
    private lateinit var refUser: DatabaseReference
    private lateinit var refMessages: DatabaseReference
    private lateinit var adapter: SingleChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var userName: TextView
    private lateinit var userStatus: TextView
    private lateinit var userPhoto: ImageView

    //private var listListeners = mutableListOf<AppChildEventListener>()
    private var countMessages = 13
    private var isScrolling = false
    private var smoothScroll = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initFields()
        initToolbar()
        initBackButton(toolbar)
        initListener()
        initDatabase()
        initRecycleView()

    }

    private fun initFields() {
        swipeRefreshLayout = binding.singleChatSwipeRefresh
        layoutManager = LinearLayoutManager(this.context)
    }

    private fun initRecycleView() {
        recyclerView = binding.singleChatMessagesList
        adapter = SingleChatAdapter()
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = layoutManager
        refMessages.limitToLast(countMessages).addChildEventListener(messagesListener)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isScrolling && dy < 0 && layoutManager.findFirstVisibleItemPosition() <= 3) {
                    updateData()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }
        })
        swipeRefreshLayout.setOnRefreshListener { updateData() }
    }

    private fun updateData() {
        smoothScroll = false
        isScrolling = false
        countMessages += 10
        refMessages.removeEventListener(messagesListener)
        refMessages.limitToLast(countMessages).addChildEventListener(messagesListener)
    }

    private fun initDatabase() {
        refUser = database.child(NODE_USERS).child(receivingUserId)
        refUser.addValueEventListener(listenerUserDataToolbar)
        refMessages = database.child(NODE_MESSAGES).child(currentUserId).child(receivingUserId)

        binding.singleChatBtnSendMessage.setOnClickListener {
            smoothScroll = true
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
        messagesListener = AppChildEventListener {
            val message = it.getCommonModel()
            if (smoothScroll) {
                adapter.addItemToBottom(message) {
                    recyclerView.smoothScrollToPosition(adapter.itemCount)
                }
            } else {
                adapter.addItemToTop(message) {
                    swipeRefreshLayout.isRefreshing = false
                }
            }
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