package com.example.onemessagechat.model

class ChatApp(
    var conversations: MutableList<Conversation>,
    var currentUser: User
) {
    fun getConversations(): MutableList<Conversation>{
        return this.conversations;
    }
    fun getCurrentUser(): User {
        return this.currentUser;
    }
    fun addConversation(conversation: Conversation){
        this.conversations.add(conversation);
    }
    fun removeConversation(conversation: Conversation){
        this.conversations.remove(conversation);
    }
}