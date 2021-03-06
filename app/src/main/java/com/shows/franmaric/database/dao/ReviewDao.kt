package com.shows.franmaric.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shows.franmaric.database.entities.ReviewEntity

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReviews(reviews: List<ReviewEntity>)

    @Query("SELECT * FROM review WHERE id IS :reviewId")
    fun getReview(reviewId: String) : ReviewEntity

    @Query("SELECT * FROM review WHERE show_id IS :showId")
    fun getReviews(showId: Int) : List<ReviewEntity>

    @Delete
    fun deleteReview(review: ReviewEntity)
}