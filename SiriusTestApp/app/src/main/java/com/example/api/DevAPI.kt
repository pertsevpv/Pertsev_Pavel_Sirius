package com.example.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DevAPI {

    @GET("hot/{id}?json=true")
    fun getHotPostList(
        @Path("id") pageId: Int
    ): Call<PostListResult>


    @GET("top/{id}?json=true")
    fun getBestPostList(
        @Path("id") pageId: Int
    ): Call<PostListResult>


    @GET("latest/{id}?json=true")
    fun getFreshPostList(
        @Path("id") pageId: Int
    ): Call<PostListResult>


}