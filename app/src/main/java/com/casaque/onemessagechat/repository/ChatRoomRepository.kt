package com.casaque.onemessagechat.repository

import android.util.Log
import com.casaque.onemessagechat.model.Chat
import com.google.firebase.database.*
import java.util.concurrent.CompletableFuture

val chatRoomRepository = ChatRoomRepository()

class ChatRoomRepository : IRepository<Chat> {

    private val db = FirebaseDatabase.getInstance().reference.child("chat")

    override fun create(obj: Chat): Int {
        db.child(obj.id.toString()).setValue(obj)
        return obj.id
    }

    override fun delete(id: String) {
        db.child(id).removeValue()
    }

    override fun update(obj: Chat): Int {
        db.child(obj.id.toString()).setValue(obj)
        return obj.id
    }

    override fun find(id: String): CompletableFuture<Chat?> {
        val future = CompletableFuture<Chat?>()

        db.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chat = snapshot.getValue(Chat::class.java)
                future.complete(chat)
            }

            override fun onCancelled(error: DatabaseError) {
                future.completeExceptionally(error.toException())
            }
        })

        return future
    }

    override fun findAll(): CompletableFuture<List<Chat>> {
        val future = CompletableFuture<List<Chat>>()

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatList = mutableListOf<Chat>()

                for (childSnapshot in snapshot.children) {
                    val chat = childSnapshot.getValue(Chat::class.java)
                    chat?.let { chatList.add(it) }
                }

                future.complete(chatList)
            }

            override fun onCancelled(error: DatabaseError) {
                future.completeExceptionally(error.toException())
            }
        })

        return future
    }
}
