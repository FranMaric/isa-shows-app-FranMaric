package com.shows.franmaric.models

import androidx.annotation.DrawableRes

data class Show (
    val ID:String,
    val name:String,
    val description:String,
    @DrawableRes val imageResourceId:Int
    )