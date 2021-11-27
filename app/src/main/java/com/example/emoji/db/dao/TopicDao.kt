package com.example.emoji.db.dao

import androidx.room.Insert
import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.emoji.db.entity.TopicsDB
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface TopicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTopic(item: TopicsDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTopics(items: List<TopicsDB>)

    @Query("Select * from `topics_table`")
    fun getAllTopics(): Flowable<List<TopicsDB>>

    @Query("Select * from `topics_table` where streamId = :streamId")
    fun getTopicsByStreamId(streamId: Int): Single<List<TopicsDB>>

    @Delete
    fun deleteTopic(item: TopicsDB)

    @Update
    fun updateTopic(item: TopicsDB)
}
