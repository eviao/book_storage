package cn.eviao.bookstorage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "authors",
    indices = [Index(value = ["name"], unique = true)])
data class Author(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)
