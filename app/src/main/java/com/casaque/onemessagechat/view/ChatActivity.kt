package com.casaque.onemessagechat.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.casaque.onemessagechat.controller.ChatRoomController
import com.casaque.onemessagechat.databinding.ChatActivityBinding
import com.casaque.onemessagechat.model.Chat
import com.casaque.onemessagechat.model.Constants.EXTRA_CHAT
import com.casaque.onemessagechat.model.Constants.VIEW_CHAT
import com.casaque.onemessagechat.repository.chatRoomRepository
import kotlin.random.Random

class ChatActivity : AppCompatActivity() {
    private val cab: ChatActivityBinding by lazy {
        ChatActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(cab.root)

        setSupportActionBar(cab.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Chat details"

        val receivedChat = intent.getParcelableExtra<Chat>(EXTRA_CHAT)
        receivedChat?.let { _receivedChat ->
            val viewChat: Boolean = intent.getBooleanExtra(VIEW_CHAT, false)
            with(cab) {
                val generatedId = _receivedChat.id ?: generateId()
                idTv.text = generatedId.toString()
                messageEt.setText(_receivedChat.message)

                saveBt.setOnClickListener {
                    val editedChat = Chat(
                        id = generatedId,
                        message = messageEt.text.toString()
                    )

                    val chatRoomController = ChatRoomController(MainActivity())

                    chatRoomController.editChat(editedChat)

                    val resultIntent = Intent()
                    resultIntent.putExtra(EXTRA_CHAT, editedChat)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }

    private fun generateId() = Random(System.currentTimeMillis()).nextInt()
}
