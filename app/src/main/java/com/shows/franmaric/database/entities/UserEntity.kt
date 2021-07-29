package com.shows.franmaric.database.entities

import androidx.room.ColumnInfo

data class UserEntity (
    @ColumnInfo(name = "user_id") val id: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "image_url")val imageUrl: String?
)