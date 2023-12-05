package com.casaque.onemessagechat.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Chat (
    @PrimaryKey(autoGenerate = true)
    var id: Int = -1,
    @NonNull
    var message: String = ""
): Parcelable