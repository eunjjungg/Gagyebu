package com.intern.gagyebu.room.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.intern.gagyebu.dialog.SelectableOptionsEnum
import com.intern.gagyebu.main.MainActivity.Companion.MONTH
import com.intern.gagyebu.main.MainActivity.Companion.YEAR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "option_state")

class OptionState(private val context: Context) {

    private val year = intPreferencesKey("year") // 년도 저장
    private val month = intPreferencesKey("month") // 달 저장
    private val filter = stringPreferencesKey("filter") // 필터값 저장
    private val order = stringPreferencesKey("order") // 정렬값 저장

    val yearFlow : Flow<Int> = context.dataStore.data
        .map { Preferences ->  Preferences[year] ?: YEAR}

    val monthFlow : Flow<Int> = context.dataStore.data
        .map { Preferences ->  Preferences[month] ?: MONTH}

    val filterFlow : Flow<String> = context.dataStore.data
        .map { Preferences ->  Preferences[filter] ?: SelectableOptionsEnum.DEFAULT.toString()}

    val orderFlow : Flow<String> = context.dataStore.data
        .map { Preferences ->  Preferences[order] ?: SelectableOptionsEnum.day.toString()}

    suspend fun setYear(value : Int){
        context.dataStore.edit {
                Preferences -> Preferences[year] = value
        }
    }

    suspend fun setMonth(value : Int){
        context.dataStore.edit {
                Preferences -> Preferences[month] = value
        }
    }

    suspend fun setFilter(value : String){
        context.dataStore.edit {
                Preferences -> Preferences[filter] = value
        }
    }

    suspend fun setOrder(value : String){
        context.dataStore.edit {
                Preferences -> Preferences[order] = value
        }
    }
}
