package com.shows.franmaric.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostReviewResponse (
    @SerialName("review") val review: Review
)