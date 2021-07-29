package com.shows.franmaric.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review")
data class ReviewEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "comment") val comment: String,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "show_id") val showId: Int,
    @Embedded val user: UserEntity?
)