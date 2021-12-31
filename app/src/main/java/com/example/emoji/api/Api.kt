package com.example.emoji.api

import com.example.emoji.api.model.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface Api {
    @GET("messages")
    fun getMessages(@QueryMap queryMap: Map<String, String>): Single<MessagesResponse>

    @POST("messages")
    fun sendMessage(@QueryMap queryMap: Map<String, String>): Completable

    @GET("users/me")
    fun getMyUser(): Single<MyUserResponse>

    @GET("users")
    fun getAllUsers(): Single<AllUsersResponse>

    @GET("streams")
    fun getAllStreams(): Single<StreamsResponse>

    @GET("users/me/{stream_id}/topics")
    fun getTopicsByStreamId(@Path("stream_id") id: Int): Single<TopicsResponse>

    @POST("messages/{message_id}/reactions")
    fun addMessageReaction(@Path("message_id") id: Int, @Query("emoji_name") reactionName: String): Single<Reaction>

    @DELETE("messages/{id}/reactions")
    fun removeMessageReaction(@Path("id") id: Int, @Query("emoji_name") reactionName: String): Completable

    @GET("users/{id}/presence")
    fun getUserPresence(@Path("id") id: Int): Single<PresenceResponse>

    @GET("/static/generated/emoji/emoji_codes.json")
    fun getReactions(): Single<GetReactionsResponse>
}
