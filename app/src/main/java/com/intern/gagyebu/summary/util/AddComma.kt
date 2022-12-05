package com.intern.gagyebu

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import java.text.DecimalFormat

interface Comma {
    fun Flow<Int>.addComma(): Flow<String> = transform { value ->
        val dec = DecimalFormat("###,###")
        return@transform emit(dec.format(value))
    }

    fun Int.addComma(): String {
        val dec = DecimalFormat("###,###")
        return (dec.format(this))
    }
}