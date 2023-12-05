package com.casaque.onemessagechat.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.casaque.onemessagechat.R
import com.casaque.onemessagechat.adapter.ChatAdapter
import com.casaque.onemessagechat.controller.ChatRoomController
import com.casaque.onemessagechat.databinding.ActivityMainBinding
import com.casaque.onemessagechat.model.Chat
import com.casaque.onemessagechat.model.Constants.CHAT_ARRAY
import com.casaque.onemessagechat.model.Constants.EXTRA_CHAT
import com.casaque.onemessagechat.model.Constants.VIEW_CHAT
import com.casaque.onemessagechat.repository.ChatRoomRepository
import com.casaque.onemessagechat.repository.chatRoomRepository
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    //ViewBinding
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //Data Source
    private val chatList: MutableList<Chat> = mutableListOf()

    private val chatAdapter: ChatAdapter by lazy {
        ChatAdapter(
            this,
            chatList
        )
    }
    private val chatController: ChatRoomController by lazy {
        ChatRoomController(this)
    }

    companion object {
        const val GET_CHATS_MSG = 1
        const val GET_CHATS_INTERVAL = 2000L
    }

    val updateChatListHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
                msg.data.getParcelableArray(CHAT_ARRAY)?.also { chatArray ->

                chatList.clear()
                chatArray.forEach {
                    chatList.add(it as Chat)
                }

                chatAdapter.notifyDataSetChanged()
            }

        }
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)
        amb.chatsLv.adapter=chatAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){result ->
            if (result.resultCode == RESULT_OK){
                val chat = result.data?.getParcelableExtra<Chat>(EXTRA_CHAT)
                chat?.let { _chat ->
                    if(chatList.any { it.id == chat.id }){
                        chatController.editChat(_chat)
                    }else {
                        chatController.insertChat(_chat)
                    }
                }
            }
        }

        amb.chatsLv.setOnItemClickListener{ parent, view, position, id->
            val chat = chatList[position]
            val viewChatIntent = Intent(this, ChatActivity::class.java)
                .putExtra(EXTRA_CHAT, chat)
                .putExtra(VIEW_CHAT,true)

            startActivity(viewChatIntent)
        }

        registerForContextMenu(amb.chatsLv)
        updateChatListHandler.apply {
            sendMessageDelayed(
                obtainMessage().apply { what = GET_CHATS_MSG },
                GET_CHATS_INTERVAL
            )
        }
        chatController.getAllChats()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.exitApp -> {
                finishAffinity()
                true
            }
            R.id.createChatMi -> {
                carl.launch(Intent(this,ChatActivity::class.java))
                true
            }
            else -> true
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position

        return when (item.itemId){
            R.id.editChatMi -> {
                val chatToEdit = chatList[position]
                val editChatIntent = Intent(this, ChatActivity::class.java)
                editChatIntent.putExtra(EXTRA_CHAT, chatToEdit)
                carl.launch(editChatIntent)
                true
            }
            else -> {true}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterForContextMenu(amb.chatsLv)
    }
}