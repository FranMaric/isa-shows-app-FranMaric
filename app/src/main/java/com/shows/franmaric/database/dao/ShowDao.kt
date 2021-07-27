package com.shows.franmaric.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shows.franmaric.database.entities.ShowEntity

@Dao
interface ShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShows(shows: List<ShowEntity>)

    @Query("SELECT * FROM show WHERE id IS :showId")
    fun getShow(showId: String) : LiveData<ShowEntity>

    @Query("SELECT * FROM show")
    fun getShows() : LiveData<List<ShowEntity>>
}