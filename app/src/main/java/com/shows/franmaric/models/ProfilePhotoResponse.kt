package com.shows.franmaric.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfilePhotoResponse (
    @SerialName("user") val user: User
)