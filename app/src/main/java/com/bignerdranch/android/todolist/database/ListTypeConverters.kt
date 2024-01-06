package com.bignerdranch.android.todolist.database

import androidx.room.TypeConverter
import java.util.UUID

class TaskTypeConverters {
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}