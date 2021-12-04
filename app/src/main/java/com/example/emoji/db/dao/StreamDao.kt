package com.example.emoji.db.dao

import androidx.room.*
import com.example.emoji.db.entity.StreamsDB
import io.reactivex.Single

/**
 * @author y.gladkikh
 */
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
