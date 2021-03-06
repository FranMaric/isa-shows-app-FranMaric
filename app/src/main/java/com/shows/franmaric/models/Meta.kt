package com.shows.franmaric.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meta (
    @SerialName("pagination") val pagination: MetaPagination
        )