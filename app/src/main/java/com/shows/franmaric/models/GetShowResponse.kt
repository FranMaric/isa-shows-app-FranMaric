package com.shows.franmaric.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetShowResponse (
    @SerialName("show") val show: ShowResponse
)
