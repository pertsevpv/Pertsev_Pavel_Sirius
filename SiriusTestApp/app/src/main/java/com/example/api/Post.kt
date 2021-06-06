package com.example.api

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    @field:Json(name = "id") var id: Int = 0,
    @field:Json(name = "description") var title: String = "",
    @field:Json(name = "gifURL") var gifUrl: String = ""
) : Parcelable

@Parcelize
data class PostListResult(
    @field:Json(name = "result") var list: List<Post> = emptyList()
) : Parcelable