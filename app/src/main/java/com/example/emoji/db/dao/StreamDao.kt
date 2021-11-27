package com.example.emoji.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Delete
import androidx.room.Update
import com.example.emoji.db.entity.StreamsDB
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface StreamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStream(item: StreamsDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStreams(items: List<StreamsDB>)

    @Query("Select * from `stream_table`")
    fun getAllStreams(): Single<List<StreamsDB>>

    @Delete
    fun deleteStream(item: StreamsDB)

    @Update
    fun updateStream(item: StreamsDB)
}
