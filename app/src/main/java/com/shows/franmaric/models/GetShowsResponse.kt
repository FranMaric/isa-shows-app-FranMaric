package com.shows.franmaric.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetShowsResponse (
    @SerialName("shows") val shows: List<ShowResponse>,
    @SerialName("meta") val meta: Meta
)