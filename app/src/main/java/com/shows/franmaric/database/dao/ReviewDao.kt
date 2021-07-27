package com.shows.franmaric.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shows.franmaric.database.entities.ReviewEntity

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReview(review: ReviewEntity)

    @Query("SELECT * FROM show WHERE id IS :reviewId")
    fun getShow(reviewId: String) : LiveData<ReviewEntity>

    @Query("SELECT * FROM review")
    fun getReviews() : LiveData<List<ReviewEntity>>


}