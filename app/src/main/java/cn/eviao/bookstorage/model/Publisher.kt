package cn.eviao.bookstorage.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "publishers",
    indices = [Index(value = ["name"], unique = true)])
data class Publisher(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)