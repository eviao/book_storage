package cn.eviao.bookstorage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "books_tags_reference",
    primaryKeys = ["book_id", "tag_id"],
    foreignKeys = [
        ForeignKey(entity = Book::class, parentColumns = ["id"], childColumns = ["book_id"]),
        ForeignKey(entity = Tag::class, parentColumns = ["id"], childColumns = ["tag_id"])
    ]
)
data class BookTag(
    @ColumnInfo(name = "book_id", index = true)
    val bookId: Int,
    @ColumnInfo(name = "tag_id", index = true)
    val tagId: Int
)

@Entity(
    tableName = "books_authors_reference",
    primaryKeys = ["book_id", "author_id"],
    foreignKeys = [
        ForeignKey(entity = Book::class, parentColumns = ["id"], childColumns = ["book_id"]),
        ForeignKey(entity = Author::class, parentColumns = ["id"], childColumns = ["author_id"])
    ]
)
data class BookAuthor(
    @ColumnInfo(name = "book_id", index = true)
    val bookId: Int,
    @ColumnInfo(name = "author_id", index = true)
    val authorId: Int
)