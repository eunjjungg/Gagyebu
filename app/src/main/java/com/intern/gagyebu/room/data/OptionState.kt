package com.intern.gagyebu.room.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore  by preferencesDataStore(name = "option_state")

class OptionState(private val context: Context) {

    private val year = intPreferencesKey("year") // 년도 저장
    private val month = intPreferencesKey("month") // 달 저장
    private val filter = stringPreferencesKey("filter") // 필터값 저장
    private val order = stringPreferencesKey("order") // 정렬값 저장

    val filterFlow : Flow<String> = context.dataStore.data
        .map { Preferences ->  Preferences[filter] ?: ""}

    val orderFlow : Flow<String> = context.dataStore.data
        .map { Preferences ->  Preferences[order] ?: ""}

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
