package com.intern.gagyebu.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemEntity (
    var amount: Int,
    var title: String,
    var year: Int,
    var month:  Int,
    var day: Int,
    var category: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

inline class IsOrder(val type: Int)
val sortDay = IsOrder(0)
val sortInSpend = IsOrder(1)
val sortInIncome = IsOrder(2)