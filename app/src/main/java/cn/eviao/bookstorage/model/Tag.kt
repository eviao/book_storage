package cn.eviao.bookstorage.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tags",
    indices = [Index(value = ["text"], unique = true)]
)
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val text: String
)