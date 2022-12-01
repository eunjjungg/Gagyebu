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

    //input: 연도, 월
    //output: 특정 월의 소비 내역을 리턴
    @Query("SELECT * FROM ItemEntity WHERE month = :month AND year = :year AND category != '수입'")
    fun getItemWhenYearAndMonthSet(year: Int, month: Int) : List<ItemEntity>

    //input: 연도, 월
    //output: 특정 월의 소비 금액만 리턴
    @Query("SELECT amount FROM ItemEntity WHERE month = :month AND year = :year AND category != '수입'")
    fun getAmountWhenYearAndMonthSet(year: Int, month: Int) : List<Int>

    //input: 연도
    //output: 특정 연도의 소비 금액을 카테고리 별로 묶어서 카테고리별 소비 금액 리턴
    @Query("SELECT category, sum(amount) FROM ItemEntity WHERE year = :year AND category != '수입' GROUP BY category")
    fun getAmountSumOfEachCategory(year: Int) : List<SumOfCategory>

    //input: 연도, 카테고리
    //output: 특정 연도의 특정 카테고리 소비 내역을 월별로 묶어서 해당 카테고리의 월별 소비 금액을 내림차순으로 정렬한 결과
    @Query("SELECT month, sum(amount) FROM ItemEntity WHERE year = :year AND category = :category AND category != '수입' GROUP BY month ORDER BY sum(amount) desc")
    fun getCategorySumOfEachMonth(year: Int, category: String) : List<MonthlyCategory>

    //input: 연도, 월, 카테고리
    //output: 특정 월의 특정 카테고리 소비 내역을 내림차순으로 정렬하여 가장 첫 번째 Item 리턴
    @Query("SELECT * FROM (SELECT * FROM ItemEntity WHERE year = :year AND category = :category AND month = :month AND category != '수입' ORDER BY amount desc) LIMIT 1")
    fun getTopCostItem(year: Int, month: Int, category: String) : ItemEntity

    //input: 연도, 월
    //output: 특정 월의 소비 내역을 카테고리로 묶어서 각 카테고리와 해당 카테고리 소비 금액을 내림차순으로 정렬한 결과
    @Query("SELECT category, sum(amount) FROM ItemEntity WHERE year = :year AND month = :month AND category != '수입' GROUP BY category ORDER BY sum(amount) desc")
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