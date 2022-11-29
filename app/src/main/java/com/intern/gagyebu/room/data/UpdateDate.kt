package com.intern.gagyebu.room.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateDate(val id:Int, val date: String, val title: String, val amount: Int, val category: String ) : Parcelable