package com.intern.gagyebu.room

import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface ItemDao{
    @Insert
    fun saveItem(itemEntity: ItemEntity)

    //기본 쿼리값 (해당 월 일별 정렬)
    @Query("SELECT * FROM ItemEntity WHERE month = :month ORDER BY day")
    fun sortDay(month: Int) : Flow<List<ItemEntity>>

    //해댕 월 금액별 정렬
    @Query("SELECT * FROM ItemEntity WHERE month = :month ORDER BY amount")
    fun sortAmount(month: Int) : Flow<List<ItemEntity>>

    //지출 필터링 + 월/금액정렬
    @Query("SELECT * FROM ItemEntity WHERE category = '수입' AND month = :month ORDER BY "+
            "CASE :order WHEN 'day' THEN day END DESC," +
            "CASE :order WHEN 'amount' THEN amount END DESC")
    fun sortInSpend(month: Int, order: String) : Flow<List<ItemEntity>>

    //수입 필터링 + 월/금액정렬
    @Query("SELECT * FROM ItemEntity WHERE NOT category = '수입' AND month = :month ORDER BY "+
            "CASE :order WHEN 'day' THEN day END DESC," +
            "CASE :order WHEN 'amount' THEN amount END DESC")
    fun sortInIncome(month: Int, order: String) : Flow<List<ItemEntity>>

    @Query("DELETE FROM ItemEntity WHERE id = :ID")
    fun deleteItem(ID: Int)
}