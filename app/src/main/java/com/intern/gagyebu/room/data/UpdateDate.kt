package com.intern.gagyebu.room.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateDate(val id:Int, val amount: Int, val title: String, val date: String, val category: String ) : Parcelable