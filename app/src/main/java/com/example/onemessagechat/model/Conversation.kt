package com.example.onemessagechat.model

class Conversation(
    var conversationId: String,
    var currentMessage: String,
    var subscribers: MutableList<User>
) {
    fun getConversationId(): String{
        return this.conversationId;
    }
    fun getCurrentMessage(): String{
        return this.currentMessage;
    }
    fun getSubscribers(): MutableList<User>{
        return this.subscribers;
    }
    fun addSubscriber(user: User){
        this.subscribers.add(user);
    }
    fun removeSubscriber(user: User){
        this.subscribers.remove(user);
    }
}