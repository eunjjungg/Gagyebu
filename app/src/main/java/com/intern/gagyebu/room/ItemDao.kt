package com.intern.gagyebu.room

import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface ItemDao{
    @Insert
    fun saveItem(itemEntity: ItemEntity)

    //해당 년, 월 일별 정렬 (기본 쿼리값)
    @Query("SELECT * FROM ItemEntity WHERE year= :year AND month = :month ORDER BY "+
            "CASE :order WHEN 'day' THEN day END DESC," +
            "CASE :order WHEN 'amount' THEN amount END DESC")
    fun sortDay(year: Int, month: Int, order: String) : Flow<List<ItemEntity>>

    //수입 필터링 + 월/금액정렬
    @Query("SELECT * FROM ItemEntity WHERE category = '수입' AND year= :year AND month = :month ORDER BY "+
            "CASE :order WHEN 'day' THEN day END DESC," +
            "CASE :order WHEN 'amount' THEN amount END DESC")
    fun sortInIncome(year: Int, month: Int, order: String) : Flow<List<ItemEntity>>

    //지출 필터링 + 월/금액정렬
    @Query("SELECT * FROM ItemEntity WHERE NOT category = '수입' AND year= :year AND month = :month ORDER BY "+
            "CASE :order WHEN 'day' THEN day END DESC," +
            "CASE :order WHEN 'amount' THEN amount END DESC")
    fun sortInSpend(year: Int, month: Int, order: String) : Flow<List<ItemEntity>>

    //해당월 수입 전체
    @Query("SELECT SUM(amount) FROM ItemEntity WHERE category = '수입' AND year= :year AND month = :month")
    fun incomeTotal(year: Int, month: Int) : Flow<Int>

    //해당월 지출 전체
    @Query("SELECT SUM(amount) FROM ItemEntity WHERE NOT category = '수입' AND year= :year AND month = :month")
    fun spendTotal(year: Int, month: Int) : Flow<Int>

    @Query("DELETE FROM ItemEntity WHERE id = :ID")
    fun deleteItem(ID: Int)
}