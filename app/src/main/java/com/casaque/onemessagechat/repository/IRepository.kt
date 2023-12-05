package com.casaque.onemessagechat.repository

import java.util.concurrent.CompletableFuture

interface IRepository<T> {
    fun create(obj: T): Int
    fun find(id: String): CompletableFuture<T?>
    fun findAll(): CompletableFuture<List<T>>
    fun update(obj: T): Int
    fun delete(id: String)
}
