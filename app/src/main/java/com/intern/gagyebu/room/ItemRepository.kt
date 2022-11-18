package com.intern.gagyebu.room

class ItemRepository(private val itemDao: ItemDao) {
    suspend fun getItemsWhenYearAndMonthSet(year: Int, month: Int)
    : List<ItemEntity> {
        return itemDao.getItemWhenYearAndMonthSet(2022, 11)
    }
}