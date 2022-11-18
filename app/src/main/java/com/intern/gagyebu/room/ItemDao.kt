package com.intern.gagyebu.room

import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface ItemDao{
    @Insert
    fun saveItem(itemEntity: ItemEntity)

    @Query("SELECT * FROM ItemEntity WHERE month = :month AND year = :year")
    fun getItemWhenYearAndMonthSet(year: Int, month: Int) : List<ItemEntity>
}