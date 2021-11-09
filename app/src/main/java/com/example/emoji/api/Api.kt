package com.example.emoji.api

import com.example.emoji.api.model.*
import io.reactivex.Single
import retrofit2.http.*


interface Api {
    @GET("oneMessages")
    fun getMessages(@QueryMap queryMap: Map<String, String>): Single<MessagesResponse>

    @POST("oneMessages")
    fun sendMessage(@QueryMap queryMap: Map<String, String>): Single<MessageResponse>

    @GET("users/me")
    fun getMyUser(): Single<MyUserResponse>

    @GET("users")
    fun getAllUsers(): Single<AllUsersResponse>

    @GET("oneStreams")
    fun getAllStreams(): Single<StreamsResponse>

    @GET("users/me/{stream_id}/oneTopics")
    fun getTopicsByStreamId(@Path("stream_id") id: Int): Single<TopicsResponse>

}