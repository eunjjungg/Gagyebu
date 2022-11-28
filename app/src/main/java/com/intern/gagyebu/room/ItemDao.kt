package com.intern.gagyebu.room

import androidx.room.Insert
import androidx.room.Query
import com.intern.gagyebu.summary.util.CategoryInfoOfMonth
import com.intern.gagyebu.summary.util.MonthlyCategory
import com.intern.gagyebu.summary.util.SumOfCategory
import kotlinx.coroutines.flow.Flow

import androidx.room.Update

@androidx.room.Dao
interface ItemDao{
    @Insert
    fun saveItem(itemEntity: ItemEntity)

    //필터링 없음 + 금액, 일자 사용자 입력에 따라 정렬
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

    @Query("SELECT * FROM ItemEntity WHERE month = :month AND year = :year")
    fun getItemWhenYearAndMonthSet(year: Int, month: Int) : List<ItemEntity>

    @Query("SELECT amount FROM ItemEntity WHERE month = :month AND year = :year")
    fun getAmountWhenYearAndMonthSet(year: Int, month: Int) : List<Int>

    // 해당 연도의 카테고리 별 총액
    @Query("SELECT category, sum(amount) FROM ItemEntity WHERE year = :year GROUP BY category")
    fun getAmountSumOfEachCategory(year: Int) : List<SumOfCategory>

    // 해당 연도의 월별 총액
    @Query("SELECT month, sum(amount) FROM ItemEntity WHERE year = :year AND category = :category GROUP BY month ORDER BY sum(amount) desc")
    fun getCategorySumOfEachMonth(year: Int, category: String) : List<MonthlyCategory>

    @Query("SELECT category, sum(amount) FROM ItemEntity WHERE year = :year AND month = :month GROUP BY category ORDER BY sum(amount) desc")
    fun getCategoryAndSumWhenYearAndMonthSet(year: Int, month: Int) : List<CategoryInfoOfMonth>
    //해당월 지출 전체
    @Query("SELECT SUM(amount) FROM ItemEntity WHERE NOT category = '수입' AND year= :year AND month = :month")
    fun spendTotal(year: Int, month: Int) : Flow<Int>

    //데이터 삭제
    @Query("DELETE FROM ItemEntity WHERE id = :ID")
    fun deleteItem(ID: Int)

    //데이터 업데이드
    @Update
    fun updateItem(itemEntity: ItemEntity)
}