package com.bignerdranch.android.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Types.NULL
import java.util.UUID

@Entity
data class Thing(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var priority: Int = NULL
)
