package com.shows.franmaric.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetTopRatedShowsResponse (
    @SerialName("shows") val shows: List<ShowResponse>
)