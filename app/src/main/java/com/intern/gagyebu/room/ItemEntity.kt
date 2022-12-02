package com.intern.gagyebu.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val amount: Int,
    val title: String,
    val year: Int,
    val month:  Int,
    val day: Int,
    val category: String
)