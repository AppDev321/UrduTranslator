package com.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieResponse(
    val page: Int?,
    val results: List<Results?>?
):Parcelable {
    @Parcelize
    data class Results(
        val id: Long?=0,
        val original_title: String?="",
        val overview: String?="",
        val poster_path: String?="",
        val vote_average:String?=""
    ):Parcelable
}