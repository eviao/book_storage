package cn.eviao.bookstorage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "books_tags_reference",
    foreignKeys = [
        ForeignKey(entity = Book::class, parentColumns = ["id"], childColumns = ["book_id"]),
        ForeignKey(entity = Tag::class, parentColumns = ["id"], childColumns = ["tag_id"])
    ]
)
data class BookTagRef(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "book_id", index = true)
    val bookId: Long,
    @ColumnInfo(name = "tag_id", index = true)
    val tagId: Long
)