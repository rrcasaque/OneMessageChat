package com.casaque.onemessagechat.controller

import android.os.Message
import com.casaque.onemessagechat.model.Chat
import com.casaque.onemessagechat.model.Constants
import com.casaque.onemessagechat.repository.ChatRoomRepository
import com.casaque.onemessagechat.view.MainActivity

class ChatRoomController(private val mainActivity: MainActivity) {
    private val chatRepository: ChatRoomRepository = ChatRoomRepository()

    fun insertChat(chat: Chat) {
        Thread {
            chatRepository.create(chat)
            getAllChats()
        }.start()
    }

    interface OnChatFoundListener {
        fun onChatFound(chat: Chat)
        fun onChatNotFound()
    }

    fun getAllChats() {
        Thread {
            val returnList = chatRepository.findAll().get()

            mainActivity.updateChatListHandler.apply {
                sendMessage(Message().apply {
                    data.putParcelableArray(
                        Constants.CHAT_ARRAY,
                        returnList.toTypedArray()
                    )
                })
            }
        }.start()
    }

    fun editChat(chat: Chat) {
        Thread {
            chatRepository.update(chat)
        }.start()
    }
}
