package cn.eviao.bookstorage.model

import androidx.room.*

@Entity(
    tableName = "books",
    indices = [
        Index(value = ["title"]),
        Index(value = ["isbn"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = Publisher::class,
            parentColumns = ["id"],
            childColumns = ["publisher_id"])
    ])
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val subtitle: String? = null,
    val originTitle: String? = null,

    val image: String? = null,

    val isbn: String,
    val pubdate: String? = null,

    val rating: Double? = null,
    val catalog: String? = null,
    val summary: String? = null,

    @ColumnInfo(name = "publisher_id", index = true)
    val publisherId: Int? = null
)