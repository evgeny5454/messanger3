package com.evgeny_m.messenger3.fragments.main.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.evgeny_m.messenger3.R
import com.evgeny_m.messenger3.databinding.FragmentContactsBinding
import com.evgeny_m.messenger3.databinding.ItemContactBinding
import com.evgeny_m.messenger3.model.CommonModel
import com.evgeny_m.messenger3.utils.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference


class ContactsFragment : Fragment() {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var refContacts: DatabaseReference
    private lateinit var refUsers: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        toolbar = binding.contactsToolbar
        toolbar.title = "Contacts"
        initBackButton(toolbar)
        initRecyclerView_Database_Adapter()

    }

    private fun initRecyclerView_Database_Adapter() {
        recyclerView = binding.contactsListView
        refContacts = database.child(NODE_PHONE_CONTATS).child(currentUserId)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(refContacts, CommonModel::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_contact, parent, false)
                return ContactsHolder(view)
            }

            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: CommonModel
            ) {
                holder.name.text = model.userfullname
                refUsers = database.child(NODE_USERS).child(model.id)
                refUsers.addValueEventListener(AppValueEventListener {
                    val contact = it.getCommonModel()

                    holder.status.text = contact.status

                    Glide
                        .with(APP_ACTIVITY)
                        .load(contact.photoUrl)
                        .centerCrop()
                        .into(holder.photo)
                    holder.itemView.setOnClickListener {
                        receivingUserId = model.id
                        receivingUserFullName = model.userfullname

                        view?.
                        findNavController()?.
                        navigate(R.id.action_contactsFragment_to_singleChatFragment)
                    }
                })
            }
        }
        recyclerView.adapter = adapter
        adapter.startListening()
    }


    class ContactsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingAdapter = ItemContactBinding.bind(view)
        val name = bindingAdapter.itemContactUserFullName
        val status = bindingAdapter.itemContactUserStatus
        val photo = bindingAdapter.itemContactUserPhoto
    }

    override fun onPause() {
        super.onPause()
        adapter.stopListening()
    }

    companion object {
        lateinit var receivingUserId: String
        lateinit var receivingUserFullName: String
    }
}