package com.example.onemessagechat.model

class User(
    var id: String,
    var username: String
){
    fun getId(): String {
        return this.id;
    }
    fun getUsername(): String {
        return this.username;
    }
}